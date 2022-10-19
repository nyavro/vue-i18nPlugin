package com.eny.i18n.vue.plugin.factory

import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement

/**
 * Language components factory
 */
interface LanguageFactory {
    /**
     * Get translation extractor object
     */
    fun translationExtractor(): com.eny.i18n.vue.plugin.factory.TranslationExtractor
    /**
     * Get folding provider
     */
    fun foldingProvider(): com.eny.i18n.vue.plugin.factory.FoldingProvider
    /**
     * Get pattern to extract i18n keys from code
     */
    fun callContext(): com.eny.i18n.vue.plugin.factory.CallContext
    /**
     * Assistant for code to translation navigation
     */
    fun referenceAssistant(): com.eny.i18n.vue.plugin.factory.ReferenceAssistant
}

/**
 * Represents localization type.
 * subSystem defines usage cases.
 */
data class LocalizationType(val fileType: FileType, val subSystem: String)

/**
 * Localization components factory
 */
interface LocalizationFactory {

    /**
     * Content generator
     */
    fun contentGenerator(): com.eny.i18n.vue.plugin.factory.ContentGenerator

    /**
     * Localization format-specific reference assistant
     */
    fun referenceAssistant(): com.eny.i18n.vue.plugin.factory.TranslationReferenceAssistant<out PsiElement>
}

/**
 * Plugin's components factory
 */
class MainFactory(private val languageFactories: List<com.eny.i18n.vue.plugin.factory.LanguageFactory>, private val localizationFactories: List<com.eny.i18n.vue.plugin.factory.LocalizationFactory>) {

    /**
     * Get available translation extractors
     */
    fun translationExtractors(): List<com.eny.i18n.vue.plugin.factory.TranslationExtractor> =
        languageFactories.map {it.translationExtractor()}

    /**
     * Get available content generators
     */
    fun contentGenerators(): List<com.eny.i18n.vue.plugin.factory.ContentGenerator> =
        localizationFactories.map {it.contentGenerator()}

    /**
     * Pick content generator by file type
     */
    fun contentGenerator(type: com.eny.i18n.vue.plugin.factory.LocalizationType): com.eny.i18n.vue.plugin.factory.ContentGenerator? =
        localizationFactories.find {it.contentGenerator().getType() == type}?.contentGenerator()
}