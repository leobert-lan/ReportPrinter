package osp.leobert.android.reporter.diagram

import com.google.auto.common.MoreElements
import com.google.auto.common.MoreTypes
import osp.leobert.android.reporter.diagram.core.IUmlElementHandler
import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ErrorType
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable
import javax.lang.model.type.WildcardType
import javax.lang.model.util.SimpleTypeVisitor6

/**
 * <p><b>Package:</b> osp.leobert.android.reporter.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Utils </p>
 * Created by leobert on 2021/9/18.
 */
object Utils {

    inline fun <reified R> Any?.takeIfInstance(): R? {
        if (this is R) return this
        return null
    }

    /**
     * remove the package info of [Element]
     * todo : it's too specify, far away to use in the whole system!
     * enhance it in 1.0.2
     * */
    fun Element?.nameRemovedPkg(ifNull: String): String {
        val fullName = this?.asType()?.toString() ?: ifNull

        var tmp = this?.enclosingElement
        while (tmp != null && tmp.kind != ElementKind.PACKAGE) {
            tmp = tmp.enclosingElement
        }

        val pkg = tmp?.asType()?.toString()?.run { "$this." } ?: ""
        return fullName.replace(pkg, "")
    }

    fun Element?.ifTypeElement(): TypeElement? {
        return try {
            if (this == null) return null
            MoreElements.asType(this)
        } catch (ignore: Exception) {
            null
        }
    }

    fun Element?.ifExecutable(): ExecutableType? {
        return try {
            this?.asType().run {
                MoreTypes.asExecutable(this)
            }
        } catch (ignore: Exception) {
            null
        }
    }


    private abstract class CastingTypeVisitor<T> constructor(private val label: String) :
        SimpleTypeVisitor6<T, Void?>() {
        override fun defaultAction(e: TypeMirror, v: Void?): T {
            throw IllegalArgumentException("$e does not represent a $label")
        }
    }

    private class FetchClassTypeVisitor : CastingTypeVisitor<List<DeclaredType>>(label = "") {
        override fun defaultAction(e: TypeMirror, v: Void?): List<DeclaredType> {
            //ignore it
            return emptyList()
        }

        override fun visitArray(t: ArrayType, p: Void?): List<DeclaredType> {
            return t.componentType.accept(this, p)
        }

        override fun visitWildcard(t: WildcardType, p: Void?): List<DeclaredType> {
            val ret = arrayListOf<DeclaredType>()

            t.superBound?.let {
                ret.addAll(it.accept(this, p))
            }
            t.extendsBound?.let {
                ret.addAll(it.accept(this, p))
            }
            return ret
        }

        override fun visitDeclared(t: DeclaredType, p: Void?): List<DeclaredType> {
            val ret = arrayListOf(t)
            t.typeArguments?.forEach {
                ret.addAll(it.accept(this, p))
            }
            return ret.toSet().toList()
        }

        override fun visitError(t: ErrorType, p: Void?): List<DeclaredType> {
            return visitDeclared(t, p)
        }

        override fun visitTypeVariable(t: TypeVariable, p: Void?): List<DeclaredType> {
            val ret = arrayListOf<DeclaredType>()

            t.lowerBound?.let {
                ret.addAll(it.accept(this, p))
            }
            t.upperBound?.let {
                ret.addAll(it.accept(this, p))
            }
            return ret
        }
    }

    fun TypeMirror.fetchDeclaredType(): List<DeclaredType> {
        return this.accept(FetchClassTypeVisitor(), null)
    }

    fun TypeMirror.ifElement(): Element? {
        return try {
            MoreTypes.asElement(this)
        } catch (e: Exception) {
            null
        }
    }

    fun shouldIgnoreUmlElement(element: Element, diagram: ClassDiagram): Boolean {
        if (element !is TypeElement) return true

        val clzIgnorance = IUmlElementHandler.ClassDiagramConfig.getIgnorance(diagram)

        if (clzIgnorance.isIgnoreExclude(element)) return false

        if (element.qualifiedName.toString().startsWith("java.")) return true
        if (element.qualifiedName.toString().startsWith("javac.")) return true
        if (element.qualifiedName.toString().startsWith("javax.")) return true
        if (element.qualifiedName.toString().startsWith("android.")) return true
        if (element.qualifiedName.toString().startsWith("androidx.")) return true

        if (clzIgnorance.isIgnored(element)) {
            return true
        }

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

    private val hexArray = "0123456789ABCDEF".toCharArray()

    fun Long.rgba(): String {
        return "#${byteToHex((this ushr 24).toByte())}${byteToHex((this ushr 16).toByte())}${
            byteToHex(
                (this ushr 8).toByte()
            )
        }${byteToHex(this.toByte())}"
    }

    fun byteToHex(b: Byte): String {
        val hexChars = CharArray(2)
        val v = b.toInt() and 0xFF
        hexChars[0] = hexArray[v ushr 4]
        hexChars[1] = hexArray[v and 0x0F]
        return String(hexChars)
    }
}