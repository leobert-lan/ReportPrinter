package osp.leobert.android.reportsample;

import osp.leobert.android.reporter.demoext.Demo;

/**
 * <p><b>Package:</b> osp.leobert.android.reportsample </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> SampleClz </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/7/8.
 */
@Demo(foo = "foo of demo notated at clz")
public class SampleClz {
    @Demo(foo = "foo of demo notated at function")
    private void foo(Object bar) {

    }

    @Demo(foo = "foo of demo notated at field")
    private int i;
}
