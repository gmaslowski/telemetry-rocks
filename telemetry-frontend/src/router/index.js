import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Dashboard from '@/components/Dashboard'
import Analysis from '@/components/Analysis'
import LocalChallenge from '@/components/LocalChallenge'

Vue.use(Router)

export default new Router({
  routes: [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/analysis',
    name: 'Analysis',
    component: Analysis
  },
  {
    path: '/localchallenge',
    name: 'LocalChallenge',
    component: LocalChallenge
  }]
})
