package utils

import com.eny.i18n.vue.plugin.utils.isQuoted
import com.eny.i18n.vue.plugin.utils.unQuote
import org.junit.Assert.*
import org.junit.jupiter.api.Test

//@Ignore
internal class StringUtilsTest : TestBase {

    @Test
    fun matchPattern() {
        val str = "startsWithEndsWithElse"
        val notMatch = "doesNotstartWithEndsWithElse2"
        val regex = Regex("starts.*")
        assertTrue(str.matches(regex))
        assertFalse(notMatch.matches(regex))
    }

    @Test
    fun matchPattern2() {
        val str = "startsWith1EndsWithElse"
        val notMatch = "doesNotstartWithEndsWithElse3"
        val regex = Regex(".*Else")
        assertTrue(str.matches(regex))
        assertFalse(notMatch.matches(regex))
    }

    @Test
    fun isQuoted() {
        listOf("'", "`", "\"").forEach {
            assertFalse("${it}sub".isQuoted())
            assertFalse("sub${it}'".isQuoted())
            assertTrue("${it}subr${it}".isQuoted())
        }
        assertFalse("u".isQuoted())
    }

    @Test
    fun unQuote() {
        listOf("'", "`", "\"").forEach {
            val left = "${it}subs"
            assertEquals(left.unQuote(), left)
            val right = "subs${it}"
            assertEquals(right.unQuote(), right)
            val wrapped = "${it}subs${it}"
            assertEquals(wrapped.unQuote(), "subs")
        }
    }

    @Test
    fun doubleUnQuote() {
        listOf("'", "`", "\"").forEach {
            val wrapped = "\"${it}'`subs`'${it}\""
            assertEquals(wrapped.unQuote(), "${it}'`subs`'${it}")
        }
    }
}