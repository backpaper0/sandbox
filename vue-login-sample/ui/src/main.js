import Vue from 'vue'
import VueRouter from 'vue-router'
import axios from 'axios'

import Home from './Home.vue'
import Login from './Login.vue'

import 'bulma/css/bulma.css'
import 'font-awesome/css/font-awesome.css'

Vue.use(VueRouter)

axios.defaults.baseURL = 'http://localhost:8000/api'
axios.defaults.withCredentials = true

Vue.prototype.$http = axios

const router = new VueRouter({
  routes: [
    { path: '/home', name: 'home', component: Home  },
    { path: '/login', name: 'login', component: Login }
  ]
})

new Vue({
  router
}).$mount('#app')

axios.get('/userinfo')
  .then(() => {
    router.replace({ name: 'home' })
  })
  .catch(e => {
    router.replace({ name: 'login' })
  })
