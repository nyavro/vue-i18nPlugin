package com.eny.i18n.vue.plugin.ide.inspections

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.jupiter.api.Test

class VueSfcHighlightingTest : PlatformBaseTest() {

    private val codeGenerator = VueCodeGenerator()

    private val tg = JsonTranslationGenerator()

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private fun check(fileName: String, code: String, translationName: String, translation: String) {
        myFixture.runVueConfig(testConfig) {
            myFixture.addFileToProject(translationName, translation)
            myFixture.configureByText(fileName, code)
            myFixture.checkHighlighting(true, true, true, true)
        }
    }

    @Test
    fun sfcReferenceResolved() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                        mapOf(
                            Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
                            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                        ),
                        "\"<info descr=\"null\">tst3.sfc.key</info>\""
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcReferenceToObject() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                        mapOf(
                            Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
                            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                        ),
                        "\"<warning descr=\"Reference to object\">tst3.sfc</warning>\""
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcUnresolvedKey() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                        mapOf(
                            Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
                            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                        ),
                        "\"tst3.sfc.<warning descr=\"Unresolved key\">km</warning>\""
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcExpressionInsideTranslation() {
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                        mapOf(
                            Pair("en", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")),
                            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                        ),
                        "isSelected ? \"<warning descr=\"Reference to object\">tst2.plurals</warning>\" : isAbsent ? \"<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\" : \"<info descr=\"null\">tst3.sfc.key</info>\""
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }
    }
}
