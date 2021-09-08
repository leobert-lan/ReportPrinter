package osp.leobert.android.reporter.review

import com.google.auto.service.AutoService
import net.steppschuh.markdowngenerator.list.TaskListItem
import net.steppschuh.markdowngenerator.text.code.CodeBlock
import net.steppschuh.markdowngenerator.text.emphasis.BoldText
import net.steppschuh.markdowngenerator.text.heading.Heading
import net.steppschuh.markdowngenerator.text.quote.Quote
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
class DoneReporter : ReporterExtension {

    private class Pack(val model: Model, val notation: Done)

    private val groupedByDate: MutableMap<String, MutableList<Pack>> = LinkedHashMap<String, MutableList<Pack>>()
    private val END = "\n"
    private val RETURN = "\r\n"

    private fun getDay(): String {
        val date = Date(System.currentTimeMillis())
        val timeStyle: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE)
        return timeStyle.format(date)
    }

    override fun applicableAnnotations(): MutableSet<String> {
        return mutableSetOf(Done::class.java.name, Dones::class.java.name)
    }

    override fun generateReport(previousData: MutableMap<String, MutableList<Model>>?): Result {
        if (previousData == null) return Result.newBuilder().handled(false).build()

        val doneModels: List<Model>? = previousData[Done::class.java.name]
        val donesModels: List<Model>? = previousData[Dones::class.java.name]
        if (doneModels.isNullOrEmpty() && donesModels.isNullOrEmpty()) return Result.newBuilder().handled(false).build()
        val docBuilder = StringBuilder()

        doneModels?.forEach { model ->
            val annotation: Done = model.element.getAnnotation(Done::class.java)
            groupBy(model, annotation)
        }

        donesModels?.forEach { model ->
            val annotation: Dones = model.element.getAnnotation(Dones::class.java)
            annotation.dones.forEach {
                groupBy(model, it)
            }
        }

        docBuilder.append(Heading("Done-List")).append(END).append(RETURN)


        docBuilder.append(BoldText("generated at ${getDay()}")).append(END).append(RETURN)

        groupedByDate.forEach { (k, v) ->
            // 时间
            docBuilder.append(Heading(k.takeIf { it.isNotEmpty() } ?: "Undefined", 2)).append(END).append(RETURN)
            v.forEach { pack ->

                docBuilder.append(TaskListItem(pack.notation.desc, true)).append(RETURN)

                docBuilder.append(CodeBlock("ref: " + pack.model.name)).append(END)
                docBuilder.append(RETURN)
            }
        }

        return Result.newBuilder()
            .handled(true)
            .reportFileNamePrefix("Done-List")
            .fileExt("md")
            .reportContent(docBuilder.toString())
            .build()
    }

    private fun groupBy(model: Model, notation: Done) {
        val groupKey: String = notation.date
        if (groupedByDate.containsKey(groupKey)) {
            groupedByDate[groupKey]?.add(Pack(model, notation))
        } else {
            val list: MutableList<Pack> = ArrayList<Pack>()
            list.add(Pack(model, notation))
            groupedByDate[groupKey] = list
        }
    }
}