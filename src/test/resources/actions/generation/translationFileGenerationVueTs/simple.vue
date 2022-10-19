<template>
    <div id="app">
        <h1>{{ \$t('ref.section.key') }}</h1>
        <div>{{title}}</div>
    </div>
</template>
<style>
    h1 {color: #42b983;}
</style>
<script lang="ts">
    export default {
        name: "AppHeader",
        computed: {
            title(): string {
                const expr: string = 'ts here';
                return "I want<caret> to move it to translation";
            }
        }
    }
</script>