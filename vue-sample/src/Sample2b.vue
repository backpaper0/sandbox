<template>
  <transition name="slide-fade">
    <div id="list" class="container">
      <div class="section">
        <p><button class="button" @click="prev">Prev</button></p>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'list',
  data () {
    return {
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
.slide-fade-enter-active {
  transition: all .3s ease;
}
.slide-fade-enter {
  transform: translateX(10px);
  opacity: 0;
}
</style>
