<template>
  <section class="section">
    <h1 class="title">Component Sample</h1>
    <Panel v-for="message in messages" :message="message"></Panel>
  </section>
</template>

<script>
import Panel from './Panel.vue'

export default {
  name: 'component-sample',
  data () {
    return {
      messages: ['foo', 'bar', 'baz']
    }
  },
  computed: {
  },
  methods: {
  },
  components: {
    Panel
  }
}
</script>

<style>
</style>
