<template>
  <div id="one" class="container">
    <div class="section">
      <h1 class="title">One</h1>
      <p>{{ message.id }} : {{ message.text }}</p>
      <button @click="back" class="button is-primary">Back</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'one',
  data () {
    return {
      messages: [
        { id: 1, text: 'foo' },
        { id: 2, text: 'bar' },
        { id: 3, text: 'baz' }
      ]
    }
  },
  computed: {
    message () {
      return this.messages.filter(x => x.id == parseInt(this.$route.params.id))[0];
    }
  },
  methods: {
    back () {
      this.$router.back()
    }
  }
}
</script>

<style>
</style>
