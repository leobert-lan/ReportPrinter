package osp.leobert.android.reporter.diagram

import com.google.auto.service.AutoService
import osp.leobert.android.reporter.diagram.Utils.forEachWindowSize2
import osp.leobert.android.reporter.diagram.Utils.nameRemovedPkg
import osp.leobert.android.reporter.diagram.Utils.rgba
import osp.leobert.android.reporter.diagram.Utils.takeIfInstance
import osp.leobert.android.reporter.diagram.core.*
import osp.leobert.android.reporter.diagram.graph.DAG
import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.diagram.notation.NameSpace
import osp.leobert.android.reportprinter.spi.IModuleInitializer
import osp.leobert.android.reportprinter.spi.Model
import osp.leobert.android.reportprinter.spi.ReporterExtension
import osp.leobert.android.reportprinter.spi.Result
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

@AutoService(ReporterExtension::class)
class DiagramCompiler : ReporterExtension, IModuleInitializer {

    companion object {
        const val RETURN = "\r\n"
    }

    private val groups = mutableMapOf<String, MutableList<Pair<ClassDiagram, Model>>>()
    private val diagramGraphByGroup = mutableMapOf<String, DAG<UmlElement>>()
    private val diagramUmlElementCache = mutableMapOf<String, MutableSet<UmlElement>>()

    private var env: ProcessingEnvironment? = null

    override fun initialize(env: ProcessingEnvironment?) {
        this.env = env
    }

    override fun applicableAnnotations(): MutableSet<String> {
        return mutableSetOf(
            ClassDiagram::class.java.name, //find custom diagram definition
            GenerateClassDiagram::class.java.name, // find elements to be drawn in uml
            NameSpace::class.java.name //find custom namespace in uml
        )
    }

