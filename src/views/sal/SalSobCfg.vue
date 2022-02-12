<template>
  <div>
    <div>
      <el-button icon="el-icon-refresh" @click="initEmps" type="primary" ></el-button>
    </div>
    <div style="margin-top: 10px">
      <div>
        <el-table
            :data="emps"
            border
            stripe>
          <el-table-column
              type="selection"
              align="left"
              width="55">
          </el-table-column>
          <el-table-column
              prop="name"
              label="姓名"
              align="left"
              fixed
              width="120">
          </el-table-column>
          <el-table-column
              prop="workID"
              label="工号"
              align="left"
              width="120">
          </el-table-column>
          <el-table-column
              prop="email"
              label="电子邮箱"
              align="left"
              width="200">
          </el-table-column>
          <el-table-column
              prop="phone"
              label="电话号码"
              align="left"
              width="120">
          </el-table-column>
          <el-table-column
              prop="department.name"
              label="部门名称"
              align="left"
              width="120">
          </el-table-column>
          <el-table-column
              label="工资账套"
              align="center">
            <template slot-scope="scope">
              <el-tooltip placement="right" v-if="scope.row.salary">
                <div slot="content">
                  <table>
                    <tr>
                      <td>基本工资</td>
                      <td>{{ scope.row.salary.basicSalary }}</td>
                    </tr>
                    <tr>
                      <td>交通补助</td>
                      <td>{{ scope.row.salary.trafficSalary }}</td>
                    </tr>
                    <tr>
                      <td>午餐补助</td>
                      <td>{{ scope.row.salary.lunchSalary }}</td>
                    </tr>
                    <tr>
                      <td>奖金</td>
                      <td>{{ scope.row.salary.bonus }}</td>
                    </tr>
                    <tr>
                      <td>养老金比率</td>
                      <td>{{ scope.row.salary.pensionPer }}</td>
                    </tr>
                    <tr>
                      <td>养老金基数</td>
                      <td>{{ scope.row.salary.pensionBase }}</td>
                    </tr>
                    <tr>
                      <td>医疗保险比率</td>
                      <td>{{ scope.row.salary.medicalPer }}</td>
                    </tr>
                    <tr>
                      <td>医疗保险基数</td>
                      <td>{{ scope.row.salary.medicalBase }}</td>
                    </tr>
                    <tr>
                      <td>公积金比率</td>
                      <td>{{ scope.row.salary.accumulationFundPer }}</td>
                    </tr>
                    <tr>
                      <td>公积金基数</td>
                      <td>{{ scope.row.salary.accumulationFundBase }}</td>
                    </tr>
                  </table>
                </div>
                <el-tag>{{scope.row.salary.name}}</el-tag>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
              label="操作"
              align="center">
            <template slot-scope="scope">
              <el-popover
                  placement="left"
                  title="编辑工资账套"
                  @show="showPop(scope.row.salary)"
                  @hide="hidePop(scope.row)"
                  width="200"
                  trigger="click">
                <div>
                  <el-select v-model="currentSalary" placeholder="请选择">
                    <el-option
                        v-for="item in salaries"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </div>
                <el-button slot="reference" type="danger">修改工资账套</el-button>
              </el-popover>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="display: flex;justify-content: flex-end;margin-top: 5px">
        <el-pagination
            background
            layout=" sizes, prev, pager, next, jumper,->,total,slot"
            @current-change="currentChange"
            @size-change="sizeChange"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SalSobCfg",
  data(){
    return{
      emps:[],
      currentPage:1,
      size:10,
      total:0,
      salaries:[],
      currentSalary:null
    }
  },
  mounted() {
    this.initEmps();
    this.initSalaries();
  },
  methods:{
    hidePop(data) {
      if (this.currentSalary&&this.currentSalary!=data.salary.id){
        this.postRequest('/salary/sobcfg/update/'+data.id+'/'+this.currentSalary).then(resp=>{
          if (resp){
            this.initEmps();
          }
        })
      }
    },
    showPop(data){
      if (data){
        this.currentSalary = data.id;
      }else {
        this.currentSalary = null;
      }
    },
    initSalaries(){
      this.getRequest('/salary/sob/listAll').then(resp=>{
        if (resp){
          this.salaries = resp;
        }
      })
    },
    currentChange(page){
      this.currentPage = page;
      this.initEmps();
    },
    sizeChange(size){
      this.size = size;
      this.initEmps();
    },
    initEmps(){
      this.postRequest('/salary/sobcfg/listAll?currentPage='+this.currentPage+'&size='+this.size).then(resp=>{
        if (resp){
          this.total = resp.total;
          this.emps = resp.data;
        }
      })
    }
  }
}
</script>

<style scoped>

</style>