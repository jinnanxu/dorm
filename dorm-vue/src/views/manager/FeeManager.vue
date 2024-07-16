<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item>水电费管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="楼宇" prop="buildingId">
        <el-select v-model="formInline.buildingId" placeholder="请选择" clearable size="small" @change="onChangeBuilding" style="width: 130px;">
          <el-option
            v-for="item in allBuildingList"
            :key="item.buildingId"
            :label="item.buildingName"
            :value="item.buildingId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="宿舍" prop="dormId">
        <el-select v-model="formInline.dormId" placeholder="请选择" clearable size="small" style="width: 100px;">
          <el-option
            v-for="item in dormList"
            :key="item.dormitoryId"
            :label="item.roomNo"
            :value="item.dormitoryId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="year">
        <el-date-picker
          size="small"
          v-model="formInline.dateRange"
          value-format="yyyy-MM"
          type="monthrange"
          range-separator="至"
          start-placeholder="开始月份"
          end-placeholder="结束月份">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="类型" prop="feeType">
          <el-select v-model="formInline.feeType" clearable size="small" style="width: 100px;">
            <el-option label="电费" :value="1"></el-option>
            <el-option label="水费" :value="2"></el-option>
          </el-select>
        </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="search"
          >搜索</el-button
        >
        <el-button
          size="small"
          type="success"
          icon="el-icon-plus"
          @click="handleEdit()"
          >新增账单</el-button
        >
      </el-form-item>
    </el-form>
    <!--列表 toyId toyName location floor price status-->
    <el-table
      size="small"
      :data="listData"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="拼命加载中"
      style="width: 100%"
    >
      <el-table-column align="center" prop="building.buildingName" label="楼宇/宿舍号">
        <template slot-scope="scope">
          <span>{{ scope.row.building.buildingName}}</span>
          <span>{{ scope.row.dorm.roomNo}}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="student.college" label="院系">
      </el-table-column> -->
      <el-table-column align="center" prop="student.majorClass" label="费用类型">
        <template slot-scope="scope">
          <span v-if="scope.row.feeType==1">电费</span>
          <span v-else>水费</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="billYear" label="账单月度">
        <template slot-scope="scope">
          <strong class="success">{{ scope.row.billYear }}年{{ scope.row.billMonth }}月</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="unitPrice" label="水电度量">
        <template slot-scope="scope">
          <span v-if="scope.row.feeType==1">{{ scope.row.usages }}度</span>
          <span v-else>{{ scope.row.usages }}吨</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="费用总额">
        <template slot-scope="scope">
          ￥<strong class="warning">{{ scope.row.amount|amountString }}</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="status" label="缴费状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">已交费</el-tag>
          <el-tag v-else-if="scope.row.status == 0" type="danger" size="small">待交费</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="mobile" label="缴费时间">
        <template slot-scope="scope">
          <span class="warning" v-if="scope.row.checkOutTime!=null">{{ scope.row.checkOutTime|timestampToDate }}</span>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="操作" min-width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="offShelf(scope.$index, scope.row, 1)" icon="el-icon-check" v-if="scope.row.status==0">已收费</el-button>
          <el-button size="mini" type="warning" @click="sendMsg(scope.row)" icon="el-icon-bell" v-if="scope.row.status==0">催缴</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      v-bind:child-msg="pageparm"
      @callFather="callFather"
    ></Pagination>

    <el-dialog
      :title="dialogTitle"
      :visible.sync="editFormVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="费用类型" prop="feeType">
          <el-select v-model="editForm.feeType" placeholder="请选择" size="small">
            <el-option label="电费" value="1"></el-option>
            <el-option label="水费" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="楼宇" prop="buildingId">
          <el-select v-model="editForm.buildingId" placeholder="请选择" size="small" @change="onChangeBuilding">
            <el-option
              v-for="item in allBuildingList"
              :key="item.buildingId"
              :label="item.buildingName"
              :value="item.buildingId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="宿舍" prop="dormId">
          <el-select v-model="editForm.dormId" placeholder="请选择" size="small" filterable>
            <el-option
              v-for="item in dormList"
              :key="item.dormitoryId"
              :label="item.roomNo"
              :value="item.dormitoryId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="账单月份" prop="billYear">
          <el-date-picker
            v-model="billDate"
            type="month"
            value-format="yyyy-MM"
            placeholder="请选择">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="水/电用量" prop="usages">
          <el-input
            size="small"
            v-model="editForm.usages"
            auto-complete="off"
            type="number"
            step="0.1"
            @blur="calAmount"
          ></el-input>
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input
            size="small"
            v-model="editForm.unitPrice"
            auto-complete="off"
            type="number"
            step="0.01"
            @blur="calAmount"
          ></el-input>
        </el-form-item>
        <el-form-item label="费用总额" prop="amount">
          <el-input
            size="small"
            v-model="editForm.amount"
            auto-complete="off"
            type="number"
            step="0.01"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          :loading="loading"
          class="title"
          @click="submitForm('editForm')"
          >保存</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from "../../components/Pagination.vue";