    override fun generateReport(previousData: MutableMap<String, MutableList<Model>>?): Result {

        val customClzDiagrams = filterNotation(ClassDiagram::class.java.name, previousData)

        val customNameSpaces = filterNotation(NameSpace::class.java.name, previousData)

        val candidates = previousData?.get(GenerateClassDiagram::class.java.name)?.filter {
            it.elementKind == ElementKind.INTERFACE || it.elementKind == ElementKind.CLASS || it.elementKind == ElementKind.ENUM
        }

        if (candidates.isNullOrEmpty()) return Result.newBuilder().handled(false).build()

        groups.clear()
        diagramGraphByGroup.clear()
        diagramUmlElementCache.clear()

        val plantUml = StringBuilder()

        customClzDiagrams.forEach { customDiagram ->
            groupElementsByUmlQualifier(customDiagram, candidates, groups)
        }

        //todo consider alias
        val nameGetter = { element: UmlElement ->
            val removePkg = element.element.nameRemovedPkg(element.name)
            "\"$removePkg\"".replace(".", "$")
        }

        val resultBuilder = Result.newBuilder().handled(true)

        groups.forEach { (qualifierName: String, u: MutableList<Pair<ClassDiagram, Model>>) ->

            plantUml.clear()

            /*子图*/
            val graph = diagramGraphByGroup.getOrPut(qualifierName) {
                DAG(nameOf = { it.name }, printChunkMax = 10)
            }

            // a set of elements by qualifier.
            /*图中已解析元素的cache*/
            val cache = diagramUmlElementCache.getOrPut(qualifierName) {
                LinkedHashSet()
            }

            val context = DrawerContext(env, cache)


            u.forEach {
                handleUmlElement(it.second, it.first, graph, context)
            }

            plantUml.append("@startuml").append(RETURN)
            plantUml.append("'").append(qualifierName).append(RETURN)

            // draw all uml-element

            // group to different namespace then draw each.
            val nsConflictMap = mutableMapOf<UmlElement, List<Model>>()
            val noneNsElements = mutableListOf<UmlElement>()
            val nameSpacedElements = mutableMapOf<NameSpace, MutableList<UmlElement>>()
            cache.forEach {
                val ele = it.element ?: return@forEach
                val nsList = customNameSpaces.filter { customNs ->
                    ele.hasAnnotationOf(customNs.element)
                }

                if (nsList.isEmpty()) {
                    noneNsElements.add(it)
                } else if (nsList.size > 1) {
                    nsConflictMap[it] = nsList
                } else {
                    nameSpacedElements.getOrPut(
                        nsList.first().element.getAnnotation(NameSpace::class.java)
                    ) {
                        mutableListOf()
                    }.add(it)
                }
            }

            // error info:namespace conflict
            if (nsConflictMap.isNotEmpty()) {
                plantUml.append("'").append("multi namespace ignored:").append(RETURN)
                nsConflictMap.forEach { it ->
                    plantUml.append("'")
                        .append(it.key.name)
                        .append(" -> [ ")
                        .append(
                            it.value.joinToString(",") { ns -> ns.name }
                        )
                        .append(" ]").append(RETURN)
                }
            }

            nameSpacedElements.forEach { entry ->
                plantUml.append("namespace ").append(entry.key.name).append(" ").append(entry.key.color.rgba()).append(" {").append(RETURN)
                entry.value.forEach {
                    plantUml.append(it.umlElement(context, 4)).append(RETURN)
                }
                plantUml.append("}").append(RETURN)
            }


//            cache
            noneNsElements.forEach {
                plantUml.append(it.umlElement(context)).append(RETURN)
            }

            graph.recursive(UmlStub.sInstance, arrayListOf())

            val relationHasDraw = mutableSetOf<Relationship>()

            // draw all relationship
            graph.deepPathList.forEach { path ->

                plantUml.append("'<")
                    .append(
                        path.joinToString {
                            it.element?.nameRemovedPkg(it.name) ?: it.name
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
                                        plantUml.append(it).append(RETURN)
                                    }
                                }
                            }

                        } catch (ignore: Exception) {
                            plantUml.append(ignore.localizedMessage).append(RETURN)
                        }
                    }
                }
            }
            plantUml.append("@enduml").append(RETURN)

            resultBuilder.fileBuilder()
                .reportFileNamePrefix("${qualifierName}Diagram")
                .reportContent(plantUml.toString())
                .fileExt("puml")
                .build()
        }

        return resultBuilder.build()
    }

    private fun handleUmlElement(
        model: Model,
        diagram: ClassDiagram,
        graph: DAG<UmlElement>,
        context: DrawerContext
    ) {
        IUmlElementHandler.HandlerImpl.handle(
            from = UmlStub.sInstance,
            relation = Relation.Stub,
            element = model.element,
            diagram = diagram,
            graph = graph,
            context = context
        )

    }

    private fun filterNotation(
        annotationName: String,
        previousData: MutableMap<String, MutableList<Model>>?
    ): List<Model> {
        return previousData?.get(annotationName)?.filter {
            it.elementKind == ElementKind.ANNOTATION_TYPE
        } ?: emptyList()
    }

    /**
     * group all elements be notated by [GenerateClassDiagram] to different groups
     *
     * ```
     * @ClassDiagram("key")
     * annotation class Custom
     *
     * @Custom
     * class Foo {}
     *
     * @ClassDiagram("k2")
     * class Bar {}
     * ```
     * will separate to 2 group:
     *
     * key -> {Foo}
     * k2 -> {Bar}
     * */
    private fun groupElementsByUmlQualifier(
        customDiagram: Model,
        candidates: List<Model>,
        targetGroups: MutableMap<String, MutableList<Pair<ClassDiagram, Model>>>
    ) {

        candidates.forEach {
            val diagram = if (it.element.hasAnnotationOf(customDiagram.element)) {
                //case Foo
                findAnnotationByAnnotation(it.element.annotationMirrors, ClassDiagram::class.java)
            } else {
                //case Bar

                //fixme now it's must be null, cause ClassDiagram only notate at an annotation,consider custom node config and
                // use config-bean when parse relation!

//                    val nameOfGCD = GenerateClassDiagram::class.java.name
//                    it.element.annotationMirrors?.find { am->
//                        am.annotationType.toString() == nameOfGCD
//                    }?.elementValues?.entries?.find { entry->
//                        entry.key.simpleName.toString() == "annos"
//                    }?.value?.value?.takeIfInstance<TypeMirror>()?.find { c->
//                        c.name == qualifier.element.asType().toString()
//                    }?.getAnnotation(ClassDiagram::class.java)?:

                it.element.getAnnotation(ClassDiagram::class.java)
            } ?: return@forEach

            (targetGroups[diagram.qualifier] ?: arrayListOf()).apply {
                this.add(diagram to it)
                targetGroups[diagram.qualifier] = this
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