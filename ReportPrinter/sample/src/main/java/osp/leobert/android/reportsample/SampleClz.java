package osp.leobert.android.reportsample;

import osp.leobert.android.reportprinter.notation.ChangeLog;

/**
 * <p><b>Package:</b> osp.leobert.android.reportsample </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> SampleClz </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/8.
 */
@ChangeLog(version = "1.0.0",
        changes = {
                "1",
                "2"})
public class SampleClz {
    @ChangeLog(version = "1.0.0",
            changes = {
                    "f1",
                    "f2"
            })
    private void foo(Object bar) {

    }
}
