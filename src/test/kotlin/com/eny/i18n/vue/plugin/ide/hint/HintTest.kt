package com.eny.i18n.vue.plugin.ide.hint

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVue
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.jupiter.api.Test

class HintTest: PlatformBaseTest() {

    @Test
    fun vueSfcHint() = myFixture.runVue {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.configureByText(
            "App.vue",
            cg.generateSfcBlock(
                "<p>{{ \$t('root.first.<caret>second')}}</p>",
                tg.generateNamedBlock("en", tg.generateContent("root", "first", "second", "translation here"))
            )
        )
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            //Not supported for Vue SFC:
            assertNull(
                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
            )
        }
    }
}
