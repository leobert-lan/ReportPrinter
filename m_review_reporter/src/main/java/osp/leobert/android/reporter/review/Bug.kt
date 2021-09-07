package osp.leobert.android.reporter.review

/**
 * Created by leobert on 2021/9/7.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPE,
//    AnnotationTarget.EXPRESSION,
//    AnnotationTarget.FILE,
//    AnnotationTarget.TYPEALIAS
)
annotation class Bug(
    val desc: String,
    val module: String,
    val track: String
)
