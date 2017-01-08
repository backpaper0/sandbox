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
import { mapState } from 'vuex'

export default {
  name: 'sample',
  data () {
    return {
    }
  },
  computed: mapState({
    message: state => state.message
  }),
  methods: {
    prev () {
      this.$router.back()
    }
  }
}
</script>

<style>
</style>
