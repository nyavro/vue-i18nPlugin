package com.eny.i18n.vue.plugin.ide.completion

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.utils.generator.code.*
import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test

class CodeCompletionInvalidMigrated: PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/completion/invalid"

    @Test
    fun testInvalid() {
        myFixture.configureByFiles("invalid/simple.ts")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("invalid/simple-patched.ts")
    }
}