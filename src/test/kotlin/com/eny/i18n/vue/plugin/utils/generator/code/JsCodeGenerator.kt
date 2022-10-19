package com.eny.i18n.vue.plugin.utils.generator.code

class JsCodeGenerator: CodeGenerator {

    override fun ext(): String = "js"

    override fun generate(key: String, index: Int): String {
        val fn = "\$t"
        return """
            export const test$index = (i18n) => {
                return $fn($key);
            };
        """
    }

    override fun generateInvalid(key: String): String = """
        const key = (t) => "$key";
    """

    override fun generateBlock(text: String, index: Int): String = """
        export const test$index = (i18n) => {
            return "$text";
        };
    """
}