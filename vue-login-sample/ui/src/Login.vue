<template>
  <div id="home" class="container">
    <div class="section">
      <h1 class="title">Login</h1>
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
      loading: false
    }
  },
  methods: {
    login () {
      this.loading = true
      setTimeout(() => {
        this.$router.replace({ name: 'home' })
      }, 3000)
    }
  }
}
</script>

<style>
</style>
