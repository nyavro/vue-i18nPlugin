package com.eny.i18n.vue.plugin.ide.quickfix

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.utils.generator.code.JsCodeGenerator
import org.junit.Test

class CreateKeyTest: PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    @Test
    fun testCreateKeyEmptyJson() {
        val cg = JsCodeGenerator()
        val hint = "Create i18n key"
        val translationFileName = "locales/en-US.json"
        myFixture.addFileToProject(
                translationFileName,
                "{}"
        )
        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"ref.section.mi<caret>ssing\""))
        val action = myFixture.filterAvailableIntentions(hint).find {it.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResult(
                translationFileName,
                """
                    {
                      "ref": {
                        "section": {
                          "missing": "ref.section.missing"
                        }
                      }
                    }
                """.trimIndent(),
                false
        )
    }

//
//    @ParameterizedTest
//    @ArgumentsSource(TranslationGenerators::class)
//    fun createKeyMultipleTranslationsVue(tg: TranslationGenerator) = myFixture.runVue {
//        val cg = VueCodeGenerator()
//        myFixture.addFileToProject(
//            "locales/en-US.${tg.ext()}",
//            contentEn(tg)
//        )
//        myFixture.addFileToProject(
//            "locales/ru-RU.${tg.ext()}",
//            contentRu(tg)
//        )
//        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"ref.section.mi<caret>ssing\""))
//        var action: IntentionAction? = null
//
//            action = myFixture.findSingleIntention("Create i18n key in all translation files")
//            assertNotNull(action)
//
//        myFixture.launchAction(action!!)
//        read {
//            myFixture.checkResult(
//                "locales/en-US.${tg.ext()}",
//                tg.generateNamedBlock(
//                    "ref",
//                    expectedEn(tg)
//                ),
//                false
//            )
//            myFixture.checkResult(
//                "locales/ru-RU.${tg.ext()}",
//                tg.generateNamedBlock(
//                    "ref",
//                    expectedRu(tg)
//                ),
//                false
//            )
//        }
//    }

}