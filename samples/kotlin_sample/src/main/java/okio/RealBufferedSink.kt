/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
class RealBufferedSink constructor(
    @JvmField val sink: Sink,
) : BufferedSink {
    @JvmField
    val bufferField: Buffer = TODO()

    @JvmField
    var closed: Boolean = false

    @Suppress("OVERRIDE_BY_INLINE") // Prevent internal code from calling the getter.
    override val buffer: Buffer
        inline get() = bufferField

    fun buffer(): Buffer = bufferField

    override fun write(source: Buffer, byteCount: Long) = TODO()
    override fun write(byteString: ByteString) = TODO()
    override fun write(byteString: ByteString, offset: Int, byteCount: Int) = TODO()
    override fun writeUtf8(string: String) = TODO()
    override fun writeUtf8(string: String, beginIndex: Int, endIndex: Int) = TODO()

    override fun writeUtf8CodePoint(codePoint: Int) = TODO()

    override fun writeString(string: String, charset: Charset): BufferedSink {
        check(!closed) { "closed" }
        buffer.writeString(string, charset)
        return emitCompleteSegments()
    }

    override fun writeString(
        string: String,
        beginIndex: Int,
        endIndex: Int,
        charset: Charset,
    ): BufferedSink {
        check(!closed) { "closed" }
        buffer.writeString(string, beginIndex, endIndex, charset)
        return emitCompleteSegments()
    }

    override fun write(source: ByteArray) = TODO()
    override fun write(source: ByteArray, offset: Int, byteCount: Int) = TODO()

    override fun write(source: ByteBuffer): Int {
        check(!closed) { "closed" }
        val result = buffer.write(source)
        emitCompleteSegments()
        return result
    }

    override fun writeAll(source: Source) = TODO()
    override fun write(source: Source, byteCount: Long): BufferedSink = TODO()
    override fun writeByte(b: Int) = TODO()
    override fun writeShort(s: Int) = TODO()
    override fun writeShortLe(s: Int) = TODO()
    override fun writeInt(i: Int) = TODO()
    override fun writeIntLe(i: Int) = TODO()
    override fun writeLong(v: Long) = TODO()
    override fun writeLongLe(v: Long) = TODO()
    override fun writeDecimalLong(v: Long) = TODO()
    override fun writeHexadecimalUnsignedLong(v: Long) = TODO()
    override fun emitCompleteSegments() = TODO()
    override fun emit() = TODO()

    override fun outputStream(): OutputStream {
        return object : OutputStream() {
            override fun write(b: Int) {
                if (closed) throw IOException("closed")
                buffer.writeByte(b.toByte().toInt())
                emitCompleteSegments()
            }

            override fun write(data: ByteArray, offset: Int, byteCount: Int) {
                if (closed) throw IOException("closed")
                buffer.write(data, offset, byteCount)
                emitCompleteSegments()
            }

            override fun flush() {
                // For backwards compatibility, a flush() on a closed stream is a no-op.
                if (!closed) {
                    this@RealBufferedSink.flush()
                }
            }

            override fun close() = this@RealBufferedSink.close()

            override fun toString() = "${this@RealBufferedSink}.outputStream()"
        }
    }

    override fun flush() = TODO()

    override fun isOpen() = !closed

    override fun close() = TODO()
    override fun timeout() = TODO()
    override fun toString() = TODO()
}
