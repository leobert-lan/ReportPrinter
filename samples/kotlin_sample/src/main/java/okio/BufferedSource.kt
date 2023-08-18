package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.InputStream
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset

// Method signatures only
@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
interface BufferedSource : Source, ReadableByteChannel {

    val buffer: Buffer

    fun exhausted(): Boolean // TODO()

    fun require(byteCount: Long) // TODO()

    fun request(byteCount: Long): Boolean // TODO()

    fun readByte(): Byte // TODO()

    // Other method signatures

    fun readString(charset: Charset): String // TODO()

    fun readString(byteCount: Long, charset: Charset): String // TODO()

    fun indexOf(b: Byte): Long // TODO()

    // Other method signatures

    fun inputStream(): InputStream // TODO()

}