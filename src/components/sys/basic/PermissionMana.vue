<template>
<div>
  <div>
    <div class="permissionManaTool">
      <el-input size="small" placeholder="请输入角色英文名" v-model="role.name" @keydown.en.enter.native="doAddRole">
        <template slot="prepend">ROLE_</template>
      </el-input>
      <el-input size="small" v-model="role.nameZh" placeholder="请输入角色中文名称"
                @keydown.en.enter.native="doAddRole"></el-input>
      <el-button size="small" icon="el-icon-plus" type="primary"
                 @click="doAddRole">添加角色</el-button>
    </div>
    <div class="permissionManaMain">
      <el-collapse v-model="activeName" accordion @change = 'change'>
        <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r,index) in roles" :key="index">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>可访问资源</span>
              <el-button style="float: right; padding: 3px 0;color: red"
                         type="text" icon="el-icon-delete" @click="deleteRole(r)"></el-button>
            </div>
<!--            注意处理子菜单-->
            <div>
<!--              key中传入index是 为了防止添加角色后不会出现循环空间-->
              <el-tree
                  show-checkbox
                  :data="allMenus"
                  :props="defaultProps"
                  ref="tree"
                  :key="index"
                  :default-checked-keys="selectedManus"
                  node-key="id"></el-tree>
              <div style="display: flex;justify-content: flex-end">
                <el-button size="mini" @click="cancelUpdate">取消修改</el-button>
                <el-button size="mini" type="primary"
                           @click="doUpdate(r.id,index)">确认修改</el-button>
              </div>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</div>
</template>

<script>



export default {
  name: "PermissionMana",
  data(){
    return{
      role:{
        name:'',
        nameZh:''
      },
      roles:[],
      allMenus:[],
      //
      defaultProps:{
        //对应allMenus中的属性，即能直接渲染显示
        children: 'children',
        label:'name'
      },
      selectedManus:[],
      activeName:-1
    }
  },
  mounted() {
    this.initRoles();
  },
  methods:{
    deleteRole(role){
      this.$confirm('此操作将永久删除['+role.nameZh+']职位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/permiss/delete/'+role.id).then((resp) => {
          if (resp){
            this.initRoles();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    doAddRole(){
      if (this.role.name&& this.role.nameZh){
        this.postRequest('/system/basic/permiss/add',this.role).then(resp=>{
          if (resp){
            this.initRoles();
            this.role.name = '';
            this.role.nameZh='';
          }
        })
      }else {
        this.$message.error("所有字段不能为空！");
      }
    },
    cancelUpdate(){
      this.activeName = -1;
    },
    doUpdate(rid,index){
      let tree = this.$refs.tree[index];
      let selectedKeys = tree.getCheckedKeys(true);
      let url = '/system/basic/permiss/updateMenuRole?rid='+rid;
      selectedKeys.forEach(key=>{
        /*注意，参数名称要与后端相同，不同会出错*/
        url+='&mid='+key;
      });
      this.putRequest(url).then(resp=>{
        if (resp){
          this.activeName = -1;
        }
      })
    },
    change(rid){
      if (rid){
        this.initSelectedMenus(rid);
        this.initAllMenus();
      }
    },
    initSelectedMenus(rid){

        this.getRequest('/system/basic/permiss/mid/'+rid).then(response => {
          if (response){
            this.selectedManus = response;
          }
        })

    },
    initAllMenus(){
      this.getRequest('/system/basic/permiss/menus').then(resp=>{
        if (resp){
          this.allMenus = resp;
        }
      })
    },
    handleAdd(){
      this.postRequest().then(resp=>{
        if (resp){
          this.initRoles();

        }
      })
    },
    initRoles(){
      this.getRequest('/system/basic/permiss/listAll').then(resp=>{
        if (resp){
          this.roles=resp;
        }
      })
    }

  }
}
</script>

<style scoped>
  .permissionManaTool{
    display: flex;
    justify-content: flex-start;
  }
  .permissionManaTool el-input{
    width: 300px;
    margin-right: 6px;

  }
  .permissionManaMain{
    margin-top: 10px;
    width: 100%;
  }
</style>