package com.eny.i18n.vue.plugin.ide.annotator

import com.eny.i18n.vue.plugin.language.vue.VueCallContext
import com.eny.i18n.vue.plugin.key.FullKeyExtractor
import com.eny.i18n.vue.plugin.language.vue.VueLanguageFactory
import com.eny.i18n.vue.plugin.parser.KeyExtractorImpl

/**
 * i18n annotator for js
 */
class VueCompositeKeyAnnotator : CompositeKeyAnnotatorBase(
    FullKeyExtractor(
        VueCallContext(),
        KeyExtractorImpl()
    ),
    VueLanguageFactory().translationExtractor().folderSelector()
)