package osp.leobert.android.report_anno_compiler.processor;

import com.google.auto.service.AutoService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import osp.leobert.android.report_anno_compiler.bean.ChangeLogItem;
import osp.leobert.android.report_anno_compiler.utils.FileUtils;
import osp.leobert.android.report_anno_compiler.utils.Logger;
import osp.leobert.android.report_anno_compiler.utils.Utils;
import osp.leobert.android.reportprinter.notation.ChangeLog;

import static osp.leobert.android.report_anno_compiler.Consts.ANNOTATION_TYPE_CHANGELOG;
import static osp.leobert.android.report_anno_compiler.Consts.KEY_MODULE_NAME;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.processor </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ChangeLogProcessor </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/8.
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ANNOTATION_TYPE_CHANGELOG})
public class ChangeLogProcessor extends AbstractProcessor {

    private Logger logger;

    private Elements elements;
    private ArrayList<ChangeLogItem> changeLogItems;

    private String module;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        changeLogItems = new ArrayList<>();

        elements = processingEnv.getElementUtils();
        logger = new Logger(processingEnv.getMessager());

        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            module = options.get(KEY_MODULE_NAME);
            logger.info(">>> module is " + module + " <<<");
        }
        if (module == null || module.equals("")) {
            module = "default";
        }

        logger.info(">>> ChangeLogPrintProcessor init. <<<");
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> changeLogs = roundEnvironment.getElementsAnnotatedWith(ChangeLog.class);
            try {
                logger.info(">>> Found ChangeLogs, start... <<<");
                parseInfo(changeLogs);
            } catch (Exception e) {
                logger.error(e);
            }
            generateReport();
            return true;
        }
        return false;
    }

    private void parseInfo(Set<? extends Element> bugs) {
        for (Element element : bugs) {
            ChangeLog changeLog = element.getAnnotation(ChangeLog.class);
            String version = changeLog.version();
            String[] desc = changeLog.changes();

            String name = "cannot track";

            if (element.getKind().isClass() || element.getKind().isInterface()) {

                String path = element.getEnclosingElement().toString();
                String simpleName = element.getSimpleName().toString();

                name = path + "." + simpleName;
            } else {//field
                String path = findLocation(element);//element.getEnclosingElement().toString();
                String simpleName = element.getSimpleName().toString();

                name = path;//+ "#" + simpleName;
            }

            changeLogItems.add(new ChangeLogItem(version, desc, name, element.getKind()));
        }
    }

    private String findLocation(Element element) {
        String path = "";

        if (element != null) {
            Element parent = element.getEnclosingElement();
//            if (parent == null)
//                return path;
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


    /**
     * generate BugReport.txt
     */
    private void generateReport() {
        logger.info("generate bug report");
        String fileName = Utils.genFileName(module + "ChangeLog");
        if (FileUtils.createFile(fileName)) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("auto generated, do not change !!!! \n\n");

            for (ChangeLogItem item : changeLogItems) {
                stringBuilder.append(item.toString());
                stringBuilder.append("\n");
            }
            FileUtils.writeStringToFile(fileName, stringBuilder.toString(), false);
            logger.info("generate success");
        } else {
            logger.info("generate failure");
        }
    }

}
