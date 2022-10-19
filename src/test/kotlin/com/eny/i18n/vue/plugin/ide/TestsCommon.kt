package com.eny.i18n.vue.plugin.ide

import com.eny.i18n.vue.plugin.ide.settings.Config
import com.eny.i18n.vue.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

internal fun CodeInsightTestFixture.runWithConfig (config: Config, block: () -> Unit) {
    val settings = Settings.getInstance(this.project)
    val original = settings.config()
    settings.setConfig(config)
    block()
    settings.setConfig(original)
}

internal fun CodeInsightTestFixture.runVueConfig (config: Config, block: () -> Unit) = runWithConfig(config, block)

internal fun CodeInsightTestFixture.runVue (block: () -> Unit) = runVueConfig(Config(), block)
