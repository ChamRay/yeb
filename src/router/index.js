import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/home/Login.vue'
import AdminInfo from '../views/home/AdminInfo'
import Home from '../views/home/Home'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
    /*登陆页面不用循环展示*/
    hidden:true
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '/userinfo',
        name: '个人中心',
        component: AdminInfo,
      }
    ]
  }

  
]

const router = new VueRouter({
  routes
})

export default router
