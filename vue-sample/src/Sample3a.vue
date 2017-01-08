<template>
  <div id="sample" class="container">
    <div class="section">
      <p>
        <input type="text" v-model="message" class="input">
      </p>
      <p>
        <button class="button" @click="next">Next</button>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'sample',
  data () {
    return {
      message: this.$store.state.message
    }
  },
  methods: {
    next () {
      this.$store.dispatch('updateMessage', this.message)
      this.$router.push({ name: 'sample3b' })
    }
  }
}
</script>

<style>
</style>
