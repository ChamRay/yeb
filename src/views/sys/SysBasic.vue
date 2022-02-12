<template>
  <div>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="部门管理" name="DepMana"><depMana></depMana></el-tab-pane>
      <el-tab-pane label="职位管理" name="PosMana"><posMana></posMana></el-tab-pane>
      <el-tab-pane label="职称管理" name="JobLevelMana"><jobLevelMana></jobLevelMana></el-tab-pane>
      <el-tab-pane label="奖惩规则" name="EcMana"><ecMana></ecMana></el-tab-pane>
      <el-tab-pane label="权限组" name="PermissionMana"><permissionMana></permissionMana></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import DepMana from "@/components/sys/basic/DepMana";
import EcMana from "@/components/sys/basic/EcMana"
import JobLevelMana from "@/components/sys/basic/JobLevelMana";
import PermissionMana from "@/components/sys/basic/PermissionMana";
import PosMana from "@/components/sys/basic/PosMana";

export default {
  name: "SysBasic",
  data(){
    return{
      //默认路由页面
      activeName: 'DepMana'
    }
  },
  components:{
    DepMana,
    EcMana,
    JobLevelMana,
    PermissionMana,
    PosMana
  }
}
</script>

<style scoped>

</style>