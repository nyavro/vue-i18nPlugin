package com.eny.i18n.vue.plugin.ide.hint

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVue
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager

class HintTestVue: PlatformBaseTest() {

    fun testSingleHint() = myFixture.runVue {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        val translation = "translation here"
        myFixture.addFileToProject("locales/en-US.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"root.first.<caret>second\""))
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            assertEquals(
                translation,
                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
            )
        }
    }

    fun testSingleHintFirstComponentNs() = myFixture.runVueConfig(Config(firstComponentNs = true)) {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        val translation = "translation here"
        myFixture.addFileToProject("locales/en-US/test.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test.root.first.<caret>second\""))
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            assertEquals(
                translation,
                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
            )
        }
    }
}
