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

import osp.leobert.android.report_anno_compiler.bean.Issue;
import osp.leobert.android.report_anno_compiler.utils.FileUtils;
import osp.leobert.android.report_anno_compiler.utils.Logger;
import osp.leobert.android.report_anno_compiler.utils.Utils;
import osp.leobert.android.reportprinter.notation.Bug;
import osp.leobert.android.reportprinter.notation.State;

import static osp.leobert.android.report_anno_compiler.Consts.ANNOTATION_TYPE_BUG;
import static osp.leobert.android.report_anno_compiler.Consts.KEY_MODULE_NAME;

/**
 * <p><b>Package:</b> osp.leobert.android.report_anno_compiler.processor </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> BugProcessor </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/8.
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_MODULE_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ANNOTATION_TYPE_BUG})
@Deprecated
public class BugProcessor extends AbstractProcessor {

    private Logger logger;
    private ArrayList<Issue> issues;

    private String module;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        issues = new ArrayList<>();

        logger = new Logger(processingEnv.getMessager());

        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            module = options.get(KEY_MODULE_NAME);
            logger.info(">>> module is " + module + " <<<");
        }
        if (module == null || module.equals("")) {
            module = "default";
        }

        logger.info(">>> BugPrintProcessor init. <<<");
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            Set<? extends Element> bugs = roundEnvironment.getElementsAnnotatedWith(Bug.class);
            try {
                logger.info(">>> Found Bugs, start... <<<");
                parseInfo(bugs);
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
//            TypeMirror tm = element.asType();
            Bug bug = element.getAnnotation(Bug.class);
            State state = bug.state();
            String desc = bug.desc();

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

            issues.add(new Issue(state, desc, name, element.getKind()));
        }
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


    /**
     * generate BugReport.txt
     */
    private void generateReport() {
        logger.info("generate bug report");
        String fileName = Utils.genFileName(module+"Bug");
        if (FileUtils.createFile(fileName)) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("auto generated, do not change !!!! \n\n");

            for (Issue issue : issues) {
                stringBuilder.append(issue.toString());
                stringBuilder.append("\n");
            }
            FileUtils.writeStringToFile(fileName, stringBuilder.toString(), false);
            logger.info("generate success");
        } else {
            logger.info("generate failure");
        }
    }

}

