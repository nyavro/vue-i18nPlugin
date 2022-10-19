package com.eny.i18n.vue.plugin.ide.actions

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVue
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.intellij.codeInsight.intention.IntentionAction
import org.junit.Test

class KeyContextVueTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/actions/context"

    @Test
    fun testKeyContextScriptVue() {
        myFixture.configureByFile("keyContextScriptVue/simple.vue")
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }

    @Test
    fun testKeyContextVue() {
        myFixture.configureByFile("keyContextVue/simple.vue")
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }
}