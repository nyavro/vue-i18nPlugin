package com.eny.i18n.vue.plugin.utils.generator.code

class TsCodeGenerator: CodeGenerator {

    override fun ext(): String = "ts"

    override fun generate(key: String, index: Int): String {
        val fn = "\$t"
        return """
        export const test$index = (i18n: {t: Function}) => {
            return $fn($key);
        };
    """}

    override fun generateInvalid(key: String): String = """
        const key = (s: Function) => s($key);
    """

    override fun generateBlock(text: String, index: Int): String = """
        export const test$index = (i18n: {t: Function}) => {
            return "$text";
        };
    """
}