package osp.leobert.android.reporter.diagram

import com.google.auto.common.MoreTypes
import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */
object Utils {

    fun preferedQulifier(notatedNotation: Element?, notation: ClassDiagram): String {
        return notation.qualifier
    }

    inline fun <reified R> Any?.takeIfInstance(): R? {
        if (this is R) return this
        return null
    }

    fun TypeMirror.ifElement(): Element? {
        return try {
            MoreTypes.asElement(this)
        } catch (e: Exception) {
            null
        }
    }

    fun shouldIgnoreEmlElement(element: Element, diagram: ClassDiagram): Boolean {
        if (element !is TypeElement) return true
        if (element.qualifiedName.toString() == "java.lang.Object") return true

//        todo diagram config parse
        return false
    }

    inline fun <T> List<T>.forEachWindowSize2(consumer: (first: T, second: T) -> Unit) {
        if (this.size < 2) return
        var index = 0

        while (index + 1 < this.size) {
            val first = this[index]
            val second = this[index + 1]
            consumer(first, second)
            index++
        }
    }
}