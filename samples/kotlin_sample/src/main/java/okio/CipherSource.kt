package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import javax.crypto.Cipher

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
class CipherSource(
    private val source: BufferedSource,
    val cipher: Cipher
) : Source {

    private val blockSize = cipher.blockSize

    init {
        require(blockSize > 0) { "Block cipher required $cipher" }
    }

    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    private fun refill() {
        TODO()
    }

    private fun update() {
        TODO()
    }

    private fun doFinal() {
        TODO()
    }

    override fun timeout(): Timeout {
        TODO()
    }

    override fun close() {
        TODO()
    }

}