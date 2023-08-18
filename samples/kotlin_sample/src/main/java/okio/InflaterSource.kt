package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.IOException
import java.util.zip.Inflater

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
class InflaterSource
internal constructor(
    private val source: BufferedSource,
    private val inflater: Inflater
) : Source {


    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    @Throws(IOException::class)
    fun readOrInflate(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    @Throws(IOException::class)
    fun refill(): Boolean {
        TODO()
    }

    private fun releaseBytesAfterInflate() {
        TODO()
    }

    override fun timeout(): Timeout {
        TODO()
    }

    @Throws(IOException::class)
    override fun close() {
        TODO()
    }

}

inline fun Source.inflate(
    inflater: Inflater = Inflater()
): InflaterSource {
    TODO()
}