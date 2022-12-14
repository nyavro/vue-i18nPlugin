package com.eny.i18n.vue.plugin.ide.references

import com.eny.i18n.vue.plugin.PlatformBaseTest
import com.eny.i18n.vue.plugin.ide.runVueConfig
import com.eny.i18n.vue.plugin.ide.settings.Config
import org.junit.jupiter.api.Test

abstract class JsConfiguredVueI18nObjTest : PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/references/"
    }

    private val testConfig = Config(jsConfiguration = "i18n.js")

    @Test
    fun testInvalidConfiguration() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid/i18nConfig.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertNull(element!!.references[0].resolve()?.text)
    }

    @Test
    fun testInvalidConfigurationObject() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertNull(element!!.references[0].resolve()?.text)
    }

    @Test
    fun testInvalidConfigurationMissingMessages() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid2/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertNull(element!!.references[0].resolve()?.text)
    }

    @Test
    fun testInvalidConfigurationMessages() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid3/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertNull(element!!.references[0].resolve()?.text)
    }

    @Test
    fun testReferencesChainIsTooLong() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf1.vue", "jsConfigured/vue-i18n/vue-i18n-object/invalid4/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("message", element!!.references[0].resolve()?.text)
    }

    @Test
    fun testReference() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf.vue", "jsConfigured/vue-i18n/vue-i18n-object/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'hello!!'", element!!.references[0].resolve()?.text)
    }

    @Test
    fun testReference1() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf1.vue", "jsConfigured/vue-i18n/vue-i18n-object/valid1/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'hey there!!!'", element!!.references[0].resolve()?.text)
    }

    @Test
    fun testReference2() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles(
            "vue/refVue-i18n-js-conf1.vue", "jsConfigured/vue-i18n/vue-i18n-object/valid2/i18n.js")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("'hey there!!!'", element!!.references[0].resolve()?.text)
    }
}
