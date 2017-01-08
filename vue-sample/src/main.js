import Vue from 'vue'
import VueRouter from 'vue-router'
import Vuex from 'vuex'

import Home from './Home.vue'
import List from './List.vue'
import One  from './One.vue'
import Sample2  from './Sample2.vue'
import Sample2b from './Sample2b.vue'

import Sample3a from './Sample3a.vue'
import Sample3b from './Sample3b.vue'

import 'bulma/css/bulma.css'

Vue.use(VueRouter)
Vue.use(Vuex)

const router = new VueRouter({
  routes: [
    { path: '/' , name: 'home', component: Home  },
    { path: '/messages'  , name: 'list', component: List },
    { path: '/messages/:id', name: 'one', component: One },
    { path: '/sample2', name: 'sample2', component: Sample2 },
    { path: '/sample2b', name: 'sample2b', component: Sample2b },
    { path: '/sample3a', name: 'sample3a', component: Sample3a },
    { path: '/sample3b', name: 'sample3b', component: Sample3b }
  ]
})

const store = new Vuex.Store({
  state: {
    message: 'Hello, world!'
  },
  mutations: {
    updateMessage (state, message) {
      state.message = message
    }
  },
  actions: {
    updateMessage (context, message) {
      context.commit('updateMessage', message)
    }
  }
})

new Vue({
  router,
  store
}).$mount('#app')

