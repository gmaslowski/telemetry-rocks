import Vue from 'vue'
import Router from 'vue-router'
import TelemetryDashboard from '@/components/TelemetryDashboard'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'TelemetryDashboard',
      component: TelemetryDashboard
    }
  ]
})
