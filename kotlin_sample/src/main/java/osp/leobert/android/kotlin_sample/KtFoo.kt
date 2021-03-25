package osp.leobert.android.kotlin_sample

import osp.leobert.android.reporter.demoext.Demo

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> KtFoo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020/6/3.
 */
@Demo(foo = "foo of demo notated at clz")
class KtFoo {
    @Demo(foo = "foo of demo notated at function")
    private fun foo(bar: Any) {
    }

    @Demo(foo = "foo of demo notated at field")
    private val i = 0
}