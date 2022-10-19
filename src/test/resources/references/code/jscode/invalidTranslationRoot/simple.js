export const test0 = (i18n) => {
    return $t(`invalidRoot:ref.section<caret>.${b ? 'key' : 'key2'}`);
};