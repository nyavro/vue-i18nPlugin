package com.eny.i18n.vue.plugin.ide.actions

import com.eny.i18n.vue.plugin.factory.TranslationExtractor
import com.eny.i18n.vue.plugin.ide.settings.Settings
import com.eny.i18n.vue.plugin.utils.PluginBundle
import com.eny.i18n.vue.plugin.utils.whenMatches
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages.showInfoMessage
import com.intellij.psi.PsiElement

internal class DefaultExtractor : TranslationExtractor {
    override fun canExtract(element: PsiElement): Boolean = false
    override fun text(element: PsiElement): String = ""
    override fun template(element: PsiElement): (argument: String) -> String {
        TODO("Not yet implemented")
    }

    override fun isExtracted(element: PsiElement): Boolean = false
}

/**
 * Intention action of i18n key extraction
 */
class ExtractI18nIntentionAction : PsiElementBaseIntentionAction(), IntentionAction {

    private val request = KeyRequest()

    private val keyCreator = KeyCreator()

    override fun getText() = PluginBundle.getMessage("action.intention.extract.key")

    override fun getFamilyName() = "ExtractI18nIntentionAction"

    override fun invoke(project: Project, editor: Editor, element: PsiElement) =
        ApplicationManager.getApplication().invokeLater {
            doInvoke(editor, project, element)
        }

    private fun getExtractor(element: PsiElement): TranslationExtractor =
        Settings
            .getInstance(element.project)
            .mainFactory()
            .translationExtractors()
            .filter { it.canExtract(element) }
            .whenMatches { extractors -> !extractors.any { it.isExtracted(element) } }
            ?.firstOrNull()
            ?: DefaultExtractor()

    private fun doInvoke(editor: Editor, project: Project, element: PsiElement) {
        val document = editor.document
        val extractor = getExtractor(element)
        val text = extractor.text(element).trim()
        val requestResult = request.key(project, text)
        if (requestResult.isCancelled) return
        if (requestResult.key == null) {
            showInfoMessage(
                PluginBundle.getMessage("action.intention.extract.key.invalid"),
                PluginBundle.getMessage("action.intention.extract.key.invalid.title")
            )
            return
        }
        val i18nKey = requestResult.key
        val template = extractor.template(element)
        val range = extractor.textRange(element)
        keyCreator.createKey(project, i18nKey, text, editor, extractor) {
            document.replaceString(range.startOffset, range.endOffset, template("'${i18nKey.source}'"))
            extractor.postProcess(editor, range.startOffset)
        }
        editor.caretModel.primaryCaret.removeSelection()
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
        getExtractor(element).canExtract(element)
}

