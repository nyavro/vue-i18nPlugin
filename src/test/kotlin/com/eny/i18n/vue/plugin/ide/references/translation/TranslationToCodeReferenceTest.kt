package com.eny.i18n.vue.plugin.ide.references.translation

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.runWithConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.utils.generator.code.*
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.vue.plugin.utils.unQuote
import org.junit.Test

private val tgs = listOf(JsonTranslationGenerator())
private val jsCgs = listOf(JsCodeGenerator(), TsCodeGenerator())

class TranslationToCodeTestBase: PlatformBaseTest() {

    @Test
    fun testSingleReference() {
        jsCgs.forEachIndexed { i0, cg ->
            val key = "'test:ref.section.key${i0}'"
            myFixture.configureByText("testReference${i0}.${cg.ext()}", cg.generate(key))
            tgs.forEach { tg ->
                myFixture.configureByText(
                    "test.${tg.ext()}",
                    tg.generateContent("ref", "section", "key<caret>${i0}", "Translation at ref section key")
                )
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertEquals(key.unQuote(), element!!.references[0].resolve()?.text?.unQuote())
            }
        }
    }

    @Test
    fun testInvalidTranslation() {
        tgs.forEach { tg ->
            myFixture.configureByText("invalid.${tg.ext()}", "item<caret> text")
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.isEmpty())
        }
    }

    @Test
    fun testNoReference() {
        tgs.forEach { tg ->
            myFixture.configureByText(
                "test.${tg.ext()}",
                tg.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.isEmpty())
        }
    }

    @Test
    fun testMultipleReferences() {
        jsCgs.forEachIndexed { index, cg ->
            val key = "'multiTest:ref.section.subsection1.key${index}'"
            myFixture.configureByText(
                "multiReference1.${cg.ext()}",
                cg.multiGenerate(
                    key,
                    "skip-multiTest:ref.section.subsection1.key${index}",
                    key
                )
            )
            myFixture.configureByText(
                "multiReference2.${cg.ext()}",
                cg.multiGenerate(
                    key,
                    "skip-multiTest2:ref.section.subsection1.key${index}"
                )
            )
            tgs.forEach { tg ->
                myFixture.configureByText(
                    "multiTest.${tg.ext()}",
                    tg.generateContent("ref", "section", "subsection1", "key<caret>${index}", "Translation")
                )
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                val msg = cg.ext() + "," + tg.ext()
                val ref = element!!.references[0] as? TranslationToCodeReference
                if (ref == null) {
                    fail(msg)
                } else {
                    assertEquals(msg, setOf(key.unQuote()), ref.findRefs().mapNotNull {it.text?.unQuote()}.toSet())
                }
            }
        }
    }

    @Test
    fun testObjectReference() {
        jsCgs.forEachIndexed { index, cg ->
            tgs.forEach { tg ->
                myFixture.configureByText(
                    "testReference.${cg.ext()}",
                    cg.multiGenerate(
                        "'skip-objectRef:ref.section.key1'",
                        "'objectRef${index}:ref.section.key2'",
                        "`skip-objectRef:ref.section.\${key2}`",
                        "'objectRef${index}:ref.section.key1'",
                        "'skip-objectRef:ref.section.key2'",
                        "`objectRef${index}:ref.section.\${key2}`"
                    )
                )
                myFixture.configureByText(
                    "objectRef${index}.${tg.ext()}",
                    tg.generateContent("ref", "section<caret>", "key1", "val 1")
                )
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                val ref = element!!.references[0]
                assertTrue(ref is TranslationToCodeReference)
                val refs = (ref as TranslationToCodeReference).findRefs().map { item -> item.text }.toSet()
                assertEquals(
                    cg.ext() + "," + tg.ext(),
                    setOf("'objectRef${index}:ref.section.key1'", "'objectRef${index}:ref.section.key2'", "`objectRef${index}:ref.section.\${key2}`"),
                    refs
                )
            }
        }
    }

    @Test
    fun testInvalidRange() {
        val tg = JsonTranslationGenerator()
        jsCgs.forEachIndexed { index, cg ->
            myFixture.configureByText(
                "testReference.${cg.ext()}",
                cg.multiGenerate(
                    "'skip-objectRef:ref.section.key1'",
                    "'objectRef${index}:ref.section.key2'",
                    "`skip-objectRef:ref.section.\${key2}`",
                    "'objectRef${index}:ref.section.key1'",
                    "'skip-objectRef:ref.section.key2'",
                    "`objectRef${index}:ref.section.\${key2}`"
                )
            )
            myFixture.configureByText(
                "objectRef${index}.${tg.ext()}",
                "{<caret>\""
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.isEmpty())
        }
    }

    @Test
    fun testDefaultNs() {
        jsCgs.forEachIndexed { index, cg ->
            myFixture.runWithConfig(Config(defaultNs = "Common")) {
                myFixture.configureByText(
                    "testDefaultNs.${cg.ext()}",
                    cg.multiGenerate(
                        "'objectRef:ref.section.key1'",
                        "'ref${index}.section.key2'",
                        "`objectRef:ref.section.\${key3}`",
                        "'ref${index}.section.key1'",
                        "'objectRef:ref.section.key2'",
                        "`ref${index}.section.\${key3}`"
                    )
                )
                tgs.forEach { tg ->
                    myFixture.configureByText(
                        "Common.${tg.ext()}",
                        tg.generateContent("ref${index}", "section<caret>", "key1", "val 1")
                    )
                    val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                    val ref = element!!.references[0]
                    assertTrue(ref is TranslationToCodeReference)
                    val refs = (ref as TranslationToCodeReference).findRefs().map { item -> item.text }.toSet()
                    assertEquals(
                        setOf("'ref${index}.section.key1'", "'ref${index}.section.key2'", "`ref${index}.section.\${key3}`"),
                        refs
                    )
                }
            }
        }
    }

    @Test
    fun testClickOnValue() {
        tgs.forEach { tg ->
            myFixture.configureByText(
                "testValue.${tg.ext()}",
                tg.generateContent("ref", "section", "key", "transla<caret>tion")
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.isEmpty())
        }
    }
}

