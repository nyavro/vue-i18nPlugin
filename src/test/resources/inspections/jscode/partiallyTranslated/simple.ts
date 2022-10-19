export const test0 = (i18n: {t: Function}) => {
    return $t('root.<warning descr="Partially translated key">sub.base</warning>');
};