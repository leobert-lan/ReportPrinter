/*
 * Copyright (C) 2020 Square, Inc.
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

import java.io.File
import java.nio.file.Path as NioPath

class Path internal  constructor(
  internal  val bytes: ByteString,
) : Comparable<Path> {
   val root: Path?
    get() = TODO()

   val segments: List<String>
    get() = TODO()

   val segmentsBytes: List<ByteString>
    get() = TODO()

   val isAbsolute: Boolean
    get() = TODO()

   val isRelative: Boolean
    get() = TODO()

  @get:JvmName("volumeLetter")
   val volumeLetter: Char?
    get() = TODO()

  @get:JvmName("nameBytes")
   val nameBytes: ByteString
    get() = TODO()

  @get:JvmName("name")
   val name: String
    get() = TODO()

  @get:JvmName("parent")
   val parent: Path?
    get() = TODO()

   val isRoot: Boolean
    get() = TODO()

  @JvmName("resolve")
   operator fun div(child: String): Path = TODO()

  @JvmName("resolve")
   operator fun div(child: ByteString): Path = TODO()

  @JvmName("resolve")
   operator fun div(child: Path): Path = TODO()

   fun resolve(child: String, normalize: Boolean): Path = TODO()

   fun resolve(child: ByteString, normalize: Boolean): Path = TODO()

   fun resolve(child: Path, normalize: Boolean): Path = TODO()

   fun relativeTo(other: Path): Path = TODO()

   fun normalized(): Path = TODO()

  fun toFile(): File = TODO()

  // Can only be invoked on platforms that have java.nio.file.
  fun toNioPath(): NioPath = TODO()

   override fun compareTo(other: Path): Int = TODO()

   override fun equals(other: Any?): Boolean = TODO()

   override fun hashCode() = TODO()

   override fun toString() = TODO()

   companion object {
    @JvmField
     val DIRECTORY_SEPARATOR: String = TODO()

    @JvmName("get")
    @JvmStatic
    @JvmOverloads
     fun String.toPath(normalize: Boolean): Path = TODO()

    @JvmName("get")
    @JvmStatic
    @JvmOverloads
    fun File.toOkioPath(normalize: Boolean = false): Path = toString().toPath(normalize)

    @JvmName("get")
    @JvmStatic
    @JvmOverloads
    fun NioPath.toOkioPath(normalize: Boolean = false): Path = toString().toPath(normalize)
  }
}
