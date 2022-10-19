package com.eny.i18n.vue.plugin.ide.references.code

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.vue.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Test

class ReferencesTestVueMigrated: PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/references/code/vue"

    val tg = JsonTranslationGenerator()

    @Test
    fun testMultiReference() {
        myFixture.configureByFiles("multiReference/simple.vue", "multiReference/locales/en-US.json", "multiReference/locales/de-DE.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
        }
    }

    @Test
    fun testRootKey() {
        myFixture.configureByFiles("rootKey/simple.vue", "rootKey/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.size > 0)
            assertEquals("Reference in json", element.references[0].resolve()?.text?.unQuote())
        }
    }

    private fun getResolvedValues(element: PsiElement?) =
            (element?.reference as I18nReference).references.map { it.reference.element?.value()?.text?.unQuote() }.toSet()
}