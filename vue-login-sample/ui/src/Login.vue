<template>
  <div id="home" class="container">
    <div class="section">
      <h1 class="title">Login</h1>
      <div class="notification is-danger" v-if="hasError">
        <button class="delete" @click="hideError"></button>
        Error!!!
      </div>
      <p class="control has-icon">
        <input class="input" type="text" placeholder="Username" v-model="username">
        <span class="icon is-small">
          <i class="fa fa-user"></i>
        </span>
      </p>
      <p class="control has-icon">
        <input class="input" type="password" placeholder="Password" v-model="password">
        <span class="icon is-small">
          <i class="fa fa-lock"></i>
        </span>
      </p>
      <p class="control">
        <button class="button is-success" @click="login" :class="{ 'is-loading': loading }">
          Login
        </button>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'home',
  data () {
    return {
      username: 'backpaper0',
      password: 'secret',
      loading: false,
      hasError: false
    }
  },
  methods: {
    login () {
      this.loading = true
      var params = new URLSearchParams()
      params.append( 'username', this.username)
      params.append('password', this.password)
      params.append('remember-me', 'yes')
      this.$http
        .post('/login', params)
        .then(response => {
          this.loading = false
          this.$router.replace({ name: 'home' })
        })
        .catch(error => {
          this.loading = false
          this.hasError = true
        })
    },
    hideError () {
      this.hasError = false
    }
  }
}
</script>

<style>
</style>
