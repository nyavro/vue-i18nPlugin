export const test0 = (i18n: {t: Function}) => {
    return $t(isSelected ? "<warning descr="Reference to object">tst2.plurals</warning>" : "<warning descr="Unresolved key">unresolved.whole.key</warning>");
};