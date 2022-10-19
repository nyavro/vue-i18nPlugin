package com.eny.i18n.vue.plugin.ide.quickfix

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runWithConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.utils.PluginBundle
import com.eny.i18n.vue.plugin.utils.generator.code.TsCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class CreateMissingTranslationsTest: PlatformBaseTest() {

    @Test
    fun testUnavailableFix() = myFixture.runWithConfig(Config(partialTranslationInspectionEnabled = true)){
        val hint = PluginBundle.getMessage("quickfix.create.missing.keys")
        val cg = TsCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.addFileToProject("en/test.json",
            tg.generateNamedBlock(
                "root",
                tg.generateNamedBlock(
                    "sub",
                    tg.generateNamedBlock("base", "\"Partially defined translation\""),
                    1
                )
            )
        )
        myFixture.configureByText("sample.${cg.ext()}", cg.generate("'test:root.sub.ba<caret>se'"))
        val action = myFixture.getAllQuickFixes().find {it.text == hint}
        assertNull(action)
    }

    @Test
    fun testCreateMissingTranslations() = myFixture.runWithConfig(Config(partialTranslationInspectionEnabled = true)){
        val hint = PluginBundle.getMessage("quickfix.create.missing.keys")
        val cg = TsCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.addFileToProject("locales/en.json",
            tg.generateNamedBlock(
                "root",
                tg.generateNamedBlock(
                    "sub",
                    tg.generateNamedBlock("base", "\"Partially defined translation\""),
                    1
                )
            )
        )
        myFixture.addFileToProject("locales/ru.json",
            """
                {
                  "root": {
                    "another": "value"
                  }
                }
            """.trimIndent()
        )
        myFixture.configureByText("sample.${cg.ext()}", cg.generate("'root.sub.ba<caret>se'"))
        val action = myFixture.getAllQuickFixes().find {it.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResult(
            "locales/ru.json",
            """
                {
                  "root": {
                    "another": "value",
                    "sub": {
                      "base": "root.sub.base"
                    }
                  }
                }
            """.trimIndent(),
            false
        )
    }
}