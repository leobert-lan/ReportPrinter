package okio

import java.io.Serializable
import java.nio.charset.Charset

open class ByteString : Serializable, Comparable<ByteString> {

    constructor(data: ByteArray)

    fun utf8(): String {
        TODO()
    }

    fun string(charset: Charset): String {
        TODO()
    }

    fun base64(): String {
        TODO()
    }

    fun md5(): ByteString {
        TODO()
    }

    fun sha1(): ByteString {
        TODO()
    }

    fun sha256(): ByteString {
        TODO()
    }

    fun sha512(): ByteString {
        TODO()
    }

    fun digest(algorithm: String): ByteString {
        TODO()
    }

    fun hmacSha1(key: ByteString): ByteString {
        TODO()
    }

    fun hmacSha256(key: ByteString): ByteString {
        TODO()
    }

    fun hmacSha512(key: ByteString): ByteString {
        TODO()
    }

    fun hmac(algorithm: String, key: ByteString): ByteString {
        TODO()
    }

    // 其他方法同样调整为 fun形式


    override fun compareTo(other: ByteString): Int {
        TODO("Not yet implemented")
    }

}