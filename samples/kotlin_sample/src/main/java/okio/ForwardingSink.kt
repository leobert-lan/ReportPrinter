package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
abstract class ForwardingSink(

    /** [Sink] to which this instance is delegating. */
    @get:JvmName("delegate")
    val delegate: Sink

) : Sink {

    // TODO 'Sink by delegate' once https://youtrack.jetbrains.com/issue/KT-23935 is fixed.

    override fun write(source: Buffer, byteCount: Long) {
        TODO()
    }

    override fun flush() {
        TODO()
    }

     override fun timeout() :Timeout {
        TODO()
    }

    override fun close() {
        TODO()
    }

    override fun toString(): String {
        TODO()
    }

}