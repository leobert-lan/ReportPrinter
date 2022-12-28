package osp.leobert.android.kotlin_sample

import osp.leobert.android.utils.reporter.Util
import java.io.File

/**
 * 演示使用Util 并得到文档
 * Created by leobert on 2020/6/3.
 */
class KtUtilReportDemo {

    fun checkMD5(md5: String?, updateFile: File?): Boolean {
       return false
    }

    @Util(group = Util.UtilGroup.MD5, usage = "文件内容计算md5")
    fun calculateMD5(updateFile: File?): String? {
        return null
    }

    @Util(group = Util.UtilGroup.MD5, usage = "字符串计算md5")
    fun calculateMD5(psw: String): String? {
        return null;
    }
}