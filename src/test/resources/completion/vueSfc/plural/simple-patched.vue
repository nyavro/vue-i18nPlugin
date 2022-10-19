<template>
  <div id="app">
    <label for="locale">locale</label>
    <select v-model="locale">
      <option>en</option>
      <option>ja</option>
    </select>
    <p>message: {{ t("tst2.plurals.value") }}</p>
  </div>
</template>

<i18n>
{
  "en": {
    "tst2": {
      "plurals": {
        "value-1": "tt",
        "value-2": "qq",
        "value-5": "vv"
      },
      "second": {
        "value": "tt"
      }
    }
  }
}
</i18n>

<script>
export default {
  name: 'App',
  data () { return { locale: 'en' } },
  watch: {
    locale (val) {
      this.$i18n.locale = val
    }
  }
}
</script>