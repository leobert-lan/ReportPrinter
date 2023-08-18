package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.OutputStream
import java.nio.channels.WritableByteChannel
import java.nio.charset.Charset

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
interface BufferedSink : Sink, WritableByteChannel {

    val buffer: Buffer // TODO

    fun write(byteString: ByteString): BufferedSink // TODO

    fun write(byteString: ByteString, offset: Int, byteCount: Int): BufferedSink // TODO

    fun write(source: ByteArray): BufferedSink // TODO

    fun write(source: ByteArray, offset: Int, byteCount: Int): BufferedSink // TODO

    fun writeAll(source: Source): Long // TODO

    fun write(source: Source, byteCount: Long): BufferedSink // TODO

    fun writeUtf8(string: String): BufferedSink // TODO

    fun writeUtf8(string: String, beginIndex: Int, endIndex: Int): BufferedSink // TODO

    fun writeUtf8CodePoint(codePoint: Int): BufferedSink // TODO

    fun writeString(string: String, charset: Charset): BufferedSink // TODO

    fun writeString(string: String, beginIndex: Int, endIndex: Int, charset: Charset): BufferedSink // TODO

    fun writeByte(b: Int): BufferedSink // TODO

    fun writeShort(s: Int): BufferedSink // TODO

    fun writeShortLe(s: Int): BufferedSink // TODO

    fun writeInt(i: Int): BufferedSink // TODO

    fun writeIntLe(i: Int): BufferedSink // TODO

    fun writeLong(v: Long): BufferedSink // TODO

    fun writeLongLe(v: Long): BufferedSink // TODO

    fun writeDecimalLong(v: Long): BufferedSink // TODO

    fun writeHexadecimalUnsignedLong(v: Long): BufferedSink // TODO

    override fun flush() // TODO

    fun emit(): BufferedSink // TODO

    fun emitCompleteSegments(): BufferedSink // TODO

    fun outputStream(): OutputStream // TODO

}