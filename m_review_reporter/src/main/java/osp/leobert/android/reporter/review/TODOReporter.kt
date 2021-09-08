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
class TODOReporter : ReporterExtension {

    private class Pack(val model: Model, val notation: TODO)

    private val groupedByDate: MutableMap<String, MutableList<Pack>> = LinkedHashMap<String, MutableList<Pack>>()
    private val END = "\n"
    private val RETURN = "\r\n"

    private fun getDay(): String {
        val date = Date(System.currentTimeMillis())
        val timeStyle: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE)
        return timeStyle.format(date)
    }

    override fun applicableAnnotations(): MutableSet<String> {
        return mutableSetOf(TODO::class.java.name, TODOs::class.java.name)
    }

    override fun generateReport(previousData: MutableMap<String, MutableList<Model>>?): Result {
        if (previousData == null) return Result.newBuilder().handled(false).build()

        val todoModels: List<Model>? = previousData[TODO::class.java.name]
        val todosModels: List<Model>? = previousData[TODOs::class.java.name]
        if (todoModels.isNullOrEmpty() && todosModels.isNullOrEmpty()) return Result.newBuilder().handled(false).build()
        val docBuilder = StringBuilder()

        todoModels?.forEach { model ->
            val annotation: TODO = model.element.getAnnotation(TODO::class.java)
            groupBy(model, annotation)
        }

        todosModels?.forEach { model ->
            val annotation: TODOs = model.element.getAnnotation(TODOs::class.java)
            annotation.todos.forEach {
                groupBy(model, it)
            }
        }

        docBuilder.append(Heading("TODO-List")).append(END).append(RETURN)


        docBuilder.append(BoldText("generated at ${getDay()}")).append(END).append(RETURN)

        groupedByDate.forEach { (k, v) ->
            // 时间
            docBuilder.append(Heading(k.takeIf { it.isNotEmpty() } ?: "right now", 2)).append(END).append(RETURN)
            v.forEach { pack ->

                docBuilder.append(TaskListItem(pack.notation.desc, false)).append(RETURN)

                docBuilder.append(CodeBlock("ref: " + pack.model.name)).append(END)
                docBuilder.append(RETURN)
            }
        }

        return Result.newBuilder()
            .handled(true)
            .reportFileNamePrefix("TODO-List")
            .fileExt("md")
            .reportContent(docBuilder.toString())
            .build()
    }

    private fun groupBy(model: Model, notation: TODO) {
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