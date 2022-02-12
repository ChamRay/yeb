<template>
  <div>
    <div>
      <el-input
          size="small"
          class="addPosInput"
          placeholder="添加职位"
          suffix-icon="el-icon-plus"
          @keydown.enter.native = "handleAdd"
          v-model="pos.name">
      </el-input>
      <el-button size="small" icon="el-icon-plus" type="primary" @click="handleAdd">添加</el-button>
    </div>
    <div class="posManaMain">
      <el-table
          stripe
          border
          @selection-change="handleSelectionChange"
          :data="positions"
          style="width: 100%">
        <!--斑马纹 和 边框-->
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="id"
            label="编号"
            width="70%">
        </el-table-column>
        <el-table-column
            prop="name"
            label="职位"
            width="120">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="创建时间"
            width="200">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button type="danger" size="small" style="margin-top: 8px" :disabled="this.multipleSelection.length==0"
    @click="deleteMany">>批量删除</el-button>
    <el-dialog
        title="编辑职位"
        :visible.sync="dialogVisible"
        width="30%">
      <div>
        <el-tag type="success">职位名称</el-tag>
        <el-input v-model="updatePos.name" size="small" class="updatePosInput"></el-input>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button size="small" @click="dialogVisible = false">取 消</el-button>
    <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PosMana",
  data(){
    return{
      pos:{
        name:''
      },
      positions:[],
      dialogVisible:false,
      updatePos:{
        name:''
      },
      //对应@selection-change="handleSelectionChange"
      multipleSelection:[]
    }
  },
  mounted() {
    this.initPositions();
  },
  methods:{
    deleteMany(){
      this.$confirm('此操作将永久删除['+this.multipleSelection.length+']条职位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?';
        this.multipleSelection.forEach(item=>{
          ids+='ids=' +item.id+'&';
        })
        this.deleteRequest('/system/basic/pos/deleteBatch'+ids).then((resp) => {
          if (resp){
            this.initPositions();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    doUpdate(){
      this.postRequest('/system/basic/pos/update',this.updatePos).then(resp=>{
        if (resp){
          this.initPositions();
          this.dialogVisible = false;
        }
      });
    },
    //添加事件
    handleAdd(){
      if (this.pos.name){
        this.postRequest('/system/basic/pos/add',this.pos).then(resp=>{
          //存在则立即展示信息
          if (resp){
            this.initPositions();
            //搜索框始终保持为空
            this.pos.name='';
          }
        })
      }else {
        this.$message.error('职位名称不能为空！');
      }
    },
    //查询事件
    handleQuery(){

    },
    //编辑事件
    handleEdit(index,data){
      Object.assign(this.updatePos,data);
      //this.updatePos = data; 使用赋值会对表单进行同步修改，取消修改后刷新才能还原
      //不必修改，保持为空
      this.updatePos.createDate ='';
      this.dialogVisible = true;
    },
    //删除事件
    handleDelete(index,data){
      this.$confirm('此操作将永久删除['+data.name+']职位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/pos/delete/'+data.id).then((resp) => {
          if (resp){
            this.initPositions();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    initPositions(){
      this.getRequest('/system/basic/pos/listAll').then(resp=>{
        if (resp){
          this.positions = resp;
        }
      })
    }
  }
}
</script>

<style scoped>
  .addPosInput{
    width: 300px;
    margin-right: 5px;
  }
  .posManaMain{
    margin-top: 10px;

  }
  .updatePosInput{
    width: 120px;
    margin-left: 5px;
  }

</style>