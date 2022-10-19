package com.eny.i18n.vue.plugin.ide.actions

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
import com.intellij.openapi.ui.TestDialogManager.setTestDialog
import com.intellij.openapi.ui.TestInputDialog
import org.junit.jupiter.api.Test

class ExtractionCancellationMigrated: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/actions/cancellation"

    @Test
    fun testCancel() {
        myFixture.configureByFiles("cancel/simple.js", "cancel/locales/en-US.json")
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = null
        })
        myFixture.launchAction(action)
        myFixture.checkResultByFile("cancel/simple.js")
    }

    @Test
    fun testCancelInvalid() {
        myFixture.configureByFiles("cancel/simple.js", "cancel/locales/en-US.json")
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = "not:a:key{here}"
        })
        setTestDialog(object : TestDialog {
            override fun show(message: String): Int {
                assertEquals("Invalid i18n key", message)
                return 1
            }
        })
        myFixture.launchAction(action)
        myFixture.checkResultByFile("cancel/simple.js")
    }
}