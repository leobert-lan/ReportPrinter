package osp.leobert.android.reporter.demoext;

import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;

import osp.leobert.android.reportprinter.spi.IModuleInitializer;
import osp.leobert.android.reportprinter.spi.Model;
import osp.leobert.android.reportprinter.spi.ReporterExtension;
import osp.leobert.android.reportprinter.spi.Result;

/**
 * 演示：自行扩展 ReporterExtension， 对自定义的注解规则解析并生成文档
 * Created by leobert on 2018/11/17.
 */
@AutoService(ReporterExtension.class)
public class DemoReporter implements ReporterExtension, IModuleInitializer {
    ProcessingEnvironment env;

    @Override
    public Set<String> applicableAnnotations() {
        return Collections.singleton(Demo.class.getName());
    }

    @Override
    public Result generateReport(Map<String, List<Model>> previousData) {
        if (previousData == null)
            return null;

        List<Model> demoModels = previousData.get(Demo.class.getName());
        if (demoModels == null || demoModels.isEmpty())
            return Result.newBuilder().handled(false).build();
        StringBuilder stringBuilder = new StringBuilder();

        for (Model model : demoModels) {
            Demo demo = model.getElement().getAnnotation(Demo.class);
            String desc = demo.desc();
            String doc = env.getElementUtils().getDocComment(model.getElement());

            stringBuilder.append(model.getElementKind().toString())
                    .append(" :")
                    .append(model.getName())
                    .append("\r\n")
                    .append("java-doc:\r\n")
                    .append(doc)
                    .append("\r\n")
                    .append("desc:")
                    .append(desc)
                    .append("\r\n")
                    .append("\r\n");
        }

        return Result.newBuilder()
                .handled(true)
                .reportFileNamePrefix("Demo")
                .fileExt("md")
                .reportContent(stringBuilder.toString())
                .build();
    }

    @Override
    public void initialize(ProcessingEnvironment env) {
        this.env = env;
    }
}
