package osp.leobert.android.reporter.diagram.notation

/**
 * Classname: NameSpace </p>
 * Description: notate an annotation, which mark a namespace in uml </p>
 *
 * [name]: the name of the namespace, caution that: don't conflict with any element's name in the uml
 * [color]: the background color of the namespace graph area, RGBA
 *
 * Created by Leobert on 2023/8/18.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class NameSpace(
    val name: String,
    val color: Long = 0xFFFFFFFF
)
