package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.util.zip.Deflater

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
class DeflaterSink(
    private val sink: BufferedSink,
    private val deflater: Deflater
) : Sink {

    private var closed = false

    override fun write(source: Buffer, byteCount: Long) {
        TODO()
    }

    private fun deflate(syncFlush: Boolean) {
        TODO()
    }

    override fun flush() {
        TODO()
    }

    internal fun finishDeflate() {
        TODO()
    }

    override fun close() {
        TODO()
    }

    override fun timeout(): Timeout {
        TODO()
    }

}

inline fun Sink.deflate(
    deflater: Deflater = Deflater()
): DeflaterSink {
    TODO()
}