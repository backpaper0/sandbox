import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from '../Home.vue'
import List from '../List.vue'
import One from '../One.vue'
import Sample2 from '../Sample2.vue'
import Sample2b from '../Sample2b.vue'

import Sample3a from '../Sample3a.vue'
import Sample3b from '../Sample3b.vue'

import ComponentSample from '../component-sample/ComponentSample.vue'

import HotSample from '../hot-sample/HandsontableSample.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/', name: 'home', component: Home }
    , { path: '/messages', name: 'list', component: List }
    , { path: '/messages/:id', name: 'one', component: One }
    , { path: '/sample2', name: 'sample2', component: Sample2 }
    , { path: '/sample2b', name: 'sample2b', component: Sample2b }
    , { path: '/sample3a', name: 'sample3a', component: Sample3a }
    , { path: '/sample3b', name: 'sample3b', component: Sample3b }
    , { path: '/component-sample', name: 'component-sample', component: ComponentSample }
    , { path: '/hot-sample', name: 'hot-sample', component: HotSample }
]

export default new VueRouter({
    mode: 'history',
    routes
})
