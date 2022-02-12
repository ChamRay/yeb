<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <div class="title">线上智慧办公系统</div>
                <el-dropdown class="userInfo" @command="commandHandler">
                    <span class="el-dropdown-link">
                        <div class="adminName">{{user.name}}</div><i><img :src="user.userFace"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
                        <el-dropdown-item command="setting">设置</el-dropdown-item>
                        <el-dropdown-item command="logout">注销登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu router unique-opened><!--不会同时打开页面 配合index使用-->
                        <el-submenu :index="index+''"
                                    v-for="(item,index) in routes"
                                    :key="index"
                                    v-if="!item.hidden">
                            <template slot="title">
                                <i :class="item.iconCls" style="color: #2aac8f;margin-right: 5px"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="children.path"
                                          v-for="(children,indexj) in item.children"
                                          :key="indexj">
                                {{children.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
<!--                  面包屑效果-->
                  <el-breadcrumb v-if="this.$router.currentRoute.path!='/home'">
                    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                  </el-breadcrumb>
                  <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/home'">
                    欢迎来到线上智慧办公系统！
                  </div>
<!--                  动态配置页面-->
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
export default {
    name:'Home',
    data(){
      return {
        //获取用户数据
       /* user:JSON.parse(window.sessionStorage.getItem('user'))*/
      }
    },
    computed:{
      routes(){
        return this.$store.state.routes;
      },
      user(){
        //计算属性获取
        return this.$store.state.currentAdmin;
      }
    },
  methods:{
      commandHandler(command){
        if (command=='logout') {
          this.$confirm('此操作将退出登录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            //注销登录
            this.getRequest('/logout');
            //清空session信息
            window.sessionStorage.removeItem('tokenStr');
            window.sessionStorage.removeItem('user');
            //清空菜单
            this.$store.commit('initRoutes',[]);
            //跳转到登录页
            this.$router.replace('/')
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已注销登录'
            });
          });
        }
        if (command=='userinfo'){
          this.$router.push('/userinfo');
        }
      }
  }
}


</script>

<style>
  .homeHeader{
    background: #2de58a;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 15px;
    box-sizing: border-box;
  }
  .homeHeader .title{
    font-size: 30px;
    font-family: 楷体;
    color: #480731FF;
    text-align: center;
  }
  .homeHeader .userInfo{
    cursor: pointer;/* 光标：指针 */
  }
  .el-dropdown-link img{
    width: 50px;
    height: 50px;
    border-radius: 25px;
    border: 1px;
  }
  .homeWelcome{
    text-align: center;
    font-size: 30px;
    font-family: 楷体;
    color: #2d031e;
    padding-top: 50px;
  }
  .homeRouterView{
    margin-top: 10px;
  }
  .adminName{
    float: left;
    align-items: center;
  }
</style>