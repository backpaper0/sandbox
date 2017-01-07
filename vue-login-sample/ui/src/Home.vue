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
    var data = {
      message: 'Now loading...'
    }
    this.$http
      .get('/userinfo')
      .then(response => {
        data.message = 'Hello, ' + response.data.name + '!'
      })
    return data
  }
}
</script>

<style>
</style>
