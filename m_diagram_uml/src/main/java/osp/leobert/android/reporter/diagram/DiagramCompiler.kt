package osp.leobert.android.reporter.diagram

import androidx.annotation.RequiresApi
import com.google.auto.service.AutoService
import osp.leobert.android.reporter.diagram.Utils.takeIfInstance
import osp.leobert.android.reporter.diagram.notation.Diagram
import osp.leobert.android.reportprinter.spi.Model
import osp.leobert.android.reportprinter.spi.ReporterExtension
import osp.leobert.android.reportprinter.spi.Result
import java.util.stream.Collectors
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(ReporterExtension::class)
/*skip lint warning!*/
@RequiresApi(24)
class DiagramCompiler : ReporterExtension {

    private val groups = mutableMapOf<String, MutableList<Model>>()

    override fun applicableAnnotations(): MutableSet<String> {
        return mutableSetOf(Diagram::class.java.name)
    }

    override fun generateReport(previousData: MutableMap<String, MutableList<Model>>?): Result {

        val notatedByDiagramNotations = filterNotation(previousData)

        val candidates = previousData?.get(Diagram::class.java.name)?.filter {
            it.elementKind == ElementKind.INTERFACE || it.elementKind == ElementKind.CLASS || it.elementKind == ElementKind.ENUM
        }

        if (candidates.isNullOrEmpty()) return Result.newBuilder().handled(false).build()

        groups.clear()

        val sb = StringBuilder()

        notatedByDiagramNotations.forEach {
            handleGroup(it, candidates)
        }

        groups.forEach { (t: String, u: MutableList<Model>) ->
            sb.append(t).append(" --> ")
                .append("[")
                .append(
                    u.map {
                        it.element.takeIfInstance<TypeElement>()?.qualifiedName?.toString()
                    }.joinToString(",\r\n")
//                        .stream().collect(Collectors.joining())
                ).append("]\r\n")
        }

        val content = previousData[Diagram::class.java.name]?.map {
            it.element.simpleName.toString() + ";" + it.element.kind.name + "\r\n"
        }?.parallelStream()?.collect(Collectors.joining())

        sb.append(content)
        return Result.newBuilder().handled(true)
            .reportFileNamePrefix("Diagram")
            .reportContent(sb.toString())
            .fileExt("puml")
            .build()
    }

    private fun filterNotation(previousData: MutableMap<String, MutableList<Model>>?): List<Model> {
        return previousData?.get(Diagram::class.java.name)?.filter {
            it.elementKind == ElementKind.ANNOTATION_TYPE
        } ?: emptyList()
    }

    private fun handleGroup(
        qualifier: Model,
        candidates: List<Model>
    ) {

        candidates.forEach {
            val diagram = if (it.element.hasAnnotationOf(qualifier.element)) {
                findAnnotationByAnnotation(it.element.annotationMirrors, Diagram::class.java)
            } else {
                it.element.getAnnotation(Diagram::class.java)
            } ?: return@forEach

            groups.getOrDefault(diagram.qualifier, arrayListOf()).apply {
                this.add(it)
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