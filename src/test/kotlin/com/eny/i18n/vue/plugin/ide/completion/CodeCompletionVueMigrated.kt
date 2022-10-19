package com.eny.i18n.vue.plugin.ide.completion

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test


internal class CodeCompletionVueMigrated : PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/completion/vue"

    val codeGenerator = VueCodeGenerator()
    val translationGenerator = JsonTranslationGenerator()

    //No completion happens
    @Test
    fun testNoCompletion() {
        myFixture.configureByFiles("noCompletion/simple.vue", "noCompletion/locales/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("noCompletion/simple-patched.vue")
    }

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    @Test
    fun testSingle() {
        myFixture.configureByFiles("single/simple.vue", "single/locales/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("single/simple-patched.vue")
    }

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    @Test
    fun testPlural() {
        myFixture.configureByFiles("plural/simple.vue", "plural/locales/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("plural/simple-patched.vue")
    }

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    @Test
    fun testPartial() {
        myFixture.configureByFiles("partial/simple.vue", "partial/locales/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("partial/simple-patched.vue")
    }

    @Test
    fun testInvalidCompletion() {
        myFixture.configureByFiles("invalid/simple.vue", "invalid/locales/en-US.json")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("invalid/simple-patched.vue")
    }

    @Test
    fun testEmptyKeyCompletion() {
        myFixture.configureByFiles("emptyKey/simple.vue", "emptyKey/locales/en-US.json")
        assertTrue(myFixture.completeBasic().find {it.lookupString == "tstw"} != null)
    }

    @Test
    fun testRootKeyCompletion() {
        myFixture.configureByFiles("rootKey/simple.vue", "rootKey/locales/en-US.json")
        assertTrue(myFixture.completeBasic().find {it.lookupString == "tst1"} != null)
    }
}

