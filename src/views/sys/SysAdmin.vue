<template>
  <div>
    <div style="display: flex;justify-content: center;margin-top: 10px">
      <el-input v-model="keywords" placeholder="请输入用户名搜索用户" prefix-icon="el-icon-search" style="width: 60%;margin-right: 10px"></el-input>
      <el-button type="primary" prefix-icon="el-icon-search"
                 style="width: 20%" @click="doSearch">搜索</el-button>
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin,index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span >{{admin.name}}</span>
          <el-button @click="deleteAdmin(admin)" icon="el-icon-delete" style="float: right; padding: 3px 0;color: red" type="text"></el-button>
        </div>
        <div>
          <div class="img-container">
            <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="userFace-img">
          </div>
        </div>
        <div class="userInfo-container">
          <div>用户名：{{admin.name}}</div>
          <div>手机号码：{{admin.phone}}</div>
          <div>电话号码：{{admin.telephone}}</div>
          <div>地址：{{admin.address}}</div>
          <div>用户状态：
            <el-switch
                style="margin-left: 6px"
                v-model="admin.enabled"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="enabledChange(admin)"
                active-text="已启用"
                inactive-text="未启用">
            </el-switch>
          </div>
          <div>
            用户角色：
            <el-tag style="margin-right: 4px" type="success" v-for="(role,indexj) in admin.roles"
                    :key="indexj">{{role.nameZh}}</el-tag>
            <el-popover
                placement="bottom"
                title="角色列表"
                width="200"
                @show="showPop(admin)"
                @hide="hidePop(admin)"
                trigger="click">
                <el-select v-model="selectRoles" multiple placeholder="请选择">
                  <el-option
                      v-for="(r,indexi) in allRoles"
                      :key="indexi"
                      :label="r.nameZh"
                      :value="r.id">
                  </el-option>
                </el-select>
              <el-button slot="reference" type="text" icon="el-icon-more"></el-button>
            </el-popover>
          </div>
          <div>
            备注：{{admin.remark}}
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "SysAdmin",
  data(){
    return{
      admins:[],
      keywords:'',
      allRoles:[],
      selectRoles:[]
    }
  },
  mounted() {
    this.initAdmins();
  },
  methods:{
    hidePop(admin){
      let roles = [];
      Object.assign(roles,admin.roles);
      let flag = false;
      if (roles.length !=this.selectRoles.length){
        flag = true;
      }else {
        for (let i=0;i<roles.length;i++){
          let role = roles[i];
          for (let j=0;j<this.selectRoles.length;j++){
            let sr = this.selectRoles[j];
            if (role.id==sr){
              roles.splice(i,1);
              i--;
              break;
            }
          }
        }
        if (roles.length!=0){
          flag = true;
        }
      }
      if (flag){
        let url = '/system/admin/updateAdminRole?adminId='+admin.id;
        this.selectRoles.forEach(sr=>{
          if (sr!=null){
            url += '&rids='+ sr;
          }
        });
        this.putRequest(url).then(resp=>{
          if (resp){
            this.initAdmins();
          }
        })
      }
    },
    showPop(admin){
      this.initAllRoles();
      let roles = admin.roles;
      this.selectRoles=[];
      roles.forEach(r=>{
        this.selectRoles.push(r.id);
      });
    },
    initAllRoles(){
      this.getRequest('/system/basic/permiss/listAll').then(resp=>{
        if (resp){
          this.allRoles = resp;
        }
      })
    },
    enabledChange(admin){
      this.postRequest('/system/admin/update',admin).then(resp=>{
        if (resp){
          this.initAdmins();
        }
      })
    },
    deleteAdmin(admin){
      this.$confirm('此操作将永久删除管理员['+admin.name+'], 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/admin/delete/'+admin.id).then((resp) => {
          if (resp){
            this.initAdmins();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    doSearch(){
      this.initAdmins();
    },
    initAdmins(){
      this.getRequest('/system/admin/listAll?keywords='+this.keywords).then(resp=>{
        if (resp){
          this.admins = resp;
        }
      })
    }
  }
}
</script>

<style scoped>
  .admin-card{
    width: 30%;
    margin-top: 10px;
    border-radius: 10px;
  }
  .admin-container{
    display: flex;
    justify-content: space-around;
    margin-bottom: 20px;
    /*自动换行*/
    flex-wrap: wrap;

  }
  .userFace-img{
    width: 50px;
    height: 50px;
    border-radius: 25px;
  }
  .img-container{
    width: 100%;
    display: flex;
  }
  .userInfo-container{
    font-size: 12px;
    color: #409eff;
  }

</style>