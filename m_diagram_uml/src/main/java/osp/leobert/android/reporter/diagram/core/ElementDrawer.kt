package osp.leobert.android.reporter.diagram.core

import javax.lang.model.element.Modifier

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

enum class ModifierDrawer : IElementDrawer {

}

object FieldDrawer : IElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
    }

}

object MethodDrawer : IElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: UmlElement) {
    }
}