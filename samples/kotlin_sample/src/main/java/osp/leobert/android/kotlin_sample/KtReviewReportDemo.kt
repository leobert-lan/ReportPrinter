package osp.leobert.android.kotlin_sample

import osp.leobert.android.reporter.demoext.Demo
import osp.leobert.android.reporter.review.Bug
import osp.leobert.android.reporter.review.Bugs
import osp.leobert.android.reporter.review.Done
import osp.leobert.android.reporter.review.TODO

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> KtFoo </p>
 * Created by leobert on 2020/6/3.
 */
@TODO(desc = "change name", date = "2021-9-7")
@Done(desc = "rename", date = "2021-9-8")
@Bug(desc = "bug-AAA", module = "AAA", track = "https://github.com/leobert-lan/ReportPrinter")
@Bugs(
    [
        Bug(desc = "bug-A1", module = "AAA", track = "https://github.com/leobert-lan/ReportPrinter"),
        Bug(desc = "bug-A2", module = "AAA", track = "https://github.com/leobert-lan/ReportPrinter")
    ]
)
class KtReviewReportDemo {

    @TODO(desc = "change method name", date = "2021-9-7")
    @Done(desc = "rename", date = "2021-9-8")
    @Bug(desc = "AAA3", module = "AAA", track = "https://github.com/leobert-lan/ReportPrinter")
    /**
     * method foo 的 Java doc
     * */
    private fun foo(bar: Any) {
//        @TODO(desc = "change name", date = "2021-9-7") 因为expression部分无法生成注解，所以直接移除了
        print("aaa")
    }

    @TODO(desc = "change param name", date = "2021-9-8")
    @Done(desc = "完成了重命名", date = "2021-9-8")
    /**
     * i 的java doc
     * */
    private val i = 0
}