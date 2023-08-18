package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.util.zip.CRC32
import java.util.zip.Deflater

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
class GzipSink(sink: Sink) : Sink {

    private val sink = RealBufferedSink(sink)

    val deflater: Deflater = TODO() // TODO

    private val deflaterSink = DeflaterSink(this.sink, deflater) // TODO

    private var closed = false

    private val crc = CRC32() // TODO

    override fun write(source: Buffer, byteCount: Long) {
        TODO()
    }

    override fun flush() {
        TODO()
    }

    override fun timeout(): Timeout {
        TODO()
    }

    override fun close() {
        TODO()
    }

    private fun writeFooter() {
        TODO()
    }

    private fun updateCrc(buffer: Buffer, byteCount: Long) {
        TODO()
    }

}

inline fun Sink.gzip(): GzipSink {
    TODO()
}