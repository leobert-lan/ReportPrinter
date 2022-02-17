package osp.leobert.android.reporter.diagram.core

import java.util.regex.Pattern
import javax.lang.model.element.TypeElement

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ClzIgnorance </p>
 * Created by leobert on 2022/2/17.
 */
class ClzIgnorance(ignores: Array<String>) {
    private val ignorePatterns = ignores.map {
        Pattern.compile(it)
    }

    private val cache = mutableMapOf<String,Boolean>()

    fun isIgnored(element: TypeElement): Boolean {
        val clzName = element.qualifiedName.toString()
        return cache.getOrPut(clzName) {
            ignorePatterns.find {
                it.matcher(clzName).find()
            } != null
        }
    }
}