package osp.leobert.android.kotlin_sample

import osp.leobert.android.reporter.demoext.Demo

/**
 * 这里的java doc 会被读取
 * Created by leobert on 2020/6/3.
 */
@Demo(desc = "这是一个演示类")
class KtDemoReportSample {

    /**
     * method foo 的 Java doc
     * */
    @Demo(desc = "这是一个方法")
    private fun foo(bar: Any) {
        print("aaa")
    }

    @field:Demo(desc = "这是一个属性")
    /**
     * i 的java doc
     * */
    private val i = 0
}