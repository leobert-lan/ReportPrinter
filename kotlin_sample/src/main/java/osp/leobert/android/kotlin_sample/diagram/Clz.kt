package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.Diagram

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample.diagram </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Clz </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/9/18.
 */

@Diagram("AAA")
annotation class DemoDiagram

@Diagram
@DemoDiagram
class Clz {
}

@Diagram("AAA")
enum class Enum {}

@Diagram("AAA")
interface InterFace {
}

@Diagram("AAA")
sealed interface SealedInterface {}


@Diagram("AAAB")
sealed class SealedClz

