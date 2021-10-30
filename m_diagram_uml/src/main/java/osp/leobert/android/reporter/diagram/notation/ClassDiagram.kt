package osp.leobert.android.reporter.diagram.notation

/**
 * Created by leobert on 2021/9/18.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ClassDiagram(
    val qualifier: String = "",
    val fieldVisible: Array<Visible> = [Visible.Private, Visible.Protected, Visible.Package, Visible.Public],
    val methodVisible: Array<Visible> = [Visible.Private, Visible.Protected, Visible.Package, Visible.Public],
)
