package com.eny.i18n.vue.plugin.utils

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class LocalizationSourceSearchTest: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/utils"
    }

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    @Test
    fun testFailWhenFolderInsideTranslations() {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.runVueConfig(testConfig) {
            myFixture.copyDirectoryToProject("test", "assets/test")
            myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5"))
            myFixture.configureByText("refToObject.${cg.ext()}", cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""))
            myFixture.checkHighlighting(true, true, true, true)
        }
    }
}