<template>
    <div>
      <div>
        <div style="display: flex;justify-content: space-between">
          <div>
            <el-input clearable
                      v-model="empName"
                      prefix="el-icon-search"
                      @keydown.enter.native="initEmp"
                      style="width: 300px;margin-right: 10px"
                      @clear="initEmp"
                      :disabled="showAdvanceSearchVisible"
                      prefix-icon="el-icon-search" placeholder="请输入员工姓名进行搜索" >
            </el-input>
            <el-button type="primary" icon="el-icon-search" @click="initEmp" :disabled="showAdvanceSearchVisible">搜索</el-button>
            <el-button type="primary" @click="showAdvanceSearchVisible=!showAdvanceSearchVisible">
              <i :class="showAdvanceSearchVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'" aria-hidden="true"></i>高级搜索
            </el-button>
          </div>
          <div>
            <el-upload
                style="display: inline-flex;margin-right: 8px"
                :show-file-list="false"
                :headers="headers"
                action="/employee/basic/import"
                :before-upload="beforeUpload"
                :on-success="onSuccess"
                :on-error="onError"
                :disabled="importDataDisabled">
              <el-button type="success" :icon="importDataBtnIcon" :disabled="importDataDisabled">
                {{importDataBtnText}}
              </el-button>

            </el-upload>

            <el-button type="success" @click="exportData" icon="el-icon-download">
              导出数据
            </el-button>
            <el-button type="primary" icon="el-icon-plus" @click="showAddEmp">添加员工</el-button>
          </div>
        </div>
      </div>
      <transition name="slide-fade">
        <div style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px" v-show="showAdvanceSearchVisible">
          <el-row>
            <el-col :span="4">
              政治面貌:
              <el-select v-model="searchValue.politicId" size="mini" style="width: 130px" placeholder="政治面貌">
                <el-option
                    v-for="item in politicsStatus"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              民族：
              <el-select size="mini" style="width: 150px" v-model="searchValue.nationId" placeholder="民族">
                <el-option
                    v-for="item in nations"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>

            </el-col>
            <el-col span="4">
              职位：
              <el-select size="mini" style="width: 150px" v-model="searchValue.posId" placeholder="职位">
                <el-option
                    v-for="item in positions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>

            </el-col>
            <el-col :span="4">
              职称:
              <el-select size="mini" style="width: 150px" v-model="searchValue.jobLevelId" placeholder="职称">
                <el-option
                    v-for="item in joblevels"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              聘用形式：
              <el-radio-group v-model="searchValue.engageForm" style="margin-top: 5px">
                <el-radio label="劳动合同">劳动合同</el-radio>
                <el-radio label="劳务合同">劳务合同</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row style="margin-top: 6px">
            <el-col :span="6">
              <div>
                所属部门：
                <el-popover
                    placement="bottom"
                    title="请选择部门"
                    width="130px"
                    trigger="manual"
                    v-model="visible2">
                  <el-tree
                      default-expand-all
                      :data="allDeps"
                      :props="defaultProps"
                      @node-click="searchHandleNodeClick"></el-tree>
                  <div slot="reference" size="mini"
                       style="width: 150px;display: inline-flex;border: 1px solid;color: #dedede;height: 24px;
                       border-radius: 5px;cursor: pointer;margin-top: 6px;align-items: center;font-size: 13px;padding-left: 8px;box-sizing: border-box" @click="showDepView2" placeholder="所属部门">{{inputDepName}}
                  </div>
                </el-popover>
              </div>
            </el-col>
            <el-col :span="10">
              <div class="block">
                入职日期：
                <el-date-picker
                    size="mini"
                    v-model="searchValue.beginDateScope"
                    value-format="yyyy-MM-dd"
                    unlink-panels
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                </el-date-picker>
              </div>
            </el-col>
            <el-col :span="6" :offset="2">
              <el-button size="mini" @click="concelSearch">取消</el-button>
              <el-button icon="el-icon-search" type="primary" size="mini"  @click="initEmp('advanced')">搜索</el-button>
            </el-col>

          </el-row>
        </div>
      </transition>
      <div style="margin-top: 10px">
        <el-table
            size="small"
            :data="emps"
            stripe
            border
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0,0,0,0.8)"
            style="width: 100%">
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="name"
              label="姓名"
              fixed="left"
              align="left"
              width="50">
          </el-table-column>
          <el-table-column
              prop="workID"
              label="工号"
              width="80">
          </el-table-column>
          <el-table-column
              prop="gender"
              label="性别"
              width="30">
          </el-table-column>
          <el-table-column
              prop="birthday"
              label="生日"
              width="100">
          </el-table-column>
          <el-table-column
              prop="idCard"
              label="身份证号"
              width="160">
          </el-table-column>
          <el-table-column
              prop="wedlock"
              label="婚姻状况"
              width="50">
          </el-table-column>
          <el-table-column
              prop="nation.name"
              label="民族"
              width="50">
          </el-table-column>
          <el-table-column
              prop="nativePlace"
              label="籍贯"
              width="80">
          </el-table-column>
          <el-table-column
              prop="politicsStatus.name"
              label="政治面貌"
              width="100">
          </el-table-column>
          <el-table-column
              prop="email"
              label="邮箱"
              width="180">
          </el-table-column>‘’
          <el-table-column
              prop="phone"
              label="电话"
              width="120">
          </el-table-column>
          <el-table-column
              prop="address"
              label="地址"
              width="180">
          </el-table-column>
          <el-table-column
              prop="department.name"
              label="部门名称"
              width="90">
          </el-table-column>
          <el-table-column
              prop="joblevel.name"
              label="职级"
              width="100">
          </el-table-column>
          <el-table-column
              prop="position.name"
              label="职位"
              width="90">
          </el-table-column>
          <el-table-column
              prop="engageForm"
              label="合同类型"
              width="80">
          </el-table-column>
          <el-table-column
              prop="tiptopDegree"
              label="最高学历"
              width="50">
          </el-table-column>
          <el-table-column
              prop="specialty"
              label="专业"
              width="100">
          </el-table-column>
          <el-table-column
              prop="school"
              label="毕业院校"
              width="150">
          </el-table-column>
          <el-table-column
              prop="beginDate"
              label="入职时间"
              width="120">
          </el-table-column>
          <el-table-column
              prop="workState"
              label="在职情况"
              width="50">
          </el-table-column>
          <el-table-column
              label="合同期限"
              width="120">
            <template slot-scope="scope">
              <el-tag>{{scope.row.contractTerm}}</el-tag>年
            </template>
          </el-table-column>
          <el-table-column
              prop="conversionTime"
              label="转正日期"
              width="120">
          </el-table-column>
          <el-table-column
              prop="notWorkDate"
              label="休假日期"
              width="120">
          </el-table-column>
          <el-table-column
              prop="beginContract"
              label="合同开始日期"
              width="120">
          </el-table-column>
          <el-table-column
              prop="endContract"
              label="合同结束日期"
              width="120">
          </el-table-column>
          <el-table-column
              prop="workAge"
              label="工龄"
              width="50">
          </el-table-column>
          <el-table-column
              prop="salary"
              label="薪资账套类别"
              width="50">
          </el-table-column>
          <el-table-column
          label = "操作"
          fixed="right"
          width="280px">
            <template slot-scope="scope">
              <el-button @click="showEditEmpView(scope.row)" size="mini" style="width: 55px">编辑</el-button>
              <el-button size="mini" type="primary" style="width: 100px;justify-content: center">查看高级资料</el-button>
              <el-button @click="deleteEmp(scope.row)" size="mini" type="danger" style="width: 55px">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 10px;display: flex;justify-content: center">
          <el-pagination
              background
              @current-change="currentChange"
              @size-change="sizeChange"
              layout="sizes, prev, pager, next,jumper,->,total"
              :total="total">
          </el-pagination>
        </div>
      </div>
      <el-dialog
          :title="title"
          :visible.sync="dialogVisible"
          width="80%">
        <div>
          <el-form ref="empForm" :model="emp" :rules="rules">
            <el-row>
              <el-col :span="6">
                <el-form-item label="姓名:" prop="name">
                  <el-input v-model="emp.name" placeholder="请输入员工姓名"
                  style="width: 150px" size="mini"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <div style="flex-wrap: wrap">
                  <el-form-item label="性别:" prop="gender">
                  <el-radio-group v-model="emp.gender" style="margin-top: 13px">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                </div>
              </el-col>
              <el-col :span="6">
                <el-form-item label="出生日期:" prop="birthday" size="mini">
                  <div class="block">
                    <el-date-picker
                        v-model="emp.birthday"
                        type="date"
                        size="mini"
                        style="width: 150px"
                        value-format="yyyy-MM-dd"
                        placeholder="出生日期">
                    </el-date-picker>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="政治面貌:" prop="politicId">
                  <el-select v-model="emp.politicId" size="mini" style="width: 150px" placeholder="政治面貌">
                    <el-option
                        v-for="item in politicsStatus"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row >
              <el-col :span="6">
                <el-form-item label="民族:" prop="nationId">
                  <el-select size="mini" style="width: 150px" v-model="emp.nationId" placeholder="民族">
                    <el-option
                        v-for="item in nations"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="籍贯:" prop="nativePlace">
                  <el-input size="mini" style="width: 120px" v-model="emp.nativePlace" placeholder="请输入籍贯">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电子邮箱:" prop="email">
                  <el-input size="mini" prefix-icon="el-icon-message" style="width: 150px" v-model="emp.email" placeholder="请输入电子邮箱">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="联系地址:" prop="address">
                  <el-input size="mini" style="width: 150px" v-model="emp.address" placeholder="请输入联系地址">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
             <el-col :span="6">
               <el-form-item label="职位:" prop="posId">
                 <el-select size="mini" style="width: 150px" v-model="emp.posId" placeholder="职位">
                   <el-option
                       v-for="item in positions"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id">
                   </el-option>
                 </el-select>
               </el-form-item>
             </el-col>
              <el-col span="6">
                <el-form-item label="职称:" prop="jobLevelId">
                  <el-select size="mini" style="width: 150px" v-model="emp.jobLevelId" placeholder="职称">
                    <el-option
                        v-for="item in joblevels"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col span="6">
                <el-form-item label="所属部门:" prop="departmentId">
                  <el-popover
                      placement="bottom"
                      title="请选择部门"
                      width="200px"
                      trigger="manual"
                      v-model="visible">
                    <el-tree
                        default-expand-all
                        :data="allDeps"
                        :props="defaultProps"
                        @node-click="handleNodeClick"></el-tree>
                    <div slot="reference" size="mini"
                         style="width: 150px;display: inline-flex;border: 1px solid;color: #dedede;height: 24px;
                       border-radius: 5px;cursor: pointer;margin-top: 2px;align-items: center;font-size: 13px;padding-left: 8px;box-sizing: border-box" @click="showDepView" placeholder="所属部门">{{inputDepName}}
                    </div>
                  </el-popover>

                </el-form-item>
              </el-col>
              <el-col span="6">
                <el-form-item label="电话号码:" prop="phone">
                  <el-input size="mini" prefix-icon="el-icon-message" style="width: 150px" v-model="emp.phone" placeholder="电话号码">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="学历:" prop="tiptopDegree">
                  <el-select size="mini" style="width: 150px" v-model="emp.tiptopDegree" placeholder="学历">
                    <el-option
                        v-for="item in tiptopDegree"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col span="6">
                <el-form-item label="工号:" prop="workID">
                  <el-input disabled size="mini" prefix-icon="el-icon-message" style="width: 150px" v-model="emp.workID" placeholder="工号">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col span="6">
                <el-form-item label="毕业院校:" prop="school">
                  <el-input size="mini" prefix-icon="el-icon-message" style="width: 150px" v-model="emp.school" placeholder="毕业院校">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col span="6">
                <el-form-item label="专业名称:" prop="specialty">
                  <el-input size="mini" prefix-icon="el-icon-message" style="width: 150px" v-model="emp.specialty" placeholder="专业">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="入职日期:" prop="beginDate" size="mini">
                  <div class="block">
                    <el-date-picker
                        v-model="emp.beginDate"
                        type="date"
                        size="mini"
                        style="width: 150px"
                        value-format="yyyy-MM-dd"
                        placeholder="入职日期">
                    </el-date-picker>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="转正日期:" prop="conversionTime" size="mini">
                  <div class="block">
                    <el-date-picker
                        v-model="emp.conversionTime"
                        type="date"
                        size="mini"
                        style="width: 150px"
                        value-format="yyyy-MM-dd"
                        placeholder="转正日期">
                    </el-date-picker>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="合同起始日期:" prop="beginContract" size="mini">
                  <div class="block">
                    <el-date-picker
                        v-model="emp.beginContract"
                        type="date"
                        size="mini"
                        style="width: 150px"
                        value-format="yyyy-MM-dd"
                        placeholder="合同起始日期">
                    </el-date-picker>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="合同截止日期:" prop="endContract" size="mini">
                  <div class="block">
                    <el-date-picker
                        v-model="emp.endContract"
                        type="date"
                        size="mini"
                        style="width: 150px"
                        value-format="yyyy-MM-dd"
                        placeholder="合同截止日期">
                    </el-date-picker>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
             <el-col :span="8">
               <el-form-item label="身份证号:" prop="idCard">
                 <el-input size="mini" prefix-icon="el-icon-message" style="width: 150px" v-model="emp.idCard" placeholder="请输入身份证号">
                 </el-input>
               </el-form-item>
             </el-col>
              <el-col :span="8">
                <el-form-item label="聘用形式:" prop="engageForm">
                  <el-radio-group v-model="emp.engageForm" style="margin-top: 13px">
                    <el-radio label="劳动合同">劳动合同</el-radio>
                    <el-radio label="劳务合同">劳务合同</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="性别:" prop="wedlock">
                  <el-radio-group v-model="emp.wedlock" style="margin-top: 13px">
                    <el-radio label="已婚">已婚</el-radio>
                    <el-radio label="未婚">未婚</el-radio>
                    <el-radio label="离异">离异</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doAddEmp">确 定</el-button>
  </span>
      </el-dialog>
    </div>
</template>

<script>
export default {
  name:'EmpBasic',
  data(){
      return{
        showAdvanceSearchVisible:false,
        headers:{
          Authorization:window.sessionStorage.getItem('tokenStr')
        },
        importDataDisabled:false,
        importDataBtnText:'导入数据',
        importDataBtnIcon:'el-icon-upload2',
        title:'',
        defaultProps:{
          children:'children',
          label:'name'
        },
        allDeps:[],
        visible:false,
        visible2:false,
        total:0,
        emps:[],
        loading:false,
        currentPage:1,
        size:10,
        empName:'',
        dialogVisible:false,
        emp:{
          id:null,
          name: "",
          gender: "",
          birthday: "",
          idCard: "",
          wedlock: "",
          nationId: null,
          nativePlace: "",
          politicId: null,
          email: "",
          phone: "",
          address: "",
          departmentId: null,
          jobLevelId: null,
          posId: null,
          engageForm: "",
          tiptopDegree: "",
          specialty: "",
          school: "",
          beginDate: "",
          workState: "在职",
          workID: "",
          contractTerm: null,
          conversionTime: "",
          notWorkDate: null,
          beginContract: "",
          endContract: "",
          workAge: null,
          salaryId: null,
        },
        nations:[],
        joblevels:[],
        politicsStatus:[],
        positions:[],
        tiptopDegree:['博士','硕士','本科','专科','高中','初中','小学','其他'],
        inputDepName:'',
        rules:{
          name:[{required:true,message:'请输入员工姓名',trigger:'blur'}],
          gender:[{required:true,message:'请输入员工性别',trigger:'blur'}],
          birthday:[{required:true,message:'请输入员工生日',trigger:'blur'}],
          idCard:[{required:true,message:'请输入员工身份证号',trigger:'blur'},
            {pattern:/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}$)/,message: '身份证号码不正确',trigger: 'blur'}],
          wedlock:[{required:true,message:'请输入员工婚姻状况',trigger:'blur'}],
          nationId:[{required:true,message:'请输入员工民族',trigger:'blur'}],
          nativePlace:[{required:true,message:'请输入员工籍贯',trigger:'blur'}],
          politicId:[{required:true,message:'请输入员工政治面貌',trigger:'blur'}],
          email:[{required:true,message:'请输入员工电子邮箱',trigger:'blur'},{type:'email',message:'邮箱格式不正确',trigger:'blur'}],
          phone:[{required:true,message:'请输入员工电话',trigger:'blur'}],
          address:[{required:true,message:'请输入员工地址',trigger:'blur'}],
          departmentId:[{required:true,message:'请输入员工部门',trigger:'blur'}],
          jobLevelId:[{required:true,message:'请输入员工职称',trigger:'blur'}],
          posId:[{required:true,message:'请输入员工职位id',trigger:'blur'}],
          engageForm:[{required:true,message:'请输入员工姓名',trigger:'blur'}],
          tiptopDegree:[{required:true,message:'请输入员工最高学历',trigger:'blur'}],
          specialty:[{required:true,message:'请输入员工专业',trigger:'blur'}],
          school:[{required:true,message:'请输入员工毕业院校',trigger:'blur'}],
          beginDate:[{required:true,message:'请输入员工入职时间',trigger:'blur'}],
          workID:[{required:true,message:'请输入员工工号',trigger:'blur'}],
          conversionTime:[{required:true,message:'请输入员工转正日期',trigger:'blur'}],
          notWorkDate:[{required:true,message:'请输入员工工作状态',trigger:'blur'}],
          beginContract:[{required:true,message:'请输入员工合同开始日期',trigger:'blur'}],
          endContract:[{required:true,message:'请输入员工合同结束日期',trigger:'blur'}],
          workAge:[{required:true,message:'请输入员工工龄',trigger:'blur'}]
        },
        searchValue:{
          nationId:null,
          politicId:null,
          posId:null,
          jobLevelId:null,
          engageForm:'',
          departmentId:null,
          beginDateScope:null
        }
      }
  },
  mounted() {
    this.initEmp();
    this.initData();
  },
  methods:{
    concelSearch(){
      this.searchValue={
        nationId:null,
            politicId:null,
            posId:null,
            jobLevelId:null,
            engageForm:'',
            departmentId:null,
            beginDateScope:null
      };
      this.initEmp();
    },
    showDepView2(){
      this.visible2 =!this.visible2;
    },
    searchHandleNodeClick(data){
      this.inputDepName = data.name;
      this.searchValue.departmentId = data.id;
      this.visible2 =!this.visible2;
    },
    onSuccess(){
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataBtnText = '导入数据';
      this.importDataDisabled = false;
      this.initEmp();
    },
    onError(){
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataBtnText = '导入数据';
      this.importDataDisabled = false;
    },
    beforeUpload(){
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataBtnText = '正在导入';
      this.importDataDisabled = true;
    },
    exportData(){
      alert('准备下载');
      this.downloadRequest('/employee/basic/export');
    },
    showEditEmpView(data){
      this.title='编辑员工信息';
      this.inputDepName=data.department.name;
      this.emp =data;
      this.dialogVisible =true;
    },
    deleteEmp(data){
      this.$confirm('此操作将永久删除['+data.name+'], 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/employee/basic/delete/'+data.id).then((resp) => {
          if (resp){
            this.initEmp();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    doAddEmp(){
      if (this.emp.id){
        this.$refs['empForm'].validate(valid=>{
          if (valid){
            this.putRequest('/employee/basic/update',this.emp).then(resp=>{
              if (resp){
                this.dialogVisible = false;
                this.initEmp();
              }
            })
          }
        });
      }else {
        this.$refs['empForm'].validate(valid=>{
          if (valid){
            this.postRequest('/employee/basic/add',this.emp).then(resp=>{
              if (resp){
                this.dialogVisible = false;
                this.initEmp();
              }
            })
          }
        });
      }
    },
    handleNodeClick(data){
      this.inputDepName = data.name;
      this.emp.departmentId = data.id;
      this.visible =!this.visible;
    },
    showDepView(){
      this.visible =!this.visible;
    },
    getMaxWorkID(){
      this.getRequest('/employee/basic/maxId').then(resp=>{
        if (resp){
          this.emp.workID =resp.obj;
        }
      })
    },
    initData(){
      if (!window.sessionStorage.getItem('nations')){
        this.getRequest('/employee/basic/nation').then(resp=>{
          if (resp){
            this.nations = resp;
            window.sessionStorage.setItem('nations',JSON.stringify(resp));
          }
        })
      }else {
        this.nations =JSON.parse(window.sessionStorage.getItem('nations'));
      }
      if (!window.sessionStorage.getItem('joblevels')){
        this.getRequest('/system/basic/joblevel/listAll').then(resp=>{
          if (resp){
            this.joblevels = resp;
            window.sessionStorage.setItem('joblevels',JSON.stringify(resp));
          }
        })
      }else {
        this.joblevels =JSON.parse(window.sessionStorage.getItem('joblevels'));
      }
      if (!window.sessionStorage.getItem('politicsStatus')){
        this.getRequest('/employee/basic/politicsStatusAll').then(resp=>{
          if (resp){
            this.politicsStatus = resp;
            window.sessionStorage.setItem('politicsStatus',JSON.stringify(resp));
          }
        })
      }else {
        this.politicsStatus =JSON.parse(window.sessionStorage.getItem('politicsStatus'));
      }
      if (!window.sessionStorage.getItem('positions')){
        this.getRequest('/system/basic/pos/listAll').then(resp=>{
          if (resp){
            this.positions = resp;
            window.sessionStorage.setItem('positions',JSON.stringify(resp));
          }
        })
      }else {
        this.positions =JSON.parse(window.sessionStorage.getItem('positions'));
      }
      if (!window.sessionStorage.getItem('allDeps')){
        this.getRequest('/system/basic/department/listAll').then(resp=>{
          if (resp){
            this.allDeps = resp;
            window.sessionStorage.setItem('allDeps',JSON.stringify(resp));
          }
        })
      }else {
        this.allDeps =JSON.parse(window.sessionStorage.getItem('allDeps'));
      }
    },
    showAddEmp(){
      this.title='添加员工';
      this.emp={
        id:null,
        name: "",
            gender: "",
            birthday: "",
            idCard: "",
            wedlock: "",
            nationId: null,
            nativePlace: "",
            politicId: null,
            email: "",
            phone: "",
            address: "",
            departmentId: null,
            jobLevelId: null,
            posId: null,
            engageForm: "",
            tiptopDegree: "",
            specialty: "",
            school: "",
            beginDate: "",
            workState: "在职",
            workID: "",
            contractTerm: null,
            conversionTime: "",
            notWorkDate: null,
            beginContract: "",
            endContract: "",
            workAge: null,
            salaryId: null,
      };
      this.inputDepName='';
      this.getMaxWorkID();
      this.dialogVisible = true;
    },
    initEmp(type){
      this.loading = true;
      let url = '/employee/basic/listAll?currentPage='+this.currentPage+'&size='+this.size;
      if (type && type=='advanced'){
        if(this.searchValue.politicId){
          url+='&politicId=' +this.searchValue.politicId
        }
        if(this.searchValue.nationId){
          url+='&nationId=' +this.searchValue.nationId
        }
        if(this.searchValue.posId){
          url+='&posId=' +this.searchValue.posId
        }
        if(this.searchValue.jobLevelId){
          url+='&jobLevelId=' +this.searchValue.jobLevelId
        }
        if(this.searchValue.engageForm){
          url+='&engageForm=' +this.searchValue.engageForm
        }
        if(this.searchValue.departmentId){
          url+='&engageForm=' +this.searchValue.departmentId
        }
        if(this.searchValue.beginDateScope){
          url+='&beginDateScope=' +this.searchValue.beginDateScope
        }
      }else {
        url+='&name='+this.empName;
      }
      this.postRequest(url).then(resp=>{
        this.loading = false;
        if (resp){
          this.total = resp.total;
          this.emps = resp.data;
        }
      })
    },
    currentChange(currentPage){
      this.currentPage = currentPage;
      this.initEmp();
    },
    sizeChange(size){
      this.size = size;
      this.initEmp();
    }
  }
}
</script>

<style>
.slide-fade-enter-active {
  transition: all .4s ease;
}
.slide-fade-leave-active {
  transition: all .5s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>