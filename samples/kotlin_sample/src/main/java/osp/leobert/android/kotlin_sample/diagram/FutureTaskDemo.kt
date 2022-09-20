package osp.leobert.android.kotlin_sample.diagram

import osp.leobert.android.reporter.diagram.notation.ClassDiagram
import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.android.reporter.diagram.notation.IgnoreExclude
import osp.leobert.android.reporter.diagram.notation.Visible
import java.util.concurrent.FutureTask

/**
 * Classname: FutureTaskDemo </p>
 * Description: TODO </p>
 * Created by leobert on 2022/9/19.
 */

@ClassDiagram(
    qualifier = "FutureTask", fieldVisible = [Visible.Package, Visible.Public],
    ignoreExclude = IgnoreExclude(
        fullNamePatterns = ["java\\.util\\.concurrent\\.\\w"]
    )
)
annotation class FutureTaskDiagram


@GenerateClassDiagram
@FutureTaskDiagram
class FutureTaskDemo {
    var task: FutureTask<String>? = null
}
