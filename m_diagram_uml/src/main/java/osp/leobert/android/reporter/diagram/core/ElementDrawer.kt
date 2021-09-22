package osp.leobert.android.reporter.diagram.core

import osp.leobert.android.reporter.diagram.Utils.nameRemovedPkg
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

const val RETURN = "\r\n"

interface IElementDrawer {
    fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>)
}

class ElementDrawer(private val nameDrawer: NameDrawer) : IElementDrawer {

    val modifierDrawer: MutableList<IElementDrawer> = mutableListOf()

    var fieldDrawer: FieldDrawer? = FieldDrawer
    var methodDrawer: MethodDrawer? = MethodDrawer


    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
        modifierDrawer.forEach {
            it.drawAspect(builder, element, context)
        }

        nameDrawer.drawAspect(builder, element, context)
        builder.append("{").append(RETURN)
        fieldDrawer?.drawAspect(builder, element, context)
        methodDrawer?.drawAspect(builder, element, context)
        builder.append("}").append(RETURN)
    }
}

object AbstractTypeDrawer : IElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
        val isAbsType = element.element?.modifiers?.contains(Modifier.ABSTRACT)
        if (isAbsType == true) {
            builder.append("abstract ")
        }
    }

}

enum class NameDrawer(val type: String) : IElementDrawer {
    ClzNameDrawer("class "), EnumNameDrawer("enum "), InterfaceNameDrawer("interface ");

    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
        builder.append(type).append("\"").append(element.element.nameRemovedPkg(element.name)).append("\"")
    }
}

interface IJavaxElementDrawer {
    fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>)
}

enum class ModifierDrawer(private val modifier: Modifier, private val mark: String) : IJavaxElementDrawer {
    Private(Modifier.PRIVATE, "-"),
    Protected(Modifier.PROTECTED, "#"),
    Public(Modifier.PUBLIC, "+"),
    Package(Modifier.DEFAULT, "~"),

    Static(Modifier.STATIC, "{static}"),
    Abstract(Modifier.ABSTRACT, "{abstract}");

    override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        if (element.modifiers?.contains(modifier) == true)
            builder.append(mark)
    }
}

object FieldNameDrawer : IJavaxElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        builder.append(element.toString())
    }
}

object FieldTypeDrawer : IJavaxElementDrawer {
    //todo 目前全类名太长了，可以适当考虑简化，因为类的依赖已经有了，所以可以从中找出替换，但是需要补充参数！
    override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {

        var name = element.nameRemovedPkg(element.asType().toString())

        context.forEach {
            val original = it.element?.asType()?.toString()
            if (!original.isNullOrBlank()) {
                name = name.replace(original, it.name)
            }
        }

        builder.append(" : ").append(name)
    }
}


object FieldDrawer : IElementDrawer {
    private val drawer: List<IJavaxElementDrawer> = arrayListOf(
            //modifiers
            ModifierDrawer.Static, ModifierDrawer.Abstract,
            ModifierDrawer.Public, ModifierDrawer.Protected, ModifierDrawer.Package, ModifierDrawer.Private,
            FieldNameDrawer, FieldTypeDrawer
    )

    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
        builder.append("'Test:FieldDrawer").append(RETURN)
        element.drawField(this, builder, context)
    }

    fun invokeDraw(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        drawer.forEach {
            it.drawAspect(builder, element, context)
        }
        builder.append(RETURN)
    }

}

object MethodDrawer : IElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
    }
}