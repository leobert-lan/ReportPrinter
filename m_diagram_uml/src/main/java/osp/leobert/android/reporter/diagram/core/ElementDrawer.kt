package osp.leobert.android.reporter.diagram.core

import com.sun.tools.javac.code.Type
import osp.leobert.android.reporter.diagram.Utils.nameRemovedPkg
import osp.leobert.android.reporter.diagram.Utils.takeIfInstance
import osp.leobert.android.reporter.diagram.notation.Visible
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

const val RETURN = "\r\n"

// TODO: #5 consider if not using jdk1.8, how to deal with com.sun.tools.javac.code.Type??
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

enum class ModifierDrawer(private val matcher: IModifierMatcher, private val mark: String) : IJavaxElementDrawer {
    Private(Visible.Private, "-"),
    Protected(Visible.Protected, "#"),
    Public(Visible.Public, "+"),
    Package(Visible.Package, "~"),

    Static(IModifierMatcher.Companion.Static, "{static}"),
    Abstract(IModifierMatcher.Companion.Abstract, "{abstract}");

    override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        if (matcher.match(element))
            builder.append(mark)
    }
}

object FieldNameDrawer : IJavaxElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        builder.append(element.toString())
    }
}

object FieldTypeDrawer : IJavaxElementDrawer {
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
            object : IJavaxElementDrawer {
                override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
                    builder.append("  {field}")
                }
            },
            //modifiers
            ModifierDrawer.Static, ModifierDrawer.Abstract,
            ModifierDrawer.Public, ModifierDrawer.Protected, ModifierDrawer.Package, ModifierDrawer.Private,
            FieldNameDrawer, FieldTypeDrawer
    )

    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
        element.drawField(this, builder, context)
    }

    fun invokeDraw(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        drawer.forEach {
            it.drawAspect(builder, element, context)
        }
        builder.append(RETURN)
    }
}

object MethodSignatureDrawer : IJavaxElementDrawer {
    override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        val tmp = element.asType().takeIfInstance<Type.MethodType>()
        builder.append(" ").append(element.simpleName)
        if (tmp == null) {
            builder.append(element.asType().toString())
        } else {
            var info = "(${tmp.argtypes.joinToString { it.asElement().nameRemovedPkg(it.toString()) }}): ${tmp.restype.run { asElement().nameRemovedPkg(toString()) }}"

            context.forEach {
                val original = it.element?.asType()?.toString()
                if (!original.isNullOrBlank()) {
                    info = info.replace(original, it.name)
                }
            }

            builder.append(info)
        }
    }
}

object MethodDrawer : IElementDrawer {
    private val drawer: List<IJavaxElementDrawer> = arrayListOf(
            object : IJavaxElementDrawer {
                override fun drawAspect(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
                    builder.append("  {method}")
                }
            },
            //modifiers
            ModifierDrawer.Static, ModifierDrawer.Abstract,
            ModifierDrawer.Public, ModifierDrawer.Protected, ModifierDrawer.Package, ModifierDrawer.Private,
            MethodSignatureDrawer
    )

    override fun drawAspect(builder: StringBuilder, element: UmlElement, context: MutableSet<UmlElement>) {
        element.drawMethod(this, builder, context)
    }

    fun invokeDraw(builder: StringBuilder, element: Element, context: MutableSet<UmlElement>) {
        drawer.forEach {
            it.drawAspect(builder, element, context)
        }
        builder.append(RETURN)
    }
}