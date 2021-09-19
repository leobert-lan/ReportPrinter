package osp.leobert.android.reporter.diagram.notation

import java.lang.annotation.Inherited

/**
 * Created by leobert on 2021/9/18.
 */
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE
)
@Inherited
annotation class ClassDiagram(
    val qualifier: String = ""
)
