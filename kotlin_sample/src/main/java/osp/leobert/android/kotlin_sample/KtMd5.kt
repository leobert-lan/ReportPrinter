package osp.leobert.android.kotlin_sample

import android.text.TextUtils
import osp.leobert.android.utils.reporter.Util
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * <p><b>Package:</b> osp.leobert.android.kotlin_sample </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> KtMd5 </p>
 * Created by leobert on 2020/6/3.
 */
class KtMd5 {
    private val TAG = "Md5"

    fun checkMD5(md5: String?, updateFile: File?): Boolean {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
//            L.e(TAG, "MD5 string empty or updateFile null");
            return false
        }
        val calculatedDigest = calculateMD5(updateFile)
                ?: //            Log.e(TAG, "calculatedDigest null");
                return false

//        Log.v(TAG, "Calculated digest: " + calculatedDigest);
//        Log.v(TAG, "Provided digest: " + md5);
        return calculatedDigest.equals(md5, ignoreCase = true)
    }


    @Util(group = Util.UtilGroup.MD5, usage = "文件内容计算md5")
    fun calculateMD5(updateFile: File?): String? {
        val digest: MessageDigest
        digest = try {
            MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
//            Log.e(TAG, "Exception while getting digest", e);
            return null
        }
        val `is`: InputStream
        `is` = try {
            FileInputStream(updateFile)
        } catch (e: FileNotFoundException) {
//            Log.e(TAG, "Exception while getting FileInputStream", e);
            return null
        }
        val buffer = ByteArray(8192)
        var read: Int
        return try {
            while (`is`.read(buffer).also { read = it } > 0) {
                digest.update(buffer, 0, read)
            }
            val md5sum = digest.digest()
            val bigInt = BigInteger(1, md5sum)
            var output = bigInt.toString(16)
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0')
            output
        } catch (e: IOException) {
            throw RuntimeException("Unable to process file for MD5", e)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
//                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }

    @Util(group = Util.UtilGroup.MD5, usage = "字符串计算md5")
    fun calculateMD5(psw: String): String? {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(psw.toByteArray(charset("UTF-8")))
            val encryption = md5.digest()
            val strBuf = StringBuilder()
            for (i in encryption.indices) {
                if (Integer.toHexString(0xff and encryption[i].toInt()).length == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff and encryption[i].toInt()))
                } else {
                    strBuf.append(Integer.toHexString(0xff and encryption[i].toInt()))
                }
            }
            strBuf.toString()
        } catch (e: NoSuchAlgorithmException) {
            ""
        } catch (e: UnsupportedEncodingException) {
            ""
        }
    }
}