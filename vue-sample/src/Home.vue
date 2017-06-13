<template>
  <div id="home" class="container">
    <div class="section">
      <h1 class="title">Home</h1>
      <ul>
        <li><router-link :to="{ name: 'list' }">Messages</router-link></li>
        <li><router-link :to="{ name: 'sample2' }">Sample2</router-link></li>
        <li><router-link :to="{ name: 'sample3a' }">Sample3</router-link></li>
        <li><router-link :to="{ name: 'component-sample' }">Component Sample</router-link></li>
        <li><router-link :to="{ name: 'hot-sample' }">Handsontable Sample</router-link></li>
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
