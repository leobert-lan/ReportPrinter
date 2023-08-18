package okio

import java.io.IOException

abstract class FileSystem {

    @Throws(IOException::class)
    abstract fun canonicalize(path: Path): Path

    @Throws(IOException::class)
    fun metadata(path: Path): FileMetadata {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun metadataOrNull(path: Path): FileMetadata?

    @Throws(IOException::class)
    fun exists(path: Path): Boolean {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun list(dir: Path): List<Path>

    abstract fun listOrNull(dir: Path): List<Path>?

    open fun listRecursively(dir: Path, followSymlinks: Boolean): Sequence<Path> {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun openReadOnly(file: Path): FileHandle

    @Throws(IOException::class)
    abstract fun openReadWrite(file: Path, mustCreate: Boolean, mustExist: Boolean): FileHandle

    @Throws(IOException::class)
    abstract fun source(file: Path): Source

    @Throws(IOException::class)
    inline fun <T> read(file: Path, readerAction: BufferedSource.() -> T): T {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun sink(file: Path, mustCreate: Boolean): Sink

    @Throws(IOException::class)
    inline fun <T> write(file: Path, mustCreate: Boolean, writerAction: BufferedSink.() -> T): T {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun appendingSink(file: Path, mustExist: Boolean): Sink

    @Throws(IOException::class)
    abstract fun createDirectory(dir: Path, mustCreate: Boolean)

    @Throws(IOException::class)
    fun createDirectories(dir: Path, mustCreate: Boolean) {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun atomicMove(source: Path, target: Path)

    open fun copy(source: Path, target: Path) {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun delete(path: Path, mustExist: Boolean)

    @Throws(IOException::class)
    open fun deleteRecursively(fileOrDirectory: Path, mustExist: Boolean) {
        TODO()
    }

    @Throws(IOException::class)
    abstract fun createSymlink(source: Path, target: Path)



}