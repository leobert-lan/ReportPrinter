package okio


import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.io.*
import java.net.Socket
import java.net.SocketTimeoutException
import java.nio.file.Files
import java.nio.file.OpenOption
import java.security.MessageDigest
import java.util.logging.Level
import java.util.logging.Logger
import javax.crypto.Cipher
import javax.crypto.Mac
import java.nio.file.Path as NioPath

/** Returns a sink that writes to `out`. */
fun OutputStream.sink(): Sink = OutputStreamSink(this, Timeout())

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
private class OutputStreamSink(
    private val out: OutputStream,
    private val timeout: Timeout,
) : Sink {

    override fun write(source: Buffer, byteCount: Long) {

    }

    override fun flush() = out.flush()

    override fun close() = out.close()

    override fun timeout() = timeout

    override fun toString() = "sink($out)"
}

/** Returns a source that reads from `in`. */
fun InputStream.source(): Source = InputStreamSource(this, Timeout())

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
private open class InputStreamSource(
    private val input: InputStream,
    private val timeout: Timeout,
) : Source {

    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    override fun close() = input.close()

    override fun timeout() = timeout

    override fun toString() = "source($input)"
}

/**
 * Returns a sink that writes to `socket`. Prefer this over [sink]
 * because this method honors timeouts. When the socket
 * write times out, the socket is asynchronously closed by a watchdog thread.
 */
@Throws(IOException::class)
fun Socket.sink(): Sink {
    val timeout = SocketAsyncTimeout(this)
    val sink = OutputStreamSink(getOutputStream(), timeout)
    return timeout.sink(sink)
}

/**
 * Returns a source that reads from `socket`. Prefer this over [source]
 * because this method honors timeouts. When the socket
 * read times out, the socket is asynchronously closed by a watchdog thread.
 */
@Throws(IOException::class)
fun Socket.source(): Source {
    val timeout = SocketAsyncTimeout(this)
    val source = InputStreamSource(getInputStream(), timeout)
    return timeout.source(source)
}

private val logger = Logger.getLogger("okio.Okio")

@Graphs.Okio
@GenerateClassDiagram
private class SocketAsyncTimeout(private val socket: Socket) : AsyncTimeout() {
    override fun newTimeoutException(cause: IOException?): IOException {
        val ioe = SocketTimeoutException("timeout")
        if (cause != null) {
            ioe.initCause(cause)
        }
        return ioe
    }

    override fun timedOut() {
        try {
            socket.close()
        } catch (e: Exception) {
            logger.log(Level.WARNING, "Failed to close timed out socket $socket", e)
        } catch (e: AssertionError) {
            if (e.isAndroidGetsocknameError) {
                // Catch this exception due to a Firmware issue up to android 4.2.2
                // https://code.google.com/p/android/issues/detail?id=54072
                logger.log(Level.WARNING, "Failed to close timed out socket $socket", e)
            } else {
                throw e
            }
        }
    }
}

/** Returns a sink that writes to `file`. */
@JvmOverloads
@Throws(FileNotFoundException::class)
fun File.sink(append: Boolean = false): Sink = FileOutputStream(this, append).sink()

/** Returns a sink that writes to `file`. */
@Throws(FileNotFoundException::class)
fun File.appendingSink(): Sink = TODO()

/** Returns a source that reads from `file`. */
@Throws(FileNotFoundException::class)
fun File.source(): Source = TODO()

/** Returns a sink that writes to `path`. */
@Throws(IOException::class)
fun NioPath.sink(vararg options: OpenOption): Sink =
    Files.newOutputStream(this, *options).sink()

/** Returns a source that reads from `path`. */
@Throws(IOException::class)
fun NioPath.source(vararg options: OpenOption): Source =
    Files.newInputStream(this, *options).source()

/**
 * Returns a sink that uses [cipher] to encrypt or decrypt [this].
 *
 * @throws IllegalArgumentException if [cipher] isn't a block cipher.
 */
fun Sink.cipherSink(cipher: Cipher): CipherSink = TODO()

/**
 * Returns a source that uses [cipher] to encrypt or decrypt [this].
 *
 * @throws IllegalArgumentException if [cipher] isn't a block cipher.
 */
fun Source.cipherSource(cipher: Cipher): CipherSource = TODO()

/**
 * Returns a sink that uses [mac] to hash [this].
 */
fun Sink.hashingSink(mac: Mac): HashingSink = TODO()

/**
 * Returns a source that uses [mac] to hash [this].
 */
fun Source.hashingSource(mac: Mac): HashingSource = TODO()

/**
 * Returns a sink that uses [digest] to hash [this].
 */
fun Sink.hashingSink(digest: MessageDigest): HashingSink = TODO()

/**
 * Returns a source that uses [digest] to hash [this].
 */
fun Source.hashingSource(digest: MessageDigest): HashingSource = TODO()

@Throws(IOException::class)
fun FileSystem.openZip(zipPath: Path): FileSystem = TODO()

fun ClassLoader.asResourceFileSystem(): FileSystem = TODO()

/**
 * Returns true if this error is due to a firmware bug fixed after Android 4.2.2.
 * https://code.google.com/p/android/issues/detail?id=54072
 */
internal val AssertionError.isAndroidGetsocknameError: Boolean
    get() {
        return cause != null && message?.contains("getsockname failed") ?: false
    }
