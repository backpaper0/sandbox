import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from './Home.vue'
import List from './List.vue'
import One  from './One.vue'

import 'bulma/css/bulma.css'

Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    { path: '/' , component: Home  },
    { path: '/messages'  , name: 'list', component: List },
    { path: '/messages/:id', name: 'one', component: One }
  ]
})

new Vue({
  router
}).$mount('#app')

