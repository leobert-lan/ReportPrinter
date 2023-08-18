package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.IOException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

@Graphs.Okio
@GenerateClassDiagram
open class AsyncTimeout : Timeout() {

    fun enter() {

        TODO()
    }

    fun exit(): Boolean {
        TODO()
    }

    open fun timedOut() {
        TODO()
    }

    fun sink(sink: Sink): Sink {
        TODO()
    }

    fun source(source: Source): Source {
        TODO()
    }

    fun <T> withTimeout(block: () -> T): T {
        TODO()
    }

    open fun newTimeoutException(cause: IOException?): IOException {
        TODO()
    }


    class Watchdog : Thread() { // TODO()

    }

}