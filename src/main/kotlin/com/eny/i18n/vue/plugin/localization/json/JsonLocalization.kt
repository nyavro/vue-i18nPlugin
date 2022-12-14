package com.eny.i18n.vue.plugin.localization.json

import com.eny.i18n.vue.plugin.factory.ContentGenerator
import com.eny.i18n.vue.plugin.factory.LocalizationFactory
import com.eny.i18n.vue.plugin.factory.LocalizationType
import com.eny.i18n.vue.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.vue.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.eny.i18n.vue.plugin.ide.settings.Settings
import com.eny.i18n.vue.plugin.key.FullKey
import com.eny.i18n.vue.plugin.key.lexer.Literal
import com.eny.i18n.vue.plugin.utils.CollectingSequence
import com.eny.i18n.vue.plugin.utils.PluginBundle
import com.fasterxml.jackson.core.io.JsonStringEncoder
import com.intellij.json.JsonFileType
import com.intellij.json.JsonLanguage
import com.intellij.json.psi.*
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parents

private val tabChar = "  "

class JsonLocalizationFactory: com.eny.i18n.vue.plugin.factory.LocalizationFactory {
    override fun contentGenerator(): ContentGenerator = JsonContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<JsonStringLiteral> = JsonReferenceAssistant()
}

private class JsonReferenceAssistant : TranslationReferenceAssistant<JsonStringLiteral> {

    private val provider = TranslationToCodeReferenceProvider()

    override fun pattern(): ElementPattern<out JsonStringLiteral> = PlatformPatterns.psiElement(JsonStringLiteral::class.java)

    override fun references(element: JsonStringLiteral): List<PsiReference> =
        if (element.isPropertyName && element.textLength > 1) {
            provider.getReferences(element, textRange(element), parents(element))
        } else {
            emptyList()
        }

    private fun parents(element: JsonStringLiteral): List<String> =
        CollectingSequence(element.parents()) {
            when {
                it is JsonProperty -> it.name
                it is JsonFile -> it.name.substringBeforeLast(".")
                else -> null
            }
        }.toList().reversed()

    private fun textRange(element: JsonStringLiteral): TextRange = TextRange(1, element.textLength - 1)
}

/**
 * Generates JSON translation content
 */
private class JsonContentGenerator: ContentGenerator {

    override fun generateContent(compositeKey: List<Literal>, value: String): String {
        val escapedValue = String(JsonStringEncoder.getInstance().quoteAsString(value))
        return compositeKey.foldRightIndexed("\"$escapedValue\"", { i, key, acc ->
            val tab = tabChar.repeat(i)
            "{\n$tabChar$tab\"${key.text}\": $acc\n$tab}"
        })
    }

    override fun getType(): FileType = JsonFileType.INSTANCE
    override fun getLanguage(): Language = JsonLanguage.INSTANCE
    override fun getDescription(): String = PluginBundle.getMessage("quickfix.create.json.translation.files")
    override fun isSuitable(element: PsiElement): Boolean = element is JsonObject
    override fun generateTranslationEntry(item: PsiElement, key: String, value: String) {
        val obj = item as JsonObject
        val generator = JsonElementGenerator(item.project)
        val keyValue = generator.createProperty(key, value)
        val props = obj.getPropertyList()
        if (props.isEmpty()) {
            obj.addAfter(keyValue, obj.findElementAt(0))
            return
        }
        val separator = generator.createComma()
        val (element, anchor) = if (Settings.getInstance(item.project).extractSorted) {
            val before = props.takeWhile {it.name < key}
            if (before.isEmpty()) {
                Pair(separator, obj.addBefore(keyValue, props.first()))
            } else {
                Pair(keyValue, obj.addAfter(separator, before.last()))
            }
        }
        else {
            Pair(keyValue, obj.addAfter(separator, props.last()))
        }
        obj.addAfter(element, anchor)
    }

    override fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?) =
        generateTranslationEntry(
            element,
            unresolved.first().text,
            generateContent(unresolved.drop(1), translationValue ?: fullKey.source)
        )
}