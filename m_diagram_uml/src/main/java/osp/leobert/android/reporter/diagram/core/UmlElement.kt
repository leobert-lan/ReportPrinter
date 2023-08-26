package osp.leobert.android.reporter.diagram.core

import osp.leobert.android.reporter.diagram.PaddingStartStringBuilder
import osp.leobert.android.reporter.diagram.graph.DAG
import osp.leobert.android.reporter.diagram.Utils.fetchDeclaredType
import osp.leobert.android.reporter.diagram.Utils.ifElement
import osp.leobert.android.reporter.diagram.Utils.ifTypeElement
import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import java.lang.Appendable
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.ElementFilter

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> UmlElement </p>
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

    /**
     * generate element info in uml
     * */
    abstract fun umlElement(context: DrawerContext,padding:Int = 0): String

    abstract fun parseFieldAndMethod(diagram: ClassDiagram, graph: DAG<UmlElement>, context: DrawerContext)

    /**
     * 绘制field
     * */
    abstract fun drawField(fieldDrawer: FieldDrawer, builder: Appendable, context: DrawerContext)

    /**
     * 绘制method
     * */
    abstract fun drawMethod(methodDrawer: MethodDrawer, builder: Appendable, context: DrawerContext)

    protected fun handleDependencyViaField(
        variableElement: VariableElement,
        diagram: ClassDiagram,
        graph: DAG<UmlElement>,
        context: DrawerContext
    ) {
        val vType = variableElement.asType()
        if (vType.kind.isPrimitive) {
            return
        }
        //todo the diagram is passed by the holder-class, but the variableElement may have a ClassDiagram or annotation notated by ClassDiagram

        val refers = variableElement.asType().fetchDeclaredType()

        if (refers.isNotEmpty()) {
            refers.forEach { refer ->
                refer.ifElement()?.let { ele ->
                    IUmlElementHandler.HandlerImpl.handle(
                        from = this, relation = Relation.Dependency,
                        element = ele, diagram = diagram, graph = graph, context = context
                    )
                }
            }
            return
        }

        val e = variableElement.asType().ifElement()

        //handle normal
        if (e != null) {
            IUmlElementHandler.HandlerImpl.handle(
                from = this,
                relation = Relation.Dependency,
                element = e,
                diagram = diagram,
                graph = graph,
                context = context
            )
            return
        }
    }

}

class UmlStub private constructor() : UmlElement(null, null) {

    companion object {
        val sInstance = UmlStub()
    }

    override fun umlElement(context: DrawerContext, padding: Int): String {
        return ""
    }

    override fun parseFieldAndMethod(diagram: ClassDiagram, graph: DAG<UmlElement>, context: DrawerContext) {
    }

    override fun drawField(fieldDrawer: FieldDrawer, builder: Appendable, context: DrawerContext) {
    }

    override fun drawMethod(methodDrawer: MethodDrawer, builder: Appendable, context: DrawerContext) {
    }

}

class UmlClass(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element) {

    companion object {
        private val drawer = ElementDrawer(NameDrawer.ClzNameDrawer).apply {
            this.modifierDrawer.remove(AbstractTypeDrawer)
            this.modifierDrawer.add(AbstractTypeDrawer)
        }
    }

    private val mFields: MutableSet<VariableElement> = LinkedHashSet()
    private val mMethods: MutableSet<ExecutableElement> = LinkedHashSet()

    override fun umlElement(context: DrawerContext, padding: Int): String {
        val builder = PaddingStartStringBuilder(padding,StringBuilder())
        drawer.drawAspect(builder, this, context)
        return builder.toString()
    }

    override fun parseFieldAndMethod(diagram: ClassDiagram, graph: DAG<UmlElement>, context: DrawerContext) {
        val tElement = element.ifTypeElement() ?: return

        val fieldVisible = diagram.fieldVisible
        val fields = ElementFilter.fieldsIn(tElement.enclosedElements)
        mFields.clear()
        if (fieldVisible.isNotEmpty()) {
            mFields.addAll(
            fields.filter { e ->
                fieldVisible.find { it.match(e) } != null
            }
            )
        }

        fields.forEach {
            handleDependencyViaField(it, diagram, graph, context)
        }

        val methodVisible = diagram.methodVisible
        val methods = ElementFilter.methodsIn(tElement.enclosedElements)

        mMethods.clear()
        if (methodVisible.isNotEmpty()) {
            mMethods.addAll(
            methods.filter { e ->
                methodVisible.find { it.match(e) } != null
            }
            )
        }
    }

    override fun drawField(fieldDrawer: FieldDrawer, builder: Appendable, context: DrawerContext) {
        mFields.let {
            if (it.isNotEmpty())
                builder.append("  .. fields ..").append(RETURN)
            it.forEach { field ->
                fieldDrawer.invokeDraw(builder, field, context)
            }
        }
    }

