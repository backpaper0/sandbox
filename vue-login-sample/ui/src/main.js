import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from './Home.vue'
import Login from './Login.vue'

import 'bulma/css/bulma.css'
import 'font-awesome/css/font-awesome.css'

Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    { path: '/', name: 'home', component: Home  },
    { path: '/login', name: 'login', component: Login }
  ]
})

new Vue({
  router
}).$mount('#app')

router.replace({ name: 'login' })
