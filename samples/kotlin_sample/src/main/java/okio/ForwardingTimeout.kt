package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.IOException
import java.util.concurrent.TimeUnit

@Graphs.Okio
@GenerateClassDiagram
open class ForwardingTimeout(

    @get:JvmName("delegate")
    @set:JvmSynthetic
    var delegate: Timeout

) : Timeout() {

    fun setDelegate(delegate: Timeout): ForwardingTimeout {
        TODO()
    }

    override fun timeout(timeout: Long, unit: TimeUnit): Timeout {
        TODO()
    }

    override fun timeoutNanos(): Long {
        TODO()
    }

    override fun hasDeadline(): Boolean {
        TODO()
    }

    override fun deadlineNanoTime(): Long {
        TODO()
    }

    override fun deadlineNanoTime(deadlineNanoTime: Long): Timeout {
        TODO()
    }

    override fun clearTimeout(): Timeout {
        TODO()
    }

    override fun clearDeadline(): Timeout {
        TODO()
    }

    @Throws(IOException::class)
    override fun throwIfReached() {
        TODO()
    }

}