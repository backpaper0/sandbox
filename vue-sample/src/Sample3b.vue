<template>
  <div id="sample" class="container">
    <div class="section">
      <p>
        {{ message }}
      <p>
      </p>
        <button class="button" @click="prev">Prev</button>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'sample',
  data () {
    return {
    }
  },
  computed: {
    message () {
      return this.$store.state.message
    }
  },
  methods: {
    prev () {
      this.$router.back()
    }
  }
}
</script>

<style>
</style>
