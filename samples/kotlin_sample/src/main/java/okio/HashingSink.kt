package okio

import osp.leobert.android.reporter.diagram.notation.GenerateClassDiagram
import osp.leobert.uml.io.Graphs

@Graphs.Okio
@GenerateClassDiagram
@Graphs.NsSink
class HashingSink(delegate: Sink) : ForwardingSink(delegate), Sink {



    override fun write(source: Buffer, byteCount: Long) {
        TODO()
    }

    @get:JvmName("hash")
    val hash: ByteString
        get() {
            TODO()
        }

    companion object {

        @JvmStatic
        fun md5(sink: Sink): HashingSink {
            TODO()
        }

        @JvmStatic
        fun sha1(sink: Sink): HashingSink {
            TODO()
        }

        @JvmStatic
        fun sha256(sink: Sink): HashingSink {
            TODO()
        }

        @JvmStatic
        fun sha512(sink: Sink): HashingSink {
            TODO()
        }

        @JvmStatic
        fun hmacSha1(sink: Sink, key: ByteString): HashingSink {
            TODO()
        }

        @JvmStatic
        fun hmacSha256(sink: Sink, key: ByteString): HashingSink {
            TODO()
        }

        @JvmStatic
        fun hmacSha512(sink: Sink, key: ByteString): HashingSink {
            TODO()
        }

    }

}