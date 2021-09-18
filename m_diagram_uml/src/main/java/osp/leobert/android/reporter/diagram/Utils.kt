package osp.leobert.android.reporter.diagram

import osp.leobert.android.reporter.diagram.notation.Diagram
import javax.lang.model.element.Element

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */
object Utils {

    fun preferedQulifier(notatedNotation: Element?, notation: Diagram): String {
        return notation.qualifier
    }

    inline fun <reified R> Any?.takeIfInstance(): R? {
        if (this is R) return this
        return null
    }
}