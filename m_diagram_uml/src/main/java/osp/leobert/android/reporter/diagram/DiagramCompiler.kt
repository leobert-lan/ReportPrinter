package osp.leobert.android.reporter.diagram

import androidx.annotation.RequiresApi
import com.google.auto.service.AutoService
import osp.leobert.android.maat.dag.DAG
import osp.leobert.android.reporter.diagram.Utils.takeIfInstance
import osp.leobert.android.reporter.diagram.core.IUmlElementHandler
import osp.leobert.android.reporter.diagram.core.Relation
import osp.leobert.android.reporter.diagram.core.UmlElement
import osp.leobert.android.reporter.diagram.core.UmlStub
import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reportprinter.spi.Model
import osp.leobert.android.reportprinter.spi.ReporterExtension
import osp.leobert.android.reportprinter.spi.Result
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(ReporterExtension::class)
/*skip lint warning!*/
@RequiresApi(24)
class DiagramCompiler : ReporterExtension {

    private val groups = mutableMapOf<String, MutableList<Pair<ClassDiagram, Model>>>()
    private val diagramGraphByGroup = mutableMapOf<String, DAG<UmlElement>>()

    override fun applicableAnnotations(): MutableSet<String> {
        return mutableSetOf(ClassDiagram::class.java.name)
    }

    override fun generateReport(previousData: MutableMap<String, MutableList<Model>>?): Result {

        val notatedByDiagramNotations = filterNotation(previousData)

        val candidates = previousData?.get(ClassDiagram::class.java.name)?.filter {
            it.elementKind == ElementKind.INTERFACE || it.elementKind == ElementKind.CLASS || it.elementKind == ElementKind.ENUM
        }

        if (candidates.isNullOrEmpty()) return Result.newBuilder().handled(false).build()

        groups.clear()
        diagramGraphByGroup.clear()

        val sb = StringBuilder()

        notatedByDiagramNotations.forEach {
            handleGroup(it, candidates)
        }

        groups.forEach { (t: String, u: MutableList<Pair<ClassDiagram, Model>>) ->

            val graph = diagramGraphByGroup.getOrDefault(
                t, DAG(
                    nameOf = { it.name }, printChunkMax = 10
                )
            )

            diagramGraphByGroup[t] = graph

            u.forEach {
                handleUmlElement(it.second, it.first, graph)
            }

            sb.append(t).append(" --> \r\n")
                .append(
                    graph.debugMatrix()
                ).append("\r\n")


            sb.append(t).append(" --> ")
                .append("[")
                .append(
                    u.map {
                        it.second.element.takeIfInstance<TypeElement>()?.qualifiedName?.toString()
                    }.joinToString(",\r\n")
                ).append("]\r\n")
        }

        return Result.newBuilder().handled(true)
            .reportFileNamePrefix("Diagram")
            .reportContent(sb.toString())
            .fileExt("puml")
            .build()
    }

    private fun handleUmlElement(model: Model, diagram: ClassDiagram, graph: DAG<UmlElement>) {
        IUmlElementHandler.HandlerImpl.handle(
            from = UmlStub(diagram),
            relation = Relation.of(0),
            element = model.element,
            diagram = diagram,
            graph = graph
        )

    }

    private fun filterNotation(previousData: MutableMap<String, MutableList<Model>>?): List<Model> {
        return previousData?.get(ClassDiagram::class.java.name)?.filter {
            it.elementKind == ElementKind.ANNOTATION_TYPE
        } ?: emptyList()
    }

    private fun handleGroup(
        qualifier: Model,
        candidates: List<Model>
    ) {

        candidates.forEach {
            val diagram = if (it.element.hasAnnotationOf(qualifier.element)) {
                findAnnotationByAnnotation(it.element.annotationMirrors, ClassDiagram::class.java)
            } else {
                it.element.getAnnotation(ClassDiagram::class.java)
            } ?: return@forEach

            groups.getOrDefault(diagram.qualifier, arrayListOf()).apply {
                this.add(diagram to it)
                groups[diagram.qualifier] = this
            }
        }
    }

    private fun Element.hasAnnotationOf(annoElement: Element): Boolean {
        return this.annotationMirrors?.find {
            it.annotationType.asElement() == annoElement
        } != null
    }

    private inline fun <reified T : Annotation?> findAnnotationByAnnotation(
        annotations: Collection<AnnotationMirror>,
        clazz: Class<T>
    ): T? {
        if (annotations.isEmpty()) return null // Save an iterator in the common case.
        for (mirror in annotations) {
            val target: Annotation? = mirror.annotationType
                .asElement()
                .getAnnotation(clazz)
            if (target != null) {
                return target.takeIfInstance<T>()
            }
        }
        return null
    }
}