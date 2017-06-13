import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
    message: 'Hello, world!'
}

const mutations = {
    updateMessage(state, message) {
        state.message = message
    }
}

const actions = {
    updateMessage(context, message) {
        context.commit('updateMessage', message)
    }
}

export default new Vuex.Store({
    state,
    mutations,
    actions
})
