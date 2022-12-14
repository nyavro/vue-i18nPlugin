package com.eny.i18n.vue.plugin.parser

import com.eny.i18n.vue.plugin.factory.CallContext
import com.eny.i18n.vue.plugin.ide.settings.Settings
import com.eny.i18n.vue.plugin.key.FullKey
import com.eny.i18n.vue.plugin.key.parser.KeyParserBuilder
import com.intellij.psi.PsiElement

/**
 * Gets element's type string
 */
fun PsiElement.type(): String = this.node?.elementType.toString()

interface Extractor {
    fun extractFullKey(element: PsiElement): FullKey?
}

class DummyContext: CallContext {
    override fun accepts(element: PsiElement): Boolean = true
}

/**
 * Extracts translation key from psi element
 */
class KeyExtractorImpl: Extractor {

    /**
     * Extracts fullkey from element, if possible
     */
    override fun extractFullKey(element: PsiElement): FullKey? {
        val config = Settings.getInstance(element.project).config()
        val parser = KeyParserBuilder
            .withSeparators(config.nsSeparator, config.keySeparator)
            .withDummyNormalizer()
            .withTemplateNormalizer()
            .build()
        return listOf(
            ReactUseTranslationHookExtractor(),
            TemplateKeyExtractor(),
            LiteralKeyExtractor(),
            StringLiteralKeyExtractor(),
            XmlAttributeKeyExtractor()
        )
            .find {it.canExtract(element)}
            ?.let{parser.parse(it.extract(element), true, config.firstComponentNs)}
    }
}
