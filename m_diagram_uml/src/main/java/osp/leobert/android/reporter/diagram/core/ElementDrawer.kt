package osp.leobert.android.reporter.diagram.core

import osp.leobert.android.reporter.diagram.Utils.takeIfInstance
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

const val RETURN = "\r\n"

interface IElementDrawer {
    fun drawAspect(builder: StringBuilder, element: UmlElement)
}

class ElementDrawer(private val nameDrawer: NameDrawer) : IElementDrawer {

    val modifierDrawer: MutableList<IElementDrawer> = mutableListOf()

    var fieldDrawer: FieldDrawer? = FieldDrawer
    var methodDrawer: MethodDrawer? = MethodDrawer


    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
        modifierDrawer.forEach {
            it.drawAspect(builder, element)
        }

        nameDrawer.drawAspect(builder, element)
        builder.append("{").append(RETURN)
        fieldDrawer?.drawAspect(builder, element)
        methodDrawer?.drawAspect(builder, element)
        builder.append("}").append(RETURN)
    }
}

object AbstractTypeDrawer : IElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
        val isAbsType = element.element?.modifiers?.contains(Modifier.ABSTRACT)
        if (isAbsType == true) {
            builder.append("abstract ")
        }
    }

}

enum class NameDrawer(val type: String) : IElementDrawer {
    ClzNameDrawer("class "), EnumNameDrawer("enum "), InterfaceNameDrawer("interface ");

    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
        builder.append(type).append(element.name)
    }
}

interface IJavaxElementDrawer {
    fun drawAspect(builder: StringBuilder, element: Element)
}

enum class ModifierDrawer(private val modifier: Modifier, private val mark: String) : IJavaxElementDrawer {
    Private(Modifier.PRIVATE, "-"),
    Protected(Modifier.PROTECTED, "#"),
    Public(Modifier.PUBLIC, "+"),
    Package(Modifier.DEFAULT, "~"),

    Static(Modifier.STATIC, "{static}"),
    Abstract(Modifier.ABSTRACT, "{abstract}");

    override fun drawAspect(builder: StringBuilder, element: Element) {
        if (element.modifiers?.contains(modifier) == true)
            builder.append(mark)
    }
}

object FieldNameDrawer : IJavaxElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: Element) {
        builder.append(element.toString())
    }
}

object FieldTypeDrawer : IJavaxElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: Element) {
        val name =  element.asType().toString()
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

    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
        builder.append("'Test:FieldDrawer").append(RETURN)
        element.drawField(this, builder)
    }

    fun invokeDraw(builder: StringBuilder, element: Element) {
        drawer.forEach {
            it.drawAspect(builder, element)
        }
        builder.append(RETURN)
    }

}

object MethodDrawer : IElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
    }
}