package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs

@Graphs.Okio
@Graphs.NsSource
@GenerateClassDiagram
abstract class ForwardingSource(

    @get:JvmName("delegate")
    val delegate: Source

) : Source {

    // TODO 'Source by delegate' once https://youtrack.jetbrains.com/issue/KT-23935 is fixed.

    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    override fun timeout(): Timeout {
        TODO()
    }

    override fun close() {
        TODO()
    }

    override fun toString(): String {
        TODO()
    }

}