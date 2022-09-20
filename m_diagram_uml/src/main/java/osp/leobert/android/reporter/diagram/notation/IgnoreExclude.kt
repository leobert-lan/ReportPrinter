package osp.leobert.android.reporter.diagram.notation

/**
 * Classname: IgnoreExclude </p>
 * Description: the exclusion of ignorance in Diagram </p>
 * Created by leobert on 2022/9/20.
 *
 * [exactFullNames] array of exactly full name of the class to be exclude. e.g. "java.lang.Object" for [Object]
 * [fullNamePatterns] array of stringify patterns of the class's full name to be exclude
 */
annotation class IgnoreExclude
constructor(
    val exactFullNames: Array<String> = [],
    val fullNamePatterns: Array<String> = [],
)
