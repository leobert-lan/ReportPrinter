package osp.leobert.android.reportprinter.spi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;

/**
 * <p><b>Package:</b> osp.leobert.android.reportprinter.spi </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ReporterExtension </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/11/17.
 */
public interface ReporterExtension {
    Set<String> applicableAnnotations();

    Result generateReport(Map<String,List<Model>> previousData);
}
