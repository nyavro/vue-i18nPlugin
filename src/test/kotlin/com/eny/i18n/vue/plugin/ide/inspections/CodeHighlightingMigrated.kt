package com.eny.i18n.vue.plugin.ide.inspections

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runWithConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import org.junit.Test

class CodeHighlightingMigrated: PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/inspections/jscode"

    @Test
    fun testReferenceToObject() {
        myFixture.configureByFiles("referenceToObject/simple.ts", "referenceToObject/locales/en-US.json")
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testResolved()  {
        myFixture.configureByFiles("resolved/simple.ts", "resolved/locales/en-US.json")
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testNotArg() {
        myFixture.configureByFiles("notArg/simple.ts", "notArg/locales/en-US.json")
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testKeyInExpression() {
        myFixture.configureByFiles("keyInExpression/simple.ts", "keyInExpression/locales/en-US.json")
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testPartiallyTranslated() = myFixture.runWithConfig(Config(partialTranslationInspectionEnabled = true)) {
        myFixture.configureByFiles(
            "partiallyTranslated/simple.ts",
            "partiallyTranslated/locales/en-US.json",
            "partiallyTranslated/locales/de-DE.json"
        )
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testUnresolvedKey() {
        myFixture.configureByFiles(
            "unresolvedKey/simple.ts",
            "unresolvedKey/locales/en-US.json"
        )
        myFixture.checkHighlighting(true, true, true, true)
    }

    @Test
    fun testResolvedTemplate() {
        myFixture.configureByFiles(
            "resolvedTemplate/simple.ts",
            "resolvedTemplate/locales/en-US.json"
        )
        myFixture.checkHighlighting(true, true, true, true)
    }
}
