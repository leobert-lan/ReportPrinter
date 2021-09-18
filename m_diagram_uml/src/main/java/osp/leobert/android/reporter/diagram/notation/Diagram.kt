package osp.leobert.android.reporter.diagram.notation

import java.lang.annotation.Inherited

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.notation </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Diagram </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE
)
@Inherited
annotation class Diagram(
    val qualifier: String = ""
)
