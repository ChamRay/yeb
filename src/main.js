import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import {postRequest} from './utils/api';
import {putRequest} from './utils/api';
import {getRequest} from './utils/api';
import {deleteRequest} from './utils/api';
import {initMenu} from './utils/menus'
/*导入font-awesome包*/
import 'font-awesome/css/font-awesome.css';
import {downloadRequest} from "@/utils/download";

//请求方式设置成插件模式,全局this使用
//postRequest后面不能带(),否则不生效
Vue.prototype.postRequest = postRequest
Vue.prototype.getRequest = getRequest
Vue.prototype.putRequest = putRequest
Vue.prototype.deleteRequest = deleteRequest
Vue.prototype.downloadRequest = downloadRequest

Vue.use(ElementUI)/*全局设置属性，优先级略低*/
Vue.config.productionTip = false
//路由导航守卫
router.beforeEach((to,from,next)=>{
  if (window.sessionStorage.getItem('tokenStr')){
    initMenu(router,store);
    //如果用户信息不存在
    if (!window.sessionStorage.getItem('user')){
      return getRequest('/admin/info').then(resp=>{
        if (resp){
          //存入用户信息
          window.sessionStorage.setItem('user',JSON.stringify(resp));
          //存入admin
          store.commit('INIT_ADMIN',resp);
          next();
        }
      })
    }
    next();
  }else {
    if (to.path == '/'){
      next();
    }else {
      //未登录页面通过路由重定向到to.path
      next('/?redirect='+to.path);
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
