package osp.leobert.android.reporter.diagram.notation

/**
 * Created by leobert on 2021/9/18.
 */
//        AnnotationTarget.CLASS,
//        AnnotationTarget.TYPE
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ClassDiagram(
        val qualifier: String = "",

        val fieldVisible: Array<Visible> = [Visible.Private, Visible.Protected, Visible.Package, Visible.Public],

        val methodVisible: Array<Visible> = [Visible.Private, Visible.Protected, Visible.Package, Visible.Public],

        )

// TODO: 2021/9/23 增加一个注解：config，仅可注解于注解，迁移原ClassDiagram ，增加一个 GenerateClassDiagram 的注解，用于标记需要生成关系树的关键节点
//处理时，关心 "config" 和 "GenerateClassDiagram"
//通过 config 得到自定义的注解 CA，理论上，他们需要Qualifier相互独立
//通过 GenerateClassDiagram 得到树的关键点，寻找CA' item 开始扩散处理，
//
// 这样可以保证配置 是针对树的而不是针对node