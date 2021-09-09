package osp.leobert.android.reporter.review

import com.google.auto.service.AutoService
import net.steppschuh.markdowngenerator.link.Link
import net.steppschuh.markdowngenerator.list.TaskListItem
import net.steppschuh.markdowngenerator.text.code.CodeBlock
import net.steppschuh.markdowngenerator.text.emphasis.BoldText
import net.steppschuh.markdowngenerator.text.heading.Heading
import osp.leobert.android.reportprinter.spi.Model
import osp.leobert.android.reportprinter.spi.ReporterExtension
import osp.leobert.android.reportprinter.spi.Result
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by leobert on 2021/9/7.
 */
@AutoService(ReporterExtension::class)
class BugReporter : ReporterExtension {

    private class Pack(val model: Model, val notation: Bug)

    private val groupedByModule: MutableMap<String, MutableList<Pack>> = LinkedHashMap<String, MutableList<Pack>>()
    private val END = "\n"
    private val RETURN = "\r\n"

    private fun getDay(): String {
        val date = Date(System.currentTimeMillis())
        val timeStyle: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE)
        return timeStyle.format(date)
    }

    override fun applicableAnnotations(): MutableSet<String> {
        return mutableSetOf(Bug::class.java.name, Bugs::class.java.name)
    }

    override fun generateReport(previousData: MutableMap<String, MutableList<Model>>?): Result {
        if (previousData == null) return Result.newBuilder().handled(false).build()

        val bugModels: List<Model>? = previousData[Bug::class.java.name]
        val bugsModels: List<Model>? = previousData[Bugs::class.java.name]
        if (bugModels.isNullOrEmpty() && bugsModels.isNullOrEmpty()) return Result.newBuilder().handled(false).build()
        val docBuilder = StringBuilder()

        bugModels?.forEach { model ->
            model.element.getAnnotationsByType(Bug::class.java).forEach { annotation ->
                groupBy(model, annotation)
            }
        }

        bugsModels?.forEach { model ->
            model.element.getAnnotationsByType(Bugs::class.java).forEach { bugs ->
                bugs.bugs.forEach { bug ->
                    groupBy(model, bug)
                }
            }
        }

        docBuilder.append(Heading("Bug-List")).append(END).append(RETURN)


        docBuilder.append(BoldText("generated at ${getDay()}")).append(END).append(RETURN)

        groupedByModule.forEach { (k, v) ->
            // 时间
            docBuilder.append(Heading(k.takeIf { it.isNotEmpty() } ?: "Undefined Module", 2)).append(END).append(RETURN)
            v.sortedBy {
                it.notation.state
            }.forEach { pack ->

                docBuilder.append(TaskListItem(pack.notation.desc, pack.notation.state.done)).append(RETURN)

                docBuilder.append(CodeBlock("ref: " + pack.model.name)).append(END).append(RETURN)

                val trackUrl = pack.notation.track
                if (trackUrl.isEmpty()) {
                    docBuilder.append("track: 无").append(END)
                } else {
                    docBuilder.append("track: ").append(Link(trackUrl)).append(END)
                }
                docBuilder.append(RETURN)
            }
        }

        return Result.newBuilder()
            .handled(true)
            .reportFileNamePrefix("Bug-List")
            .fileExt("md")
            .reportContent(docBuilder.toString())
            .build()
    }

    private fun groupBy(model: Model, notation: Bug) {
        val groupKey: String = notation.module
        if (groupedByModule.containsKey(groupKey)) {
            groupedByModule[groupKey]?.add(Pack(model, notation))
        } else {
            val list: MutableList<Pack> = ArrayList<Pack>()
            list.add(Pack(model, notation))
            groupedByModule[groupKey] = list
        }
    }


}