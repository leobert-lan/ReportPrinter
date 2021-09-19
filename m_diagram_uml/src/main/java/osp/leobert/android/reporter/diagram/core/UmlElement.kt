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
abstract class UmlElement(val diagram: ClassDiagram, val element: Element?) {
    val name: String = element?.simpleName?.toString() ?: "Stub"

}

//inline operator fun UmlElement.getValue(thisObj: Any?, property: KProperty<*>): String = this.element


class UmlStub(diagram: ClassDiagram) : UmlElement(diagram, null)

class UmlClass(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element)

class UmlEnum(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element)

class UmlInterface(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element)


