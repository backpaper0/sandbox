import Vue from 'vue'

import 'bulma/css/bulma.css'
import 'font-awesome/css/font-awesome.css'

import router from './router/router.js'
import store from './store/store.js'

new Vue({
  router,
  store
}).$mount('#app')
