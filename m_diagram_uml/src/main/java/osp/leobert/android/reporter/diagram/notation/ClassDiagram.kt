package osp.leobert.android.reporter.diagram.notation

/**
 * Created by leobert on 2021/9/18.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ClassDiagram

/**
 * [ignore]: since 1.0.1, pattern for ignorance of Class
 * */
constructor(
    val qualifier: String = "",
    val fieldVisible: Array<Visible> = [Visible.Private, Visible.Protected, Visible.Package, Visible.Public],
    val methodVisible: Array<Visible> = [Visible.Private, Visible.Protected, Visible.Package, Visible.Public],
    val ignore: Array<String> = [],
    val ignoreExclude: IgnoreExclude = IgnoreExclude()
)

// TODO: add ignoreExclude 2022-09-19 19:17:22
