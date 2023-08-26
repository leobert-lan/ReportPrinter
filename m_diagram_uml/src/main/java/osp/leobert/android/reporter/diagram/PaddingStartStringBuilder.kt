package osp.leobert.android.reporter.diagram

/**
 * Classname: PaddingStartStringBuilder </p>
 * Created by Leobert on 2023/8/18.
 */
class PaddingStartStringBuilder(
    private val padding: Int,
    private val real: StringBuilder
) : Appendable {

    private val RETURN: String = try {
        System.getProperty("line.separator")
    } catch (ignore: Exception) {
        "\r\n"
    }

    private fun appendPaddingStart() {
        if (padding > 0 && (real.isEmpty() || real.endsWith(RETURN))) {
            repeat(padding) {
                real.append(" ")
            }
        }
    }

    override fun append(p0: CharSequence?): Appendable {
        appendPaddingStart()
        real.append(p0)
        return this
    }

    override fun append(p0: CharSequence?, p1: Int, p2: Int): Appendable {
        appendPaddingStart()
        real.append(p0, p1, p2)
        return this
    }

    override fun append(p0: Char): Appendable {
        appendPaddingStart()
        real.append(p0)
        return this
    }

    override fun toString(): String {
        return real.toString()
    }


}