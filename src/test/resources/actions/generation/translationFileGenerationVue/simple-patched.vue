<template>
    <div id="app"/>
</template>
<style>
    h1 {color: #42b983;}
</style>
<script>
    export default {
        name: "AppHeader",
        computed: {
            title() {
                const text = this.$t('component.header.title');
                return "";
            }
        }
    }
</script>