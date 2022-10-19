package com.eny.i18n.vue.plugin.ide.settings

import com.eny.i18n.vue.plugin.factory.MainFactory
import com.eny.i18n.vue.plugin.language.js.JsLanguageFactory
import com.eny.i18n.vue.plugin.language.vue.VueLanguageFactory
import com.eny.i18n.vue.plugin.localization.json.JsonLocalizationFactory
import com.eny.i18n.vue.plugin.localization.yaml.YamlLocalizationFactory
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Plugin settings
 */
@State(name = "i18nSettings", storages = [Storage("i18nSettings.xml")])
class Settings : PersistentStateComponent<Settings> {

    private val default: Config = Config()

    internal var searchInProjectOnly = default.searchInProjectOnly

    internal var nsSeparator = default.nsSeparator

    internal var keySeparator = default.keySeparator

    internal var pluralSeparator = default.pluralSeparator

    internal var defaultNs = default.defaultNs

    internal var vueDirectory = default.vueDirectory

    internal var firstComponentNs = default.firstComponentNs

    internal var jsConfiguration = default.jsConfiguration

    internal var preferYamlFilesGeneration = default.preferYamlFilesGeneration

    internal var foldingEnabled = default.foldingEnabled

    internal var foldingPreferredLanguage = default.foldingPreferredLanguage

    internal var foldingMaxLength = default.foldingMaxLength

    internal var jsonContentGenerationEnabled = default.jsonContentGenerationEnabled

    internal var yamlContentGenerationEnabled = default.yamlContentGenerationEnabled

    internal var extractSorted = default.extractSorted

    internal var partialTranslationInspectionEnabled = default.partialTranslationInspectionEnabled

    /**
     * Returns plugin configuration
     */
    fun config(): Config {
        if (ApplicationManager.getApplication().isHeadlessEnvironment) {
            synchronized(this) {
                return doGetConfig()
            }
        } else {
            return doGetConfig()
        }
    }

    private fun doGetConfig() = Config(
        searchInProjectOnly = searchInProjectOnly,
        nsSeparator = nsSeparator,
        keySeparator = keySeparator,
        pluralSeparator = pluralSeparator,
        defaultNs = defaultNs,
        vueDirectory = vueDirectory,
        firstComponentNs = firstComponentNs,
        jsConfiguration = jsConfiguration,
        preferYamlFilesGeneration = preferYamlFilesGeneration,
        foldingEnabled = foldingEnabled,
        foldingPreferredLanguage = foldingPreferredLanguage,
        foldingMaxLength = foldingMaxLength,
        jsonContentGenerationEnabled = jsonContentGenerationEnabled,
        yamlContentGenerationEnabled = yamlContentGenerationEnabled,
        extractSorted = extractSorted,
        partialTranslationInspectionEnabled = partialTranslationInspectionEnabled
    )

    fun setConfig(config: Config) {
        if (ApplicationManager.getApplication().isHeadlessEnvironment) {
            // Only in Test mode
            synchronized(this) {
                doSetConfig(config)
            }
        } else {
            doSetConfig(config)
        }
    }

    private fun doSetConfig(config: Config) {
        searchInProjectOnly = config.searchInProjectOnly
        nsSeparator = config.nsSeparator
        keySeparator = config.keySeparator
        pluralSeparator = config.pluralSeparator
        defaultNs = config.defaultNs
        vueDirectory = config.vueDirectory
        firstComponentNs = config.firstComponentNs
        jsConfiguration = config.jsConfiguration
        preferYamlFilesGeneration = config.preferYamlFilesGeneration
        foldingEnabled = config.foldingEnabled
        foldingPreferredLanguage = config.foldingPreferredLanguage
        foldingMaxLength = config.foldingMaxLength
        jsonContentGenerationEnabled = config.jsonContentGenerationEnabled
        yamlContentGenerationEnabled = config.yamlContentGenerationEnabled
        extractSorted = config.extractSorted
        partialTranslationInspectionEnabled = config.partialTranslationInspectionEnabled
    }

    override fun loadState(state: Settings) = XmlSerializerUtil.copyBean(state, this)

    override fun getState(): Settings = this

    /**
     * Service class for persisting settings
     */
    companion object Persistence {
        /**
         * Loads project's Settings instance
         */
        fun getInstance(project: Project): Settings = ServiceManager.getService(project, Settings::class.java)
    }

    fun mainFactory(): com.eny.i18n.vue.plugin.factory.MainFactory =
        com.eny.i18n.vue.plugin.factory.MainFactory(
            listOf(
                listOf(JsLanguageFactory()),
                listOf(VueLanguageFactory())
            ).flatten(),
            listOf(
                if (this.jsonContentGenerationEnabled) listOf(JsonLocalizationFactory()) else emptyList(),
                if (this.yamlContentGenerationEnabled) listOf(YamlLocalizationFactory()) else emptyList()
            ).flatten()
        )
}