import { ManagerGetFeeList, ManagerUpdateFeeStatus, ManagerSaveFee, GetAllBuildingList, AdminGetDormByBuilding,AdminGetAllStudentList,ManagerSendFeeMsg } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      leaveFormVisible: false, //详情对话框显示与隐藏
      dialogTitle: "添加用户",
      editForm: {
        feeId: '',
        feeType: '',
        dormId: '',
        remark: '',
        buildingId: '',
        billYear: '',
        billMonth: '',
        status: 0,
        amount: '',
        unitPrice: '',
        usages: ''
      },
      billDate: '',
      // rules表单验证 
      rules: {
        dormId: [{ required: true, message: '请选择', trigger: 'blur' }],
        feeType: [{ required: true, message: '请选择', trigger: 'blur' }],
        buildingId: [{ required: true, message: '请选择', trigger: 'blur' }],
      },
      formInline: {
        currentpage: 1,
        pageSize: 10,
        buildingId: "",
        dormId: "",
        year: '',
        month: '',
        feeType: '',
        dateRange: ''
      },
      listData: [], //用户数据
      allBuildingList: [],
      dormList: [],
      allStudent: [],
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10,
      },
      leaveData: null
    };
  },
  // 注册组件
  components: {
    Pagination,
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    this.getdata(this.formInline)
    GetAllBuildingList({}).then((res) => {
      if (res.success) {
        this.allBuildingList = res.data
      }
    })
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取列表数据
    getdata(parameter) {
      this.loading = true;
      ManagerGetFeeList(parameter)
        .then((res) => {
          this.loading = false;
          if (res.success) {
            this.listData = res.data.records;
            this.pageparm.currentPage = res.data.currentPage;
            this.pageparm.pageSize = res.data.pageSize;
            this.pageparm.total = res.data.totalrecord;
          } else {
            this.$message({
              type: "info",
              message: res.msg,
            });
          }
        })
        .catch((err) => {
          this.editFormVisible = false;
          this.loading = false;
          this.$message.error("获取数据失败，请稍后再试！");
        });
    },
    // 分页插件事件
    callFather(parm) {
      console.log('分页插件事件', parm)
      this.formInline.currentpage=parm.currentPage
      this.formInline.pageSize=parm.pageSize
      this.getdata(this.formInline);
    },
    // 搜索事件
    search() {
      console.log('查询条件：', this.formInline)
      this.getdata(this.formInline);
    },

    onChangeBuilding(val){
      AdminGetDormByBuilding({buildingId: val}).then((res) => {
        if (res.success) {
          this.dormList = res.data
        }
      })
    },

    calAmount(){
      if(this.editForm.unitPrice!=''&&this.editForm.usages!=''){
        console.log('自动计算总额')
        this.editForm.amount = (parseFloat(this.editForm.unitPrice * this.editForm.usages)).toFixed(2)
      }
    },

    //显示编辑界面
    handleEdit: function (index, row) {
      AdminGetAllStudentList({}).then((res) => {
        if (res.success) {
          this.allStudent = res.data
        }
      })
      this.editFormVisible = true;
      // 默认新增
      if (row != undefined && row != "undefined") {
        // 更新
        this.dialogTitle = '修改账单信息'
        this.editForm.feeId = row.feeId;
        this.editForm.feeType = row.feeType;
        this.editForm.dormId = row.dormId;
        this.editForm.remark = row.remark;
        this.editForm.status = row.status;
        this.editForm.buildingId = row.buildingId;
        this.editForm.billMonth = row.billMonth;
        this.editForm.amount = row.amount;
        this.editForm.unitPrice = row.unitPrice;
        this.editForm.usages = row.usages;
      }else{
        this.dialogTitle = '添加账单信息'
        this.editForm.feeId=''
        this.editForm.feeType = ''
        this.editForm.dormId = ''
        this.editForm.remark = ''
        this.editForm.status = 0
        this.editForm.buildingId = ''
        this.editForm.billMonth = ''
        this.editForm.amount = ''
        this.editForm.unitPrice = ''
        this.editForm.usages = '',
        this.billDate=''
      }
    },

    // 提交表单
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          let year = this.billDate.split('-')[0]
          let month = this.billDate.split('-')[1]
          this.editForm.billYear = year
          this.editForm.billMonth = month
          ManagerSaveFee(this.editForm)
            .then((res) => {
              this.editFormVisible = false;
              this.leaveFormVisible = false;
              this.loading = false;
              if (res.success) {
                this.getdata(this.formInline);
                this.$message({
                  type: "success",
                  message: "更新成功！",
                });
                this.photos=[]
              } else {
                this.$message({
                  type: "info",
                  message: res.msg,
                });
              }
            })
        } else {
          return false;
        }
      });
    },
    // 确认收款
    offShelf(index, row, status) {
      let msg = "确定已线下收款吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          ManagerUpdateFeeStatus({feeId: row.feeId, status: status})
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "操作成功!",
                });
                this.getdata(this.formInline);
              } else {
                this.$message({
                  type: "info",
                  message: res.msg,
                });
              }
            })
            .catch((err) => {
              console.log(err)
              this.loading = false;
              this.$message.error("操作失败，请稍后再试！");
            });
        }).catch((err) => {
          console.log(err)
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    //发送催缴通知
    sendMsg(row) {
      let msg = "确定要发送催缴通知吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        ManagerSendFeeMsg({feeId: row.feeId})
          .then((res) => {
            if (res.success) {
              this.$message({
                type: "success",
                message: "操作成功!",
              });
            } else {
              this.$message({
                type: "info",
                message: res.msg,
              });
            }
          }).catch((err) => {
            console.log(err)
            this.loading = false;
            this.$message.error("操作失败，请稍后再试！");
          });
      })
    },
    //显示编辑界面
    handleDetail: function (index, row) {
      this.detailVisible = true
    },
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
    },
    closeDialog2() {
      this.leaveFormVisible = false;
    },
  },
};
</script>

<style scoped>
.user-search {
  margin-top: 20px;
}
.userRole {
  width: 100%;
}
.avatar-uploader /deep/ .el-upload {
    border-radius: 6px;
    background-color: #fff;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100%;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #41a8a8;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    border: 1px dashed #8c939d;
    width: 180px;
    height: 140px;
    line-height: 140px;
    text-align: center;
  }
  .showUploader /deep/ .el-upload-list{
    display: none;
  }
  .avatar {
    width: 100%;
    height: 140px;
    display: block;
  }
  .img-list{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }
  .img-list .img-li{
    display: inline-block;
    margin-right: 1%;
    width: 32%;
    margin-bottom: 5px;
  }
  .demo-image{
    margin-top: 15px;
  }
  .blue{
    color: #409EFF;
  }
  .success{
    color: #67C23A;
  }
  .warning{
    color: #E6A23C;
  }
</style>

 
 