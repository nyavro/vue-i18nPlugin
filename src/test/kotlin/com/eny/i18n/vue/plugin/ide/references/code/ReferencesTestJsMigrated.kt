package com.eny.i18n.vue.plugin.ide.references.code

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ReferencesTestJsMigrated : PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/references/code/jscode"

    @Test
    fun testReference() {
        myFixture.configureByFiles("reference/simple.js", "reference/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.size > 0)
            assertEquals("Reference in json", element.references[0].resolve()?.text?.unQuote())
        }
    }

    @Test
    fun testMultiReference() {
        myFixture.configureByFiles("multiReference/simple.js", "multiReference/locales/en-US.json", "multiReference/locales/de-DE.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
        }
    }

    private fun getResolvedValues(element: PsiElement?): Set<String> {
        return (element?.reference as? I18nReference)?.references?.mapNotNull { it.reference.element?.value()?.text?.unQuote() }?.toSet() ?: emptySet()
    }

    @Test
    fun testPartiallyResolved() {
        myFixture.configureByFiles("partiallyResolved/simple.js", "partiallyResolved/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.size > 0)
            assertEquals("section", element.references[0].resolve()?.text?.unQuote())
        }
    }

    @Test
    fun testExpressionReference() {
        myFixture.configureByFiles("expressionReference/simple.js", "expressionReference/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.size > 0)
            assertEquals("section", element.references[0].resolve()?.text?.unQuote())
        }
    }

    @Test
    @Disabled
    fun testExpressionMultiKeys() {
        myFixture.configureByFiles("expressionMultiKeys/simple.js", "expressionMultiKeys/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.size > 0)
            val ref = element.references[0].resolve()
        }
    }

    @Test
    fun testInvalidTranslationRoot() {
        myFixture.configureByFiles("invalidTranslationRoot/simple.js", "invalidTranslationRoot/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertEmpty(element!!.references)
        }
    }

    @Test
    fun testInvalidTranslationValue() {
        myFixture.configureByFiles("invalidTranslationValue/simple.js", "invalidTranslationValue/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
        }
    }

    @Test
    fun testRootKey() {
        myFixture.configureByFiles("rootKey/simple.js", "rootKey/locales/en-US.json")
        read {
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue( element!!.references.size > 0)
            assertEquals("Reference in json", element.references[0].resolve()?.text?.unQuote())
        }
    }
}