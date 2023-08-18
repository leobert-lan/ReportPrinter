package okio

import java.io.RandomAccessFile

internal class JvmFileHandle(
    readWrite: Boolean,
    private val randomAccessFile: RandomAccessFile,
) : FileHandle(readWrite) {

    @Synchronized
    override fun protectedResize(size: Long) {
       TODO()
    }

    @Synchronized
    override fun protectedSize(): Long {
        TODO()
    }

    @Synchronized
    override fun protectedRead(
        fileOffset: Long,
        array: ByteArray,
        arrayOffset: Int,
        byteCount: Int,
    ): Int {
        TODO()
    }

    @Synchronized
    override fun protectedWrite(
        fileOffset: Long,
        array: ByteArray,
        arrayOffset: Int,
        byteCount: Int,
    ) {
        TODO()
    }

    @Synchronized
    override fun protectedFlush() {
    }

    @Synchronized
    override fun protectedClose() {
    }
}