package com.eny.i18n.vue.plugin.ide.references.code

import com.eny.i18n.vue.plugin.language.vue.VueLanguageFactory
import com.eny.i18n.vue.plugin.language.vue.VueReferenceAssistant

/**
 * Vue dialect reference contributor
 */
class VueReferenceContributor: ReferenceContributorBase(VueLanguageFactory().referenceAssistant())