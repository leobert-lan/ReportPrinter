package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Clz </p>
 * Created by leobert on 2021/9/18.
 */

@ClassDiagram("AAA")
annotation class DemoDiagram

@DemoDiagram
abstract class SuperClz : SealedI {
    var superI: Int = 0

}

@GenerateClassDiagram
@DemoDiagram
class Clz : SuperClz(), SealedI {

    val list: List<Enum> = listOf()
    val listOfList: ArrayList<List<Enum>> = arrayListOf()
    val listOfArray: List<Array<C>> = arrayListOf()
    val array: Array<C> = arrayOf()

    //代理类型目前尚未得到优雅的处理
//    val wilcard: CWildcard<A, B> by lazy { CWildcard() }

    val wilcard2: CWildcard<A, B> = CWildcard()

    val int: Int? = null

    override val eInInterface: Enum?
        get() = null
}

class CWildcard<T, M> {

}

class A
class B
class C

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
@DemoDiagram
enum class Enum(val a: A?) {
    E1(null), E2(null)
}

@DemoDiagram
interface InterFace {
    companion object {
        //        @JvmField 两种注解会改变编译的结果
        @JvmStatic
        val a: Enum? = null
    }

    val eInInterface: Enum?
}

@DemoDiagram
sealed interface SealedI : InterFace {}

//@ClassDiagram(qualifier = "AAAB")
//annotation class AAAB

@GenerateClassDiagram
//(annos = [AAAB::class])
@AAAB
sealed class SealedClz2 {
    fun m() {
        print(Enum.E1.a)
    }
}

