package com.eny.i18n.vue.plugin.ide.references.code

import com.eny.i18n.vue.plugin.language.js.JsLanguageFactory

/**
 * JS dialect reference contributor
 */
class JsReferenceContributor: ReferenceContributorBase(JsLanguageFactory().referenceAssistant())