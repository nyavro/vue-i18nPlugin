package com.eny.i18n.vue.plugin.ide.settings

import com.eny.i18n.vue.plugin.utils.PluginBundle
import io.mockk.mockk
import io.mockk.unmockkAll
import net.sourceforge.marathon.javadriver.JavaDriver
import net.sourceforge.marathon.javadriver.JavaProfile
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.reflect.KMutableProperty1
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

class SettingsPanelTest {

    private lateinit var driver: JavaDriver

    @Before
    fun setUp() {
        driver = JavaDriver(JavaProfile(JavaProfile.LaunchMode.EMBEDDED))
    }

    @Test
    fun testSearchInProjectFilesOnly() {
        checkBooleanProperty(PluginBundle.getMessage("settings.search.in.project.files.only"), Settings::searchInProjectOnly)
    }

    @Test
    fun testFirstComponentNs() {
        checkBooleanProperty(PluginBundle.getMessage("settings.vue.first.component.ns"), Settings::firstComponentNs)
    }

    @Test
    fun testPreferYamlFilesGeneration() {
        checkBooleanProperty(PluginBundle.getMessage("settings.prefer.yaml.files.generation"), Settings::preferYamlFilesGeneration)
    }

    @Test
    fun testFoldingEnabled() {
        checkBooleanProperty(PluginBundle.getMessage("settings.folding.enabled"), Settings::foldingEnabled)
    }

    @Test
    fun testExtractSorted() {
        checkBooleanProperty(PluginBundle.getMessage("settings.extraction.sorted"), Settings::extractSorted)
    }

    @Test
    fun testPartiallyTraslated() {
        checkBooleanProperty(PluginBundle.getMessage("settings.annotations.partially.translated.enabled"), Settings::partialTranslationInspectionEnabled)
    }

    @Test
    fun testInvalidSeparator() = runWithSettings(Settings()) {
        settings ->
            val message = PluginBundle.getMessage("settings.key.separator")
            val cb = driver.findElementByName(message)
            assertNotNull(cb)
            val value = cb.text
            assertEquals(value, settings.keySeparator)
            cb.clear()
            cb.sendKeys(" {}\$")
            assertEquals("", settings.keySeparator)
    }

    @Test
    fun testKeySeparator() {
        checkStringProperty("@", PluginBundle.getMessage("settings.key.separator"), Settings::keySeparator)
    }

    @Test
    fun testPluralSeparator() {
        checkStringProperty("%", PluginBundle.getMessage("settings.plural.separator"), Settings::pluralSeparator)
    }

    @Test
    fun testDefaultNs() {
        checkStringProperty("testloc", PluginBundle.getMessage("settings.default.namespace"), Settings::defaultNs)
    }

    @Test
    fun testVueDirectory() {
        checkStringProperty("testloc", PluginBundle.getMessage("settings.vue.locales.directory"), Settings::vueDirectory)
    }

    @Test
    fun testFoldingPreferredLanguage() {
        checkStringProperty("jp", PluginBundle.getMessage("settings.folding.preferredLanguage"), Settings::foldingPreferredLanguage)
    }

//    @Test
//    fun testFoldingMaxLength() {
//        checkIntProperty("\b\b17", PluginBundle.getMessage("settings.folding.maxLength"), Settings::foldingMaxLength)
//    }

//    private fun checkIntProperty(keys: String, message: String, property: KMutableProperty1<Settings, Int>) = runWithSettings(Settings()) {
//        settings ->
//            val cb = driver.findElementByName(message)
//            assertNotNull(cb)
//            val text = cb.text
//            assertEquals(text.toInt(), property.get(settings))
//            cb.sendKeys(keys)
//            assertEquals(keys.toInt(), property.get(settings))
//    }

    private fun checkStringProperty(keys: String, message: String, property: KMutableProperty1<Settings, String>) = runWithSettings(Settings()) {
        settings ->
            val cb = driver.findElementByName(message)
            assertNotNull(cb)
            val value = cb.text
            assertEquals(value, property.get(settings))
            cb.clear()
            cb.sendKeys(keys)
            assertEquals(keys, property.get(settings))
    }

    private fun checkBooleanProperty(message: String, property: KMutableProperty1<Settings, Boolean>) = runWithSettings(Settings()) {
        settings ->
            val cb = driver.findElementByName(message)
            assertNotNull(cb)
            val value = cb.text == "true"
            assertEquals(value, property.get(settings))
            cb.click()
            assertEquals(!value, property.get(settings))
    }

    private fun runWithSettings(settings: Settings, block: (settings: Settings) -> Unit) {
        val frame = JFrame()
        frame.contentPane.add(SettingsPanel(settings, mockk()).getRootPanel())
        SwingUtilities.invokeLater {
            frame.isVisible = true
        }
        block(settings)
        frame.dispose()
    }

    @After
    fun tearDown() {
        driver.quit()
        unmockkAll()
    }
}
