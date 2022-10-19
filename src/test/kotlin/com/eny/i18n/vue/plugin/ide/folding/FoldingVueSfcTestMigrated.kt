package com.eny.i18n.vue.plugin.ide.folding

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.vue.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import org.junit.jupiter.api.Test
import java.io.File

class FoldingVueSfcTestMigrated: PlatformBaseTest() {

    override fun getTestDataPath(): String = "src/test/resources/folding/vueSfc"

    private val testConfig = Config(foldingEnabled = true)

    @Test
    fun testBasic() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFile("basic/simple.vue")
        assertEquals(
            File(getTestDataPath() + "/basic/simple-folded.vue").readText(),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testPreferredLanguage() = myFixture.runVueConfig(
        Config(foldingPreferredLanguage = "ru", foldingMaxLength = 26, foldingEnabled = true)
    ) {
        myFixture.configureByFile("preferredLanguage/simple.vue")
        assertEquals(
            File(getTestDataPath() + "/preferredLanguage/simple-folded.vue").readText(),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testPreferredLanguageInvalidConfiguration() = myFixture.runVueConfig(
            Config(foldingPreferredLanguage = "fr", foldingEnabled = true)
    ) {
        myFixture.configureByFile("preferredLanguageInvalidConfiguration/simple.vue")
        assertEquals(
            File(getTestDataPath() + "/preferredLanguageInvalidConfiguration/simple-folded.vue").readText(),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testIncompleteKey() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFile("incompleteKey/simple.vue")
        assertEquals(
            File(getTestDataPath() + "/incompleteKey/simple-folded.vue").readText(),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testFoldingDisabled() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFile("foldingDisabled/simple.vue")
        assertEquals(
            File(getTestDataPath() + "/foldingDisabled/simple-folded.vue").readText(),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testFoldingParametrizedTranslation() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFile("foldingParametrizedTranslation/simple.vue")
        assertEquals(
            File(getTestDataPath() + "/foldingParametrizedTranslation/simple-folded.vue").readText(),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }
}


