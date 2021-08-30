package osp.leobert.android.report_anno_compiler.processor;


import com.google.auto.service.AutoService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
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

import osp.leobert.android.reportprinter.spi.Model;
import osp.leobert.android.reportprinter.spi.ReporterExtension;
import osp.leobert.android.reportprinter.spi.Result;

import static osp.leobert.android.report_anno_compiler.processor.Consts.ACTIVE;
import static osp.leobert.android.report_anno_compiler.processor.Consts.CLZ_WRITER;
import static osp.leobert.android.report_anno_compiler.processor.Consts.KEY_MODULE_NAME;
import static osp.leobert.android.report_anno_compiler.processor.Consts.MODE;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.processor </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ReportProcessor </p>
 * <p><b>Description:</b> ReportProcessor </p>
 * Created by leobert on 2018/11/17.
 */
@AutoService(Processor.class)
@SupportedOptions({KEY_MODULE_NAME, MODE, ACTIVE, CLZ_WRITER})
public class ReportProcessor extends AbstractProcessor {

    private Set<ReporterExtension> extensions;
    private final ClassLoader loaderForExtensions;

    private Logger logger;

    private Elements elements;
    private String module;
    private Mode _mode;
    private State _state;
    private WriterType _writerType;

    private Filer filer;

    public ReportProcessor() {
        this(ReportProcessor.class.getClassLoader());
    }

    private ReportProcessor(ClassLoader loaderForExtensions) {
        this.loaderForExtensions = loaderForExtensions;
        this.extensions = null;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elements = env.getElementUtils();
        logger = new Logger(env.getMessager());
        String mode = "";
        String state = "";
        String writerType = "";
        Map<String, String> options = env.getOptions();
        if (options != null && !options.isEmpty())  {
            module = options.get(KEY_MODULE_NAME);
            mode = options.get(MODE);
            state = options.get(ACTIVE);
            writerType = options.get(CLZ_WRITER);
            logger.info(">>> module is " + module + "  mode is:" + mode +
                    "  state is:" + state + "  writerType is:" + writerType +
                    " <<<");
        }
        if (module == null || module.equals("")) {
            module = "default";
        }
        _mode = Mode.customValueOf(mode);
        _state = State.customValueOf(state);
        _writerType = WriterType.customValueOf(writerType);
        filer = env.getFiler();

        try {
            extensions = new LinkedHashSet<>();
            for (ReporterExtension reporterExtension : ServiceLoader.load(ReporterExtension.class, loaderForExtensions)) {
                extensions.add(reporterExtension);
            }

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
            extensions = Collections.emptySet();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotations = new LinkedHashSet<>();
        for (ReporterExtension ext : extensions) {
            supportedAnnotations.addAll(ext.applicableAnnotations());
        }

        return supportedAnnotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            boolean result = internalProcess(set, roundEnvironment);
            logger.info("process result: " + result);
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    private boolean internalProcess(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) throws Exception {
        if (State.Off.equals(_state)) {
            logger.warning(">>> reporter off");
            return false;
        }
        if (set != null && set.size() > 0) {
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
                        generateExtReportJavaFile(result);
                }
            }
//            return handleByAnyOne; change to always false
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

    private void generateExtReportJavaFile(Result result) {
        String fileName = Utils.genFileName(module + result.getReportFileNamePrefix(), result.getFileExt());
        logger.info("generate " + fileName);
        Writeable writeable = getWriteable();
        Utils.generatePrinterClass(Utils.genReporterClzName(module + result.getReportFileNamePrefix()),
                fileName,
                result.getReportContent(),
                writeable);
        logger.info("generate success");
    }

    private Writeable getWriteable() {
        if (WriterType.Custom.equals(_writerType)) {
            return Writeable.DirectionWriter.of(new File("./" + module + "/ext"));
        } else {
            return Writeable.FilerWriter.of(filer);
        }
    }
}
