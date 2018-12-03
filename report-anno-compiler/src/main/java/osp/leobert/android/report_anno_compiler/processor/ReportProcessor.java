package osp.leobert.android.report_anno_compiler.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import osp.leobert.android.report_anno_compiler.Mode;
import osp.leobert.android.report_anno_compiler.Writeable;
import osp.leobert.android.report_anno_compiler.utils.Logger;
import osp.leobert.android.report_anno_compiler.utils.Utils;
import osp.leobert.android.reportprinter.spi.Model;
import osp.leobert.android.reportprinter.spi.ReporterExtension;
import osp.leobert.android.reportprinter.spi.Result;

import static osp.leobert.android.report_anno_compiler.Consts.KEY_MODULE_NAME;
import static osp.leobert.android.report_anno_compiler.Consts.MODE;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.processor </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ReportProcessor </p>
 * <p><b>Description:</b> ReportProcessor </p>
 * Created by leobert on 2018/11/17.
 */
@AutoService(Processor.class)
@SupportedOptions({KEY_MODULE_NAME, MODE})
//@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ReportProcessor extends AbstractProcessor {

    private Set<ReporterExtension> extensions;
    private final ClassLoader loaderForExtensions;

    private Logger logger;

    private Elements elements;
    private String module;
    private Mode _mode;

    private Filer filer;

    public ReportProcessor() {
        this(ReportProcessor.class.getClassLoader());
    }

    private ReportProcessor(ClassLoader loaderForExtensions) {
        this.loaderForExtensions = loaderForExtensions;
        this.extensions = null;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        elements = processingEnv.getElementUtils();
        logger = new Logger(processingEnv.getMessager());
        String mode = "";
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            module = options.get(KEY_MODULE_NAME);
            mode = options.get(MODE);
            logger.info(">>> module is " + module + "  mode is:" + mode + " <<<");
        }
        if (module == null || module.equals("")) {
            module = "default";
        }
        _mode = Mode.customValueOf(mode);
        filer = processingEnvironment.getFiler();

        try {
            extensions =
                    ImmutableSet.copyOf(ServiceLoader.load(ReporterExtension.class, loaderForExtensions));

            StringBuilder tmp = new StringBuilder();
            for (ReporterExtension ext : extensions) {
                tmp.append(ext.getClass().getName()).append(" ; ");
            }
            logger.info(">>> check extensions:" + tmp.toString());
            // ServiceLoader.load returns a lazily-evaluated Iterable, so evaluate it eagerly now
            // to discover any exceptions.
        } catch (Throwable t) {
            StringBuilder warning = new StringBuilder();
            warning.append("An exception occurred while looking for ReporterExtension extensions. "
                    + "No extensions will function.");
            if (t instanceof ServiceConfigurationError) {
                warning.append(" This may be due to a corrupt jar file in the compiler's classpath.");
            }
            warning.append(" Exception: ")
                    .append(t);
            logger.warning(warning.toString());
            extensions = ImmutableSet.of();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotations = Sets.newLinkedHashSet();
        for (ReporterExtension ext : extensions) {
            supportedAnnotations.addAll(ext.applicableAnnotations());
        }

        return supportedAnnotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            boolean handleByAnyOne = false;
            for (ReporterExtension ext : extensions) {
                Set<String> targetAnnotations = ext.applicableAnnotations();
                Map<String, List<Model>> previousData = new HashMap<>();

                for (String anno : targetAnnotations) {
                    List<Model> modelsForOneAnnotation = new ArrayList<>();

                    TypeElement annoType = elements.getTypeElement(anno);
                    Set<? extends Element> hitElements = roundEnvironment.getElementsAnnotatedWith(annoType);

                    for (Element element : hitElements) {
                        Model model = Model.newBuilder()
                                .annoType(annoType)
                                .element(element)
                                .elementKind(element.getKind())
                                .name(findElementName(element))
                                .build();
                        modelsForOneAnnotation.add(model);
                    }

                    previousData.put(anno, modelsForOneAnnotation);
                }

                Result result = ext.generateReport(previousData);
                if (result == null)
                    continue;
                handleByAnyOne = handleByAnyOne | result.isHandled();
                if (result.isHandled()) {
                    if (Mode.MODE_FILE.equals(_mode))
                        generateReport(result);
                    else
                        generateExeReport(result);
                }
            }
            return handleByAnyOne;
        }
        return false;
    }

    private String findElementName(Element element) {
        String name = "unknown element name";
        try {
            if (element.getKind().isClass() || element.getKind().isInterface()) {

                String path = element.getEnclosingElement().toString();
                String simpleName = element.getSimpleName().toString();

                name = path + "." + simpleName;
            } else {//field
                name = findLocation(element);
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return name;
    }

    private String findLocation(Element element) {
        String path = "";

        if (element != null) {
            Element parent = element.getEnclosingElement();
            if (element.getKind().isClass() || element.getKind().isInterface()) {
                String p = element.getEnclosingElement().toString();
                String s = element.getSimpleName().toString();

                return p + "." + s;
            }
            return findLocation(parent) +
                    "#" + element.toString();
        }
        return path;
    }

    private void generateReport(Result result) {
        String fileName = Utils.generateReportFilePath(module + result.getReportFileNamePrefix(), result.getFileExt());
        logger.info("generate " + fileName);
        if (Utils.createFile(fileName)) {
            Utils.writeStringToFile(fileName, result.getReportContent(), false);
            logger.info("generate success");
        } else {
            logger.info("generate failure");
        }
    }

    private void generateExeReport(Result result) {
        String fileName = Utils.genFileName(module + result.getReportFileNamePrefix(), result.getFileExt());
        logger.info("generate " + fileName);
        Writeable writeable = Writeable.DirectionWriter.of(new File("./" + module + "/ext"));
        Utils.generatePrinterClass(Utils.genReporterClzName(module + result.getReportFileNamePrefix()),
                fileName,
                result.getReportContent(),
                writeable);
        logger.info("generate success");

    }
}
