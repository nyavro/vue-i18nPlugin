package com.eny.i18n.vue.plugin.ide.actions

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
import com.intellij.openapi.ui.TestInputDialog
import org.junit.jupiter.api.Test

class VueTranslationGenerationTest: PlatformBaseTest() {
    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/actions/generation"

    protected fun config(ext: String, extractSorted: Boolean = false) =
        Config(yamlContentGenerationEnabled = ext == "yml",
            jsonContentGenerationEnabled = ext == "json",
            preferYamlFilesGeneration = ext == "yml",
            extractSorted = extractSorted
        )

    protected fun predefinedTextInputDialog(newKey: String): TestInputDialog {
        return object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?): String {
                return newKey
            }
        }
    }

    @Test
    fun testTranslationFileGenerationVue() {
        myFixture.tempDirFixture.findOrCreateDir("translationFileGenerationVue/locales")
        myFixture.configureByFile("translationFileGenerationVue/simple.vue")
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResultByFile("translationFileGenerationVue/simple-patched.vue")
        myFixture.checkResultByFile(
            "translationFileGenerationVue/locales/en.json",
            "translationFileGenerationVue/locales/en-US-generated.json",
            false
        )
    }

    @Test
    fun testTranslationFileGenerationVueTs() = myFixture.runVueConfig(config("json")) {
        myFixture.tempDirFixture.findOrCreateDir("translationFileGenerationVueTs/locales")
        myFixture.configureByFile("translationFileGenerationVueTs/simple.vue")
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResultByFile("translationFileGenerationVueTs/simple-patched.vue")
        myFixture.checkResultByFile(
            "translationFileGenerationVueTs/locales/en.json",
            "translationFileGenerationVueTs/locales/en-US-generated.json",
            false
        )
    }

    @Test
    fun testTranslationFileGenerationVueAttributeFix() = myFixture.runVueConfig(config("json")) {
        myFixture.tempDirFixture.findOrCreateDir("translationFileGenerationVueAttributeFix/locales")
        myFixture.configureByFile("translationFileGenerationVueAttributeFix/simple.vue")
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResultByFile("translationFileGenerationVueAttributeFix/simple-patched.vue")
        myFixture.checkResultByFile(
            "translationFileGenerationVueAttributeFix/locales/en.json",
            "translationFileGenerationVueTs/locales/en-US-generated.json",
            false
        )
    }
}