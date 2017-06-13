<template>
  <div id="list" class="container">
    <div class="section">
      <h1 class="title">List</h1>
      <ul>
        <li v-for="message in messages">
          <router-link :to="{ name: 'one', params: { id: message.id }}">
            {{ message.text }}
          </router-link>
        </li>
      </ul>
      <p><router-link to="/" class="button is-info">Home</router-link></p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'list',
  data () {
    return {
      messages: [
        { id: 1, text: 'foo' },
        { id: 2, text: 'bar' },
        { id: 3, text: 'baz' }
      ]
    }
  }
}
</script>

<style>
</style>
