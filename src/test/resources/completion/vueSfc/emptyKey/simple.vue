<template>
  <div id="app">
    <label for="locale">locale</label>
    <select v-model="locale">
      <option>en</option>
      <option>ja</option>
    </select>
    <p>message: {{ t("<caret>") }}</p>
  </div>
</template>
<i18n>
  {
    "en": {
      "tstw": {
        "fstt": {
          "leu": "value"
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