package com.eny.i18n.vue.plugin.ide.annotator

import com.eny.i18n.vue.plugin.factory.TranslationFolderSelector
import com.eny.i18n.vue.plugin.ide.quickfix.*
import com.eny.i18n.vue.plugin.ide.settings.Settings
import com.eny.i18n.vue.plugin.key.FullKey
import com.eny.i18n.vue.plugin.key.lexer.Literal
import com.eny.i18n.vue.plugin.tree.PropertyReference
import com.eny.i18n.vue.plugin.utils.PluginBundle
import com.eny.i18n.vue.plugin.utils.RangesCalculator
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Annotation helper methods
 */
class AnnotationHelper(private val holder: AnnotationHolder, private val rangesCalculator: RangesCalculator, private val project: Project, private val folderSelector: TranslationFolderSelector) {

    private val errorSeverity = HighlightSeverity.WARNING
    private val infoSeverity = HighlightSeverity.INFORMATION

    /**
     * Annotates resolved translation key
     */
    fun annotateResolved(fullKey: FullKey) {
        holder
            .newAnnotation(infoSeverity, "")
            .range(rangesCalculator.compositeKeyFullBounds(fullKey))
            .textAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT)
            .create()
    }

    /**
     * Annotates reference to object, not a leaf key in json/yaml
     */
    fun annotateReferenceToObject(fullKey: FullKey) {
        holder
            .newAnnotation(errorSeverity, PluginBundle.getMessage("annotator.object.reference"))
            .range(rangesCalculator.compositeKeyFullBounds(fullKey))
            .create()
    }

    /**
     * Annotates unresolved namespace
     */
    fun unresolvedNs(fullKey: FullKey, ns: Literal) {
        val builder = holder
            .newAnnotation(errorSeverity, PluginBundle.getMessage("annotator.unresolved.ns"))
            .range(rangesCalculator.unresolvedNs(fullKey))
        Settings.getInstance(project).mainFactory().contentGenerators().forEach {
            builder.withFix(CreateTranslationFileQuickFix(fullKey, it, folderSelector, ns.text))
        }
        builder.create()
    }

    /**
     * Annotates unresolved default namespace
     */
    fun unresolvedDefaultNs(fullKey: FullKey) {
        holder
            .newAnnotation(errorSeverity, PluginBundle.getMessage("annotator.missing.default.ns"))
            .range(rangesCalculator.compositeKeyFullBounds(fullKey))
            .create()
    }

    /**
     * Annotates unresolved composite key
     */
    fun unresolvedKey(fullKey: FullKey, mostResolvedReference: PropertyReference<PsiElement>) {
        val builder = holder
            .newAnnotation(errorSeverity, PluginBundle.getMessage("annotator.unresolved.key"))
            .range(rangesCalculator.unresolvedKey(fullKey, mostResolvedReference.path))
        val generators = Settings.getInstance(project).mainFactory().contentGenerators()
        builder.withFix(CreateKeyQuickFix(fullKey, UserChoice(), PluginBundle.getMessage("quickfix.create.key"), generators))
        builder.withFix(CreateKeyQuickFix(fullKey, AllSourcesSelector(), PluginBundle.getMessage("quickfix.create.key.in.files"), generators))
        builder.create()
    }

    /**
     * Annotates partially translated key and creates quick fix for it.
     */
    fun annotatePartiallyTranslated(fullKey: FullKey, references: List<PropertyReference<PsiElement>>) {
        references.minByOrNull { it.path.size }?.let {
            val builder = holder
                .newAnnotation(errorSeverity, PluginBundle.getMessage("annotator.partially.translated"))
                .range(rangesCalculator.unresolvedKey(fullKey, it.path))
            val generators = Settings.getInstance(project).mainFactory().contentGenerators()
            builder.withFix(CreateKeyQuickFix(fullKey, UserChoice(), PluginBundle.getMessage("quickfix.create.key"), generators))
            builder.withFix(CreateKeyQuickFix(fullKey, AllSourcesSelector(), PluginBundle.getMessage("quickfix.create.missing.keys"), generators))
            builder.create()
        }
    }
}