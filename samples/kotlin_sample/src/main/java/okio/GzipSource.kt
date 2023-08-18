package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.IOException
import java.util.zip.CRC32
import java.util.zip.Inflater

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
class GzipSource(source: Source) : Source {


    private val source = RealBufferedSource(source)

    private val inflater = Inflater(true)

    private val inflaterSource = InflaterSource(this.source, inflater)

    private val crc = CRC32()

    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    @Throws(IOException::class)
    private fun consumeHeader() {
        TODO()
    }

    @Throws(IOException::class)
    private fun consumeTrailer() {
        TODO()
    }

    override fun timeout(): Timeout {
        TODO()
    }

    @Throws(IOException::class)
    override fun close() {
        TODO()
    }

    private fun updateCrc(buffer: Buffer, offset: Long, byteCount: Long) {
        TODO()
    }

    private fun checkEqual(name: String, expected: Int, actual: Int) {
        TODO()
    }

}

private inline fun Int.getBit(bit: Int): Boolean {
    TODO()
}

inline fun Source.gzip(): GzipSource {
    TODO()
}