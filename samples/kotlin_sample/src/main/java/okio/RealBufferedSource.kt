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
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
internal class RealBufferedSource constructor(
    @JvmField val source: Source,
) : BufferedSource {
    override val buffer: Buffer
        get() = TODO("Not yet implemented")

    override fun exhausted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun require(byteCount: Long) {
        TODO("Not yet implemented")
    }

    override fun request(byteCount: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun readByte(): Byte {
        TODO("Not yet implemented")
    }

    override fun readString(charset: Charset): String {
        TODO("Not yet implemented")
    }

    override fun readString(byteCount: Long, charset: Charset): String {
        TODO("Not yet implemented")
    }

    override fun indexOf(b: Byte): Long {
        TODO("Not yet implemented")
    }

    override fun inputStream(): InputStream {
        TODO("Not yet implemented")
    }

    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO("Not yet implemented")
    }

    override fun read(p0: ByteBuffer?): Int {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun isOpen(): Boolean {
        TODO("Not yet implemented")
    }

}
