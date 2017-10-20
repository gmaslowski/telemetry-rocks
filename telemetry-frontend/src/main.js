import Vue from 'vue'
import Vuetify from 'vuetify'
import App from './App'
import router from './router'

Vue.config.productionTip = false

// index.js or main.js
import('../node_modules/vuetify/dist/vuetify.min.css')

Vue.use(Vuetify)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {
    App
  }
})
