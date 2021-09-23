package osp.leobert.android.reporter.demoext;

import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import osp.leobert.android.reportprinter.spi.Model;
import osp.leobert.android.reportprinter.spi.ReporterExtension;
import osp.leobert.android.reportprinter.spi.Result;

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.demoext </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> FooReporter </p>
 * Created by leobert on 2018/11/17.
 */
@AutoService(ReporterExtension.class)
public class FooReporter implements ReporterExtension {
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
            String foo = demo.foo();
            stringBuilder.append(model.getElementKind().toString())
                    .append(" :")
                    .append(model.getName())
                    .append("\r\n")
                    .append("foo:")
                    .append(foo)
                    .append("\r\n");
        }

        return Result.newBuilder()
                .handled(true)
                .reportFileNamePrefix("Demo")
                .fileExt("md")
                .reportContent(stringBuilder.toString())
                .build();
    }
}
