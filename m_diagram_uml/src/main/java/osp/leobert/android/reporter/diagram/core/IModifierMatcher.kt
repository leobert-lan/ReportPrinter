package osp.leobert.android.reporter.diagram.core

import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram.core </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> IModifierMatcher </p>
 * Created by leobert on 2021/10/2.
 */
interface IModifierMatcher {
    fun match(element: Element): Boolean

    companion object {
        object Static:IModifierMatcher {
            override fun match(element: Element): Boolean {
               return element.modifiers.contains(Modifier.STATIC)
            }
        }

        object Abstract:IModifierMatcher {
            override fun match(element: Element): Boolean {
                return element.modifiers.contains(Modifier.ABSTRACT)
            }
        }
    }
}