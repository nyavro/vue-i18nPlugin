package com.eny.i18n.vue.plugin.ide.completion

import com.eny.i18n.vue.plugin.language.vue.VueLanguageFactory

/**
 * Completion contributor for Vue
 */
class VueCompletionContributor: CompositeKeyCompletionContributor(VueLanguageFactory().callContext())