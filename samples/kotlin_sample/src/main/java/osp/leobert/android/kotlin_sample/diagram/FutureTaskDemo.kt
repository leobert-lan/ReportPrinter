package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.diagram.notation.IgnoreExclude
import osp.leobert.android.reporter.diagram.notation.Visible
import java.util.concurrent.CompletableFuture
import java.util.concurrent.FutureTask
import java.util.concurrent.RecursiveAction
import java.util.concurrent.RecursiveTask
import java.util.concurrent.RunnableScheduledFuture

/**
 * Classname: FutureTaskDemo </p>
 * Description: TODO </p>
 * Created by leobert on 2022/9/19.
 */

@ClassDiagram(
    qualifier = "FutureTask", fieldVisible = [/*Visible.Package, */Visible.Public],
    methodVisible=[Visible.Public],
    ignoreExclude = IgnoreExclude(
        exactFullNames = ["java.lang.Runnable"],
        fullNamePatterns = ["java\\.util\\.concurrent\\.\\w"]
    )
)
annotation class FutureTaskDiagram


@GenerateClassDiagram
@FutureTaskDiagram
class FutureTaskDemo(
    private val futureTask: FutureTask<String>,
    private val rsf:RunnableScheduledFuture<String>,
    private val rt:RecursiveTask<String>,
    val a:CompletableFuture<String>,
    val b:RecursiveTask<String>,
    val c:RecursiveAction
) {
    var task: FutureTask<String>? = null

}
