<template>
  <div id="home" class="container">
    <div class="section">
      <h1 class="title">Home</h1>
      <ul>
        <li><router-link :to="{ name: 'list' }">Messages</router-link></li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'home',
  data () {
    return {
    }
  }
}
</script>

<style>
</style>
