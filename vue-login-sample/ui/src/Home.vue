<template>
  <div id="home" class="container">
    <div class="section">
      <h1 class="title">Home</h1>
      <div class="notification is-primary">
        <p>{{ message }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'home',
  data () {
    return {
      message: 'Hello, world!'
    }
  }
}
</script>

<style>
</style>
