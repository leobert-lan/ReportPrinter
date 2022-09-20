package osp.leobert.android.reporter.diagram.core

import osp.leobert.android.reporter.diagram.notation.IgnoreExclude
import java.util.regex.Pattern
import javax.lang.model.element.TypeElement

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> ClzIgnorance </p>
 * Created by leobert on 2022/2/17.
 */
class ClzIgnorance constructor(ignores: Array<String>, ignoreExclude: IgnoreExclude? = null) {
    private val ignorePatterns = ignores.map {
        Pattern.compile(it)
    }

    private val ignoreExactExclusion = ignoreExclude?.exactFullNames?.toHashSet() ?: hashSetOf()

    private val ignorePatternExclusion = ignoreExclude?.fullNamePatterns?.map {
        Pattern.compile(it)
    }?.toHashSet() ?: hashSetOf()


    private val ignoranceCache = mutableMapOf<String, Boolean>()
    private val excludeCache = mutableMapOf<String, Boolean>()

    fun isIgnoreExclude(element: TypeElement): Boolean {
        val clzName = element.qualifiedName.toString()
        return excludeCache.getOrPut(clzName) {
            (ignoreExactExclusion.contains(clzName)) or (ignorePatternExclusion.find {
                it.matcher(clzName).find()
            } != null)
        }
    }

    fun isIgnored(element: TypeElement): Boolean {
        if (isIgnoreExclude(element)) return false

        val clzName = element.qualifiedName.toString()
        return ignoranceCache.getOrPut(clzName) {
            ignorePatterns.find {
                it.matcher(clzName).find()
            } != null
        }
    }
}