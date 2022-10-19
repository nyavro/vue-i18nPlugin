export const test0 = (i18n) => {
    return $t(isMobile ? 'ref.section.key' : 'ref.section2.ke<caret>y');
};