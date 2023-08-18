package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs
import java.security.MessageDigest

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSource
class HashingSource : ForwardingSource, Source {
    constructor(delegate: Source) : super(delegate)


    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO()
    }

    @get:JvmName("hash")
    val hash: ByteString
        get() {
            TODO()
        }

    companion object {

        @JvmStatic
        fun md5(source: Source): HashingSource {
            TODO()
        }

        @JvmStatic
        fun sha1(source: Source): HashingSource {
            TODO()
        }

        @JvmStatic
        fun sha256(source: Source): HashingSource {
            TODO()
        }

        @JvmStatic
        fun sha512(source: Source): HashingSource {
            TODO()
        }

        @JvmStatic
        fun hmacSha1(source: Source, key: ByteString): HashingSource {
            TODO()
        }

        @JvmStatic
        fun hmacSha256(source: Source, key: ByteString): HashingSource {
            TODO()
        }

        @JvmStatic
        fun hmacSha512(source: Source, key: ByteString): HashingSource {
            TODO()
        }

    }

}