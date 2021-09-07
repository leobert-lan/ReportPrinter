package osp.leobert.android.kotlin_sample

import osp.leobert.android.reporter.demoext.Demo
import osp.leobert.android.reporter.review.TODO

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> KtFoo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020/6/3.
 */
@Demo(foo = "foo of demo notated at clz")
@TODO(desc = "change name", date = "2021-9-7")
class KtFoo {
    @Demo(foo = "foo of demo notated at function")
    @TODO(desc = "change method name", date = "2021-9-7")
    private fun foo(bar: Any) {
//        @TODO(desc = "change name", date = "2021-9-7") 因为expression部分无法生成注解，所以直接移除了
        print("aaa")
    }

    @Demo(foo = "foo of demo notated at field")
    @TODO(desc = "change param name", date = "2021-9-8")
    private val i = 0
}