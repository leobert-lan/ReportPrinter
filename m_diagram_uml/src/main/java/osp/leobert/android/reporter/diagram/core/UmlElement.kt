package osp.leobert.android.reporter.diagram.core

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import javax.lang.model.element.Element

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> UmlElement </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */
abstract class UmlElement(val diagram: ClassDiagram?, val element: Element?) {
    val name: String = element?.simpleName?.toString() ?: "Stub"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UmlElement

        if (diagram != other.diagram) return false
        if (element != other.element) return false

        return true
    }

    override fun hashCode(): Int {
        var result = diagram.hashCode()
        result = 31 * result + (element?.hashCode() ?: 0)
        return result
    }

    abstract fun umlElement(): String
}

//inline operator fun UmlElement.getValue(thisObj: Any?, property: KProperty<*>): String = this.element


class UmlStub private constructor() : UmlElement(null, null) {

    companion object {
        val sInstance = UmlStub()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun umlElement(): String {
        return ""
    }

}

class UmlClass(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element) {

    companion object {
        private val drawer = ElementDrawer(NameDrawer.ClzNameDrawer).apply {
            this.modifierDrawer.remove(AbstractTypeDrawer)
            this.modifierDrawer.add(AbstractTypeDrawer)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun umlElement(): String {
        val builder = StringBuilder()
        drawer.drawAspect(builder, this)
        return builder.toString()
    }

}

class UmlEnum(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element) {
    companion object {
        private val drawer = ElementDrawer(NameDrawer.EnumNameDrawer)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun umlElement(): String {
        val builder = StringBuilder()
        drawer.drawAspect(builder, this)
        return builder.toString()
    }

}

class UmlInterface(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element) {
    companion object {
        private val drawer = ElementDrawer(NameDrawer.InterfaceNameDrawer)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun umlElement(): String {
        val builder = StringBuilder()
        drawer.drawAspect(builder, this)
        return builder.toString()
    }

}