class VueReferencesTestBase: PlatformBaseTest() {

    private val codeGenerator = VueCodeGenerator()

    @Test
    fun testVue() {
        myFixture.runVueConfig(Config(vueDirectory = "assets")) {
            tgs.forEachIndexed { index, tg ->
                val translation = tg.generateContent("ref${index}", "section<caret>", "key1", "val 1")
                myFixture.configureByText("test${index}.vue",
                    codeGenerator.multiGenerate(
                        "'skip.ref.section.key1'",
                        "'ref${index}.section.key2'",
                        "'drop.ref.section.key3'",
                        "'skpref.section.key4'",
                        "'ref${index}.section.key5'"
                    )
                )
                myFixture.configureFromExistingVirtualFile(
                    myFixture.addFileToProject("assets/en-US.${tg.ext()}", translation).virtualFile
                )
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                val ref = element!!.references[0]
                assertTrue(ref is TranslationToCodeReference)
                assertEquals(
                    setOf("'ref${index}.section.key2'", "'ref${index}.section.key5'"),
                    (ref as TranslationToCodeReference).findRefs().map { it.text }.toSet()
                )
            }
        }
    }

    @Test
    fun testVueIncorrectConfiguration() {
        myFixture.runVueConfig(Config(vueDirectory = "translations")) {
            tgs.forEachIndexed { index, tg ->
                val translation = tg.generateContent("ref${index}", "section<caret>", "key1", "val 1")
                myFixture.configureByText("test.vue",
                    codeGenerator.multiGenerate(
                    "'skip.ref.section.key1'",
                    "'ref${index}.section.key2'",
                    "'drop.ref.section.key3'",
                    "'skpref.section.key4'",
                    "'ref${index}.section.key5'"
                ))
                myFixture.configureFromExistingVirtualFile(
                    myFixture.addFileToProject("assets/en-US.${tg.ext()}", translation).virtualFile
                )
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertTrue(element!!.references.isEmpty())
            }
        }
    }
}