    override fun drawMethod(methodDrawer: MethodDrawer, builder: Appendable, context: DrawerContext) {
        mMethods.let {
            if (it.isNotEmpty())
                builder.append("  .. methods ..").append(RETURN)
            it.forEach { method ->
                methodDrawer.invokeDraw(builder, method, context)
            }
        }
    }

}

class UmlEnum(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element) {
    companion object {
        private val drawer = ElementDrawer(NameDrawer.EnumNameDrawer)
    }

    private val mFields: MutableSet<VariableElement> = LinkedHashSet()
    private val mMethods: MutableSet<ExecutableElement> = LinkedHashSet()

    override fun umlElement(context: DrawerContext, padding: Int): String {
        val builder = PaddingStartStringBuilder(padding,StringBuilder())
        drawer.drawAspect(builder, this, context)
        return builder.toString()
    }

    override fun parseFieldAndMethod(diagram: ClassDiagram, graph: DAG<UmlElement>, context: DrawerContext) {
        val tElement = element.ifTypeElement() ?: return

        val fieldVisible = diagram.fieldVisible
        val fields = ElementFilter.fieldsIn(tElement.enclosedElements)
        mFields.clear()
        if (fieldVisible.isNotEmpty()) {
            mFields.addAll(
                fields.filter { e ->
                    fieldVisible.find { it.match(e) } != null
                }
            )
        }

        fields.forEach {
            if (it.asType() != element?.asType())
                handleDependencyViaField(it, diagram, graph, context)
        }

        val methods = ElementFilter.methodsIn(tElement.enclosedElements)
        mMethods.clear()
        val methodVisible = diagram.methodVisible
        if (methodVisible.isNotEmpty()) {
            mMethods.addAll(
                methods.filter { e ->
                    methodVisible.find { it.match(e) } != null
                }
            )
        }
    }

    override fun drawField(fieldDrawer: FieldDrawer, builder: Appendable, context: DrawerContext) {
        mFields.filter {
            it.asType() == element?.asType()
        }.let {
            builder.append("  .. enums ..").append(RETURN)
            it.forEach { field ->
                builder.append("  ").append(field.toString()).append(RETURN)
            }
        }

        mFields.filter {
            it.asType() != element?.asType()
        }.let {
            builder.append("  .. fields ..").append(RETURN)
            it.forEach { field ->
                fieldDrawer.invokeDraw(builder, field, context)
            }
        }
    }

    override fun drawMethod(methodDrawer: MethodDrawer, builder: Appendable, context: DrawerContext) {
        mMethods.let {
            if (it.isNotEmpty())
                builder.append("  .. methods ..").append(RETURN)
            it.forEach { method ->
                methodDrawer.invokeDraw(builder, method, context)
            }
        }
    }
}

class UmlInterface(diagram: ClassDiagram, element: Element) : UmlElement(diagram, element) {
    companion object {
        private val drawer = ElementDrawer(NameDrawer.InterfaceNameDrawer)
    }

    private val mFields: MutableSet<VariableElement> = LinkedHashSet()
    private val mMethods: MutableSet<ExecutableElement> = LinkedHashSet()

    override fun umlElement(context: DrawerContext, padding: Int): String {
        val builder = PaddingStartStringBuilder(padding,StringBuilder())
        drawer.drawAspect(builder, this, context)
        return builder.toString()
    }

    override fun parseFieldAndMethod(diagram: ClassDiagram, graph: DAG<UmlElement>, context: DrawerContext) {
        val tElement = element.ifTypeElement() ?: return

        val fieldVisible = diagram.fieldVisible
        val fields = ElementFilter.fieldsIn(tElement.enclosedElements)
        mFields.clear()
        if (fieldVisible.isNotEmpty()) {
            mFields.addAll(
                fields.filter { e ->
                    fieldVisible.find { it.match(e) } != null
                }
            )
        }

        fields.forEach {
            handleDependencyViaField(it, diagram, graph, context)
        }

        val methods = ElementFilter.methodsIn(tElement.enclosedElements)
        mMethods.clear()
        val methodVisible = diagram.methodVisible
        if (methodVisible.isNotEmpty()) {
            mMethods.addAll(
                methods.filter { e ->
                    methodVisible.find { it.match(e) } != null
                }
            )
        }
    }

    override fun drawField(fieldDrawer: FieldDrawer, builder: Appendable, context: DrawerContext) {
        mFields.let {
            if (it.isNotEmpty())
                builder.append("  .. fields ..").append(RETURN)
            it.forEach { field ->
                fieldDrawer.invokeDraw(builder, field, context)
            }
        }
    }

    override fun drawMethod(methodDrawer: MethodDrawer, builder: Appendable, context: DrawerContext) {
        mMethods.let {
            if (it.isNotEmpty())
                builder.append("  .. methods ..").append(RETURN)
            it.forEach { method ->
                methodDrawer.invokeDraw(builder, method, context)
            }
        }
    }

}


