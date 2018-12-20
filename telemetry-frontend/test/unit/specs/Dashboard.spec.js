import Vue from 'vue'
import Vuetify from 'vuetify'
import VueResource from 'vue-resource'
import Dashboard from '@/components/Dashboard'

Vue.use(Vuetify)
Vue.use(VueResource)

describe('Dashboard.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(Dashboard)
    new Constructor().$mount()
  })
})
