package osp.leobert.android.reporter.diagram

import androidx.annotation.RequiresApi
import com.google.auto.service.AutoService
import osp.leobert.android.maat.dag.DAG
import osp.leobert.android.reporter.diagram.Utils.forEachWindowSize2
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

@AutoService(ReporterExtension::class)
/*skip lint warning!*/
@RequiresApi(24)
class DiagramCompiler : ReporterExtension {

    val RETURN = "\r\n"

    private val groups = mutableMapOf<String, MutableList<Pair<ClassDiagram, Model>>>()
    private val diagramGraphByGroup = mutableMapOf<String, DAG<UmlElement>>()
    private val diagramUmlElementCache = mutableMapOf<String, MutableSet<UmlElement>>()

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
        diagramUmlElementCache.clear()

        val sb = StringBuilder()

        notatedByDiagramNotations.forEach {
            handleGroup(it, candidates)
        }

        //todo consider alias
        val nameGetter = { element: UmlElement ->
            element.name
        }

        groups.forEach { (qualifierName: String, u: MutableList<Pair<ClassDiagram, Model>>) ->

            val graph = diagramGraphByGroup.getOrPut(qualifierName, {
                DAG(nameOf = { it.name }, printChunkMax = 10)
            })

            val cache = diagramUmlElementCache.getOrPut(qualifierName, {
                LinkedHashSet()
            })


            u.forEach {
                handleUmlElement(it.second, it.first, graph, cache)
            }

            sb.append("@startuml").append(RETURN)
            sb.append("'").append(qualifierName).append(RETURN)

            // draw all uml-element
            cache.forEach {
                sb.append(it.umlElement()).append(RETURN)
            }

//            //todo debug info
//            sb.append("'").append(qualifierName).append(" --> ").append(RETURN)
//                    .append(
//                            graph.debugMatrix().replace("\r\n", "\r\n'")
//                    ).append(RETURN)

            graph.recursive(UmlStub.sInstance, arrayListOf())

            val relationHasDraw = mutableSetOf<Relationship>()

            graph.deepPathList.forEach { path ->

                //todo debug info
                sb.append("'<")
                        .append(
                                path.joinToString {
                                    it.name
                                }
                        )
                        .append(">").append(RETURN)

                path.forEachWindowSize2 { first: UmlElement, second: UmlElement ->

                    val relationType = graph.getWeight(first, second)
                    if (relationType > Relation.Stub.type) {
                        try {
                            Relation.of(relationType)?.let { relation ->
                                val relationShip = Relationship(first, second, relation)
                                if (relationHasDraw.contains(relationShip).not()) {
                                    relationHasDraw.add(relationShip)

                                    relation.format(
                                            first, second, nameGetter
                                    )?.let {
                                        sb.append(it).append(RETURN)
                                    }
                                }

                            }

                        } catch (ignore: Exception) {
                            sb.append(ignore.localizedMessage).append(RETURN)
                        }
                    }
                }
            }
            sb.append("@enduml").append(RETURN)
        }

        return Result.newBuilder().handled(true)
                .reportFileNamePrefix("Diagram")
                .reportContent(sb.toString())
                .fileExt("puml")
                .build()
    }

    private fun handleUmlElement(model: Model, diagram: ClassDiagram, graph: DAG<UmlElement>, cache: MutableSet<UmlElement>) {
        IUmlElementHandler.HandlerImpl.handle(
                from = UmlStub.sInstance,
                relation = Relation.Stub,
                element = model.element,
                diagram = diagram,
                graph = graph,
                cache = cache
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

    private class Relationship(val from: UmlElement, val to: UmlElement, val relation: Relation) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Relationship

            if (from != other.from) return false
            if (to != other.to) return false
            if (relation != other.relation) return false

            return true
        }

        override fun hashCode(): Int {
            var result = from.hashCode()
            result = 31 * result + to.hashCode()
            result = 31 * result + relation.hashCode()
            return result
        }
    }
}