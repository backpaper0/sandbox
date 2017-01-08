import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from './Home.vue'
import List from './List.vue'
import One  from './One.vue'
import Sample2  from './Sample2.vue'
import Sample2b from './Sample2b.vue'

import 'bulma/css/bulma.css'

Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    { path: '/' , component: Home  },
    { path: '/messages'  , name: 'list', component: List },
    { path: '/messages/:id', name: 'one', component: One },
    { path: '/sample2', name: 'sample2', component: Sample2 },
    { path: '/sample2b', name: 'sample2b', component: Sample2b }
  ]
})

new Vue({
  router
}).$mount('#app')

