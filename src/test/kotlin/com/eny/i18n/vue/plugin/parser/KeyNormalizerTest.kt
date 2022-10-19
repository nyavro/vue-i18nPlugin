package com.eny.i18n.vue.plugin.parser

import com.eny.i18n.vue.plugin.utils.KeyElement
import com.intellij.codeInsight.completion.CompletionInitializationContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class KeyNormalizerTest {

//valid:ROOT.Key1.---idea-placeholder-here---
    @Test
    fun normalizeDummy() {
        val element = KeyElement.literal("valid:ROOT.Key1.${CompletionInitializationContext.DUMMY_IDENTIFIER}")
        val normalized = DummyTextNormalizer().normalize(element)
        assertEquals(
            "valid:ROOT.Key1.${CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED}",
            normalized?.text
        )
    }
}
