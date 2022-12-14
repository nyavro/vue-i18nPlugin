package com.eny.i18n.vue.plugin.key

import com.eny.i18n.vue.plugin.key.lexer.Literal
import com.eny.i18n.vue.plugin.utils.nullableToList

/**
 * Represents translation key
 */
data class FullKey(
    val source: String,
    val ns: Literal?,
    val compositeKey:List<Literal>,
    val namespaces: List<String>? = null
) {
    fun allNamespaces(): List<String> {
        return ns?.text.nullableToList() + (namespaces ?: listOf())
    }
}