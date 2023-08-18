package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel
import java.nio.charset.Charset

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
@Graphs.NsSource
interface Buffer : BufferedSource, BufferedSink, ByteChannel {

    var size: Long // TODO

    fun buffer(): Buffer // TODO

    override val buffer: Buffer // TODO

    override fun outputStream(): OutputStream // TODO

    override fun emitCompleteSegments(): BufferedSink // TODO

    override fun emit(): BufferedSink // TODO

    override fun exhausted(): Boolean // TODO

    override fun require(byteCount: Long) // TODO

    override fun request(byteCount: Long): Boolean // TODO

    fun peek(): BufferedSource // TODO

    override fun inputStream(): InputStream // TODO

    fun copyTo(
        out: OutputStream,
        offset: Long = 0L,
        byteCount: Long = size - offset
    ): Buffer // TODO

    fun copyTo(out: Buffer, offset: Long, byteCount: Long): Buffer // TODO

    fun copyTo(out: Buffer, offset: Long): Buffer // TODO

    fun writeTo(out: OutputStream, byteCount: Long = size): Buffer // TODO

    fun readFrom(input: InputStream): Buffer // TODO

    fun readFrom(input: InputStream, byteCount: Long): Buffer // TODO

    fun completeSegmentByteCount(): Long // TODO

    override fun readByte(): Byte // TODO

    operator fun get(pos: Long): Byte // TODO

    fun readShort(): Short // TODO

    fun readInt(): Int // TODO

    fun readLong(): Long // TODO

    fun readShortLe(): Short // TODO

    fun readIntLe(): Int // TODO

    fun readLongLe(): Long // TODO

    fun readDecimalLong(): Long // TODO

    fun readHexadecimalUnsignedLong(): Long // TODO

    fun readByteString(): ByteString // TODO

    fun readByteString(byteCount: Long): ByteString // TODO


    fun readFully(sink: Buffer, byteCount: Long) // TODO

    fun readAll(sink: Sink): Long // TODO

    fun readUtf8(): String // TODO

    fun readUtf8(byteCount: Long): String // TODO

    override fun readString(charset: Charset): String // TODO

    override fun readString(byteCount: Long, charset: Charset): String // TODO

    fun readUtf8Line(): String? // TODO

    fun readUtf8LineStrict(): String // TODO

    fun readUtf8LineStrict(limit: Long): String // TODO

    fun readUtf8CodePoint(): Int // TODO

    fun readByteArray(): ByteArray // TODO

    fun readByteArray(byteCount: Long): ByteArray // TODO

    fun read(sink: ByteArray): Int // TODO

    fun readFully(sink: ByteArray) // TODO

    fun read(sink: ByteArray, offset: Int, byteCount: Int): Int // TODO

    override fun read(sink: ByteBuffer): Int // TODO

    fun clear() // TODO

    fun skip(byteCount: Long) // TODO

    override fun write(byteString: ByteString): Buffer // TODO

    override fun write(byteString: ByteString, offset: Int, byteCount: Int): Buffer // TODO

    override fun writeUtf8(string: String): Buffer // TODO

    override fun writeUtf8(string: String, beginIndex: Int, endIndex: Int): Buffer // TODO

    override fun writeUtf8CodePoint(codePoint: Int): Buffer // TODO

    override fun writeString(string: String, charset: Charset): Buffer // TODO

    override fun writeString(
        string: String,
        beginIndex: Int,
        endIndex: Int,
        charset: Charset
    ): Buffer // TODO

    override fun write(source: ByteArray): Buffer // TODO

    override fun write(source: ByteArray, offset: Int, byteCount: Int): Buffer // TODO

    override fun write(source: ByteBuffer): Int // TODO

    override fun writeAll(source: Source): Long // TODO

    override fun write(source: Source, byteCount: Long): Buffer // TODO

    override fun writeByte(b: Int): Buffer // TODO

    override fun writeShort(s: Int): Buffer // TODO

    override fun writeShortLe(s: Int): Buffer // TODO

    override fun writeInt(i: Int): Buffer // TODO

    override fun writeIntLe(i: Int): Buffer // TODO

    override fun writeLong(v: Long): Buffer // TODO

    override fun writeLongLe(v: Long): Buffer // TODO

    override fun writeDecimalLong(v: Long): Buffer // TODO

    override fun writeHexadecimalUnsignedLong(v: Long): Buffer // TODO

    fun writableSegment(minimumCapacity: Int): Segment // TODO

    override fun write(source: Buffer, byteCount: Long) // TODO

    override fun read(sink: Buffer, byteCount: Long): Long // TODO

    override fun indexOf(b: Byte): Long // TODO

    fun indexOf(b: Byte, fromIndex: Long): Long // TODO

    fun indexOf(b: Byte, fromIndex: Long, toIndex: Long): Long // TODO

    fun indexOf(bytes: ByteString): Long // TODO

    fun indexOf(bytes: ByteString, fromIndex: Long): Long // TODO

    fun indexOfElement(targetBytes: ByteString): Long // TODO

    fun indexOfElement(targetBytes: ByteString, fromIndex: Long): Long // TODO

    fun rangeEquals(offset: Long, bytes: ByteString): Boolean // TODO

    fun rangeEquals(
        offset: Long,
        bytes: ByteString,
        bytesOffset: Int,
        byteCount: Int
    ): Boolean // TODO

    override fun flush() // TODO

    override fun isOpen(): Boolean // TODO

    override fun close() // TODO

    override fun timeout(): Timeout // TODO

    fun md5(): ByteString // TODO

    fun sha1(): ByteString // TODO

    fun sha256(): ByteString // TODO

    fun sha512(): ByteString // TODO

    fun hmacSha1(key: ByteString): ByteString // TODO

    fun hmacSha256(key: ByteString): ByteString // TODO

    fun hmacSha512(key: ByteString): ByteString // TODO

    override fun equals(other: Any?): Boolean // TODO

    override fun hashCode(): Int // TODO

    override fun toString(): String // TODO

    fun copy(): Buffer // TODO

    fun clone(): Buffer // TODO

    fun snapshot(): ByteString // TODO

    fun snapshot(byteCount: Int): ByteString // TODO

    fun readUnsafe(unsafeCursor: UnsafeCursor): UnsafeCursor // TODO

    fun readAndWriteUnsafe(unsafeCursor: UnsafeCursor): UnsafeCursor // TODO

    class UnsafeCursor : Closeable { // TODO

        fun next(): Int {
            TODO()
        }

        override fun close() {
            TODO("Not yet implemented")
        }
    }
}