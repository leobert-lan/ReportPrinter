package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.ClassDiagram

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Clz </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */

@ClassDiagram("AAA")
annotation class DemoDiagram

@ClassDiagram
@DemoDiagram
class Clz : SealedI {
}

@ClassDiagram("AAA")
enum class Enum {}

@ClassDiagram("AAA")
interface InterFace {
}

@ClassDiagram("AAA")
sealed interface SealedI : InterFace {}


@ClassDiagram("AAAB")
sealed class SealedClz

