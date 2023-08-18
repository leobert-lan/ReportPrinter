package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import javax.crypto.Cipher

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
class CipherSink(
    private val sink: BufferedSink,
    val cipher: Cipher
) : Sink {

    private val blockSize = cipher.blockSize

    private var closed = false

    init {
        require(blockSize > 0) { "Block cipher required $cipher" }
    }

    override fun write(source: Buffer, byteCount: Long) {
        TODO()
    }

    private fun update(source: Buffer, remaining: Long): Int {
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

    private fun doFinal(): Throwable? {
        TODO()
    }

}