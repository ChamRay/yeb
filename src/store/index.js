import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
    state:{
        routes:[],
        currentAdmin:JSON.parse(window.sessionStorage.getItem('user'))
    },
    mutations:{//同步执行的  action是异步执行的
        INIT_ADMIN(state,admin){
            //同步当前admin为admin
            state.currentAdmin = admin;
        },
        initRoutes(state,data){
            state.routes = data;
        }
    }


})