package com.eny.i18n.vue.plugin.ide.actions

import com.eny.i18n.vue.plugin.ide.settings.Settings
import com.eny.i18n.vue.plugin.key.FullKey
import com.eny.i18n.vue.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.vue.plugin.utils.KeyElement
import com.eny.i18n.vue.plugin.utils.PluginBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages.getQuestionIcon
import com.intellij.openapi.ui.Messages.showInputDialog

/**
 * Key request result
 */
class KeyRequestResult(val key: FullKey?, val isCancelled: Boolean)

/**
 * Requests i18n key from user
 */
class KeyRequest {

    /**
     * Requests key
     */
    fun key(project: Project, text: String): KeyRequestResult {
        val config = Settings.getInstance(project).config()
        val keyStr = showInputDialog(
            project,
            String.format(PluginBundle.getMessage("action.intention.extract.key.hint"), text),
            PluginBundle.getMessage("action.intention.extract.key.input.key"),
            getQuestionIcon(),
            null,
            isValidKey())
        return if(keyStr == null) {
            KeyRequestResult(null, true)
        } else {
            KeyRequestResult(
                KeyParserBuilder.withSeparators(config.nsSeparator, config.keySeparator).build()
                    .parse(
                        Pair(listOf(KeyElement.literal(keyStr)), null),
                        emptyNamespace = true,
                        firstComponentNamespace = config.firstComponentNs
                    ),
                false
            )
        }
    }

    private fun isValidKey() = object : InputValidator {
        override fun checkInput(inputString: String?): Boolean {
            return (inputString ?: "").isNotEmpty()
        }
        override fun canClose(inputString: String?): Boolean = true
    }
}