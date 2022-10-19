package com.eny.i18n.vue.plugin.ide.actions

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runWithConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestDialogManager.setTestDialog
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
import com.intellij.openapi.ui.TestInputDialog
import org.junit.jupiter.api.Test

class ExtractI18nIntentionActionMigrated: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/actions/extraction"

    @Test
    fun testKeyExtraction() = runTestCase(
        "keyExtraction",
        "js",
        predefinedTextInputDialog("ref.dvalue3")
    )

    @Test
    fun testKeyExtractionSortedFirst() = myFixture.runWithConfig(Config(extractSorted = true)) {
        runTestCase(
            "keyExtractionSortedFirst",
            "js",
            predefinedTextInputDialog("ref.dvalue3")
        )
    }

    @Test
    fun testKeyExtractionSortedMiddle() = myFixture.runWithConfig(Config(extractSorted = true)) {
        runTestCase(
            "keyExtractionSortedMiddle",
            "js",
            predefinedTextInputDialog("ref.mkey")
        )
    }

    @Test
    fun testKeyExtractionSortedEnd() = myFixture.runWithConfig(Config(extractSorted = true)) {
        runTestCase(
            "keyExtractionSortedEnd",
            "js",
            predefinedTextInputDialog("ref.zkey")
        )
    }

    @Test
    fun testRightBorderKeyExtraction() = runTestCase(
        "rightBorderKeyExtraction",
        "js",
        predefinedTextInputDialog("ref.value3")
    )

    @Test
    fun testRootSource() = runTestCase(
        "rootSource",
        "js",
        predefinedTextInputDialog("ref.value3")
    )

    @Test
    fun testKeyExtractionVue() = runTestCase(
        "keyExtractionVue",
        "vue",
        predefinedTextInputDialog("ref.value3")
    )

    @Test
    fun testKeyExtractionVueTemplate() = runTestCase(
        "keyExtractionVueTemplate",
        "vue",
        predefinedTextInputDialog("ref.value3")
    )

    @Test
    fun testScriptExtractionVue() = runTestCase(
        "scriptExtractionVue",
        "vue",
        predefinedTextInputDialog("ref.value3")
    )

    private fun runTestCase(
        name: String,
        ext: String,
        inputDialog: TestInputDialog,
        message: TestDialog? = null
    ) {
        myFixture.configureByFiles("$name/simple.$ext", "$name/locales/en-US.json")
        if (!ApplicationManager.getApplication().isReadAccessAllowed()) return
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(inputDialog)
        if (message != null) setTestDialog(message)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("$name/locales/en-US.json", "$name/locales/en-US-patched.json", false)
        myFixture.checkResultByFile("$name/simple-patched.$ext")
    }

    private fun predefinedTextInputDialog(newKey: String): TestInputDialog {
        return object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?): String {
                return newKey
            }
        }
    }
}