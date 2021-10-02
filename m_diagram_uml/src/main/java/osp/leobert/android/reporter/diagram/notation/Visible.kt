package osp.leobert.android.reporter.diagram.notation

import osp.leobert.android.reporter.diagram.core.IModifierMatcher
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

enum class Visible : IModifierMatcher {
    Private {
        override fun match(element: Element): Boolean {
            return element.modifiers.contains(Modifier.PRIVATE)
        }
    },
    Package {
        override fun match(element: Element): Boolean {
            return (Private.match(element) || Protected.match(element) || Public.match(element)).not()
        }
    },
    Protected {
        override fun match(element: Element): Boolean {
            return element.modifiers.contains(Modifier.PROTECTED)
        }
    },
    Public {
        override fun match(element: Element): Boolean {
            return element.modifiers.contains(Modifier.PUBLIC)
        }
    };


}