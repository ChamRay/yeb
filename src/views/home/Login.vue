<template>
  <div>
      <el-form :rules="rules" 
      v-loading="loading"
    element-loading-text="正在登陆"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
      ref="loginForm" :model="loginForm" class="loginContainer"> 
          <h3 class="loginTitle">系统登录</h3>
          <el-form-item prop='username'>
              <el-input type="text" auto-complete=false v-model="loginForm.username" placeholder="请输入用户名" ></el-input>
          </el-form-item>
          <el-form-item prop='password'>
              <el-input type="password" auto-complete=false v-model="loginForm.password" placeholder="请输入密码" ></el-input>
          </el-form-item>
          <el-form-item prop='code'>
              <el-input type="text" style="width:250px;margin-right:5px" auto-complete=false v-model="loginForm.code" placeholder="点击图片更换验证码" >
                  </el-input><img :src="captchaUrl" @click="updateCaptcha">
          </el-form-item>
          <el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox>
          <el-button type="primary" style="width:100%" @click="submitLogin">登录</el-button>
          
      </el-form>
  </div>
</template>

<script>

/* import {postRequest} from '@/utils/api'; */

export default {
    name:'Login',
    /* 要填入属性，否则页面会加载不出来 */
    data(){
        return{
            captchaUrl:'/captcha?time='+new Date(),/* 点击时能够刷新，防止获取到同一个验证码 */
            loginForm:{
                username:'admin',
                password:'123',
                code:''
            },
            checked:true,
            loading:false,
            rules:{
                /* 与标签中的props属性对应 */
                username:[{
                    required:true,
                    message:'请输入用户名！',
                    trigger:'blur'
                }],
                password:[{
                    required:true,
                    message:'请输入密码！',
                    trigger:'blur'
                }],
                code:[{
                    required:true,
                    message:'请输入验证码！',
                    trigger:'blur'
                }]
            }
        }
    },
    methods:{
        updateCaptcha(){
            this.captchaUrl='/captcha?time='+new Date();
        },
        submitLogin(){
            this.$refs.loginForm.validate((valid)=>{
                if(valid){
                    this.loading = true;
                    /* postRequest使用时需引入；请求到url，带着当前页面的参数loginForm then响应*/
                    this.postRequest('/login',this.loginForm).then(resp=>{
                        if(resp){
                            this.loading = false;
                            //存储用户token
                            const tokenStr = resp.obj.tokenHead+resp.obj.token;
                            window.sessionStorage.setItem('tokenStr',tokenStr);
                            //页面跳转
                            let path = this.$route.query.redirect;
                            this.$router.replace((path=='/'||path==undefined)?'/home':path);
                        }
                    })
                }else{
                    this.$message.error('请输入所有字段!')
                    return false;
                }
            })
        }
    }
}
</script>

<style>
    .loginContainer{
        border-radius: 15px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 15px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;

    }
    /* 类选择器之间不用标点分开 */
    .loginTitle{
        margin: 0px auto 40px auto;
        text-align: center;
    }
    .loginRemember{
        text-align: left;
        margin: 0px 0px 15px 0px;
        
    }
    .el-form-item__content{
        display:flex;/* 解决验证码图片错位问题 */
        align: center;
    }

</style>