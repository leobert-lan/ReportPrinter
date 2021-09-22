package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Clz </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */

@ClassDiagram("AAA")
annotation class DemoDiagram

@DemoDiagram
abstract class SuperClz : SealedI {
    var superI: Int = 0

}

@ClassDiagram
@DemoDiagram
class Clz : SuperClz(), SealedI {
//    var i: Int = 0
//    var i2: Int? = null

    val strs: List<Enum> = listOf()
    val strs2: ArrayList<List<Enum>> = arrayListOf()

    //代理类型目前尚未得到优雅的处理
//    val wilcard: CWildcard<A, B> by lazy { CWildcard() }

    val wilcard2: CWildcard<A, B> = CWildcard()

    val strName: Int? = null
    override val eInInterface: Enum?
        get() = null
}

class CWildcard<T, M> {

}

class A
class B

//public enum Enum {
//   E1,
//   E2;
//
//   @Nullable
//   private final A a;
//
//   @Nullable
//   public final A getA() {
//      return this.a;
//   }
//
//   private Enum(A a) {
//      this.a = a;
//   }
//}
@ClassDiagram("AAA")
enum class Enum(public val a: A?) {
    E1(null), E2(null)
}

@ClassDiagram("AAA")
interface InterFace {
    companion object {
        @JvmStatic
        val a: Enum? = null
    }

    val eInInterface: Enum?
}

@ClassDiagram("AAA")
sealed interface SealedI : InterFace {}


@ClassDiagram("AAAB")
sealed class SealedClz {
    fun m() {
        print(Enum.E1.a)
    }
}

