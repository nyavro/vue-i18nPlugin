package com.eny.i18n.vue.plugin.ide.completion

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVue
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test

class VueSfcCodeCompletionMigrated: PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/completion/vueSfc"

    private fun doCheck(testName: String) {
        myFixture.configureByFile("${testName}/simple.vue")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("${testName}/simple-patched.vue")
    }

    @Test
    fun testEmptyKeyCompletion() {
        myFixture.configureByFile("emptyKey/simple.vue")
        assertTrue(myFixture.completeBasic().find {it.lookupString == "tstw"} != null)
    }

    @Test
    fun testRootKeyCompletion() {
        myFixture.configureByFile("rootKey/simple.vue")
        assertTrue(myFixture.completeBasic().find {it.lookupString == "tst1"} != null)
    }

    //No completion happens
    @Test
    fun testNoCompletion() = doCheck("noCompletion")

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    @Test
    fun testSingle() = doCheck("single")

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    @Test
    fun testPlural() = doCheck("plural")

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    @Test
    fun testPartial() = doCheck("partial")

    @Test
    fun testInvalidCompletion() = doCheck("invalid")
}
