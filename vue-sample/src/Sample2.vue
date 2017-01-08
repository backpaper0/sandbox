<template>
  <div id="list" class="container">
    <div class="section">
      <p><input type="text" v-model="message" class="input"></p>
      <p>{{ message }}</p>
      <p><button class="button" @click="next">Next</button></p>
    </div>
  </div>
</template>

<script>
console.log(Date.now() + ': load Sample2.vue')
const data = {
  message: ''
}
export default {
  name: 'list',
  data () {
    console.log(Date.now() + ': return data')
    return data
  },
  methods: {
    next () {
      this.$router.push({ name: 'sample2b' })
    }
  }
}
</script>

<style>
</style>
