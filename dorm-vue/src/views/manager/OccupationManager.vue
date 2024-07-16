<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item>入住管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="楼宇" prop="buildingId">
        <el-select v-model="formInline.buildingId" placeholder="请选择" clearable size="small" @change="onChangeBuilding">
          <el-option
            v-for="item in allBuildingList"
            :key="item.buildingId"
            :label="item.buildingName"
            :value="item.buildingId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="宿舍" prop="dormId">
        <el-select v-model="formInline.dormId" placeholder="请选择" clearable size="small">
          <el-option
            v-for="item in dormList"
            :key="item.dormitoryId"
            :label="item.roomNo"
            :value="item.dormitoryId">
          </el-option>
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
          >新增入住</el-button
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
          <span class="blue">{{ scope.row.building.buildingName}}</span>
          <span>{{ scope.row.dorm.roomNo}}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="student.college" label="院系">
      </el-table-column> -->
      <el-table-column align="center" prop="student.majorClass" label="院系/专业班级" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.student.college}}</span>
          <span>{{ scope.row.student.majorClass}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="student.realName" label="姓名">
      </el-table-column>
      <el-table-column align="center" prop="student.gender" label="性别" width="80">
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="入住时间">
        <template slot-scope="scope">
          <span class="success">{{ scope.row.checkInTime|timestampToDate }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="搬离时间">
        <template slot-scope="scope">
          <span class="warning" v-if="scope.row.checkOutTime!=null">{{ scope.row.checkOutTime|timestampToDate }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="remark" label="备注" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.currStatus == 1" type="success" size="small">住宿中</el-tag>
          <el-tag v-else-if="scope.row.currStatus == -1" type="danger" size="small">已搬离</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            v-if="scope.row.currStatus == 1"
            icon="el-icon-upload2"
            @click="showLeaveForm(scope.$index, scope.row)"
            >搬离</el-button
          >
          <!-- <el-button size="mini" type="danger" @click="offShelf(scope.$index, scope.row, -1)" icon="el-icon-lock" v-if="scope.row.currStatus > -1">注销</el-button> -->
          <!-- <el-button size="mini" type="success" @click="offShelf(scope.$index, scope.row, 1)" icon="el-icon-check" v-else>解冻</el-button> -->
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
        <el-form-item label="操作提示：" prop="remark">
          <span class="warning">若选择的学生当前已经在其它宿舍入住中，提前本次表单后，将会自动从原入住宿舍搬离，然后入住到新宿舍！</span>
        </el-form-item>
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="editForm.studentId" placeholder="请选择" size="small" filterable clearable>
            <el-option
              v-for="item in allStudent"
              :key="item.studentId"
              :label="item.majorClass+'：'+item.realName+'('+item.gender+')'"
              :value="item.studentId">
            </el-option>
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
        <el-form-item label="备注" prop="remark">
          <el-input
            size="small"
            v-model="editForm.remark"
            auto-complete="off"
            placeholder=""
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

    <!--搬离表单-->
    <el-dialog
      title="学生搬离"
      :visible.sync="leaveFormVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
      <el-form
        label-width="120px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="学生信息" prop="remark" v-if="leaveData!=null">
          <span>{{ leaveData.student.college}}</span>
          <span>{{ leaveData.student.majorClass}}</span>
        </el-form-item>
        <el-form-item label="当前入住宿舍" prop="remark" v-if="leaveData!=null">
          {{ leaveData.building.buildingName }} {{ leaveData.dorm.roomNo }}
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            size="small"
            v-model="editForm.remark"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog2">取消</el-button>
        <el-button
          size="small"
          type="danger"
          :loading="loading"
          class="title"
          @click="submitForm('editForm')"
          >确认搬离</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from "../../components/Pagination.vue";
import { OccupationManager, UpdateApartmentMgrStatus, SaveOccupation, GetAllBuildingList, AdminGetDormByBuilding,AdminGetAllStudentList } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      leaveFormVisible: false, //详情对话框显示与隐藏
      dialogTitle: "添加用户",
      editForm: {
        occupationId: '',
        studentId: '',
        dormId: '',
        remark: '',
        buildingId: '',
        currStatus: 1
      },
      // rules表单验证 
      rules: {
        dormId: [{ required: true, message: '请选择', trigger: 'blur' }],
        studentId: [{ required: true, message: '请选择', trigger: 'blur' }],
        buildingId: [{ required: true, message: '请选择', trigger: 'blur' }],
      },
      formInline: {
        currentpage: 1,
        pageSize: 10,
        buildingId: "",
        dormId: ""
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
    // 获取用户列表数据
    getdata(parameter) {
      this.loading = true;
      OccupationManager(parameter)
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
      this.getdata(this.formInline);
    },

    onChangeBuilding(val){
      AdminGetDormByBuilding({buildingId: val}).then((res) => {
        if (res.success) {
          this.dormList = res.data
        }
      })
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
        this.dialogTitle = '修改入住信息'
        this.editForm.occupationId = row.occupationId;
        this.editForm.studentId = row.studentId;
        this.editForm.dormId = row.dormId;
        this.editForm.remark = row.remark;
        this.editForm.currStatus = row.currStatus;
        this.editForm.buildingId = row.buildingId;
      }else{
        this.dialogTitle = '添加入住信息'
        this.editForm.occupationId=''
        this.editForm.studentId = ''
        this.editForm.dormId = ''
        this.editForm.remark = ''
        this.editForm.buildingId = ''
        this.editForm.currStatus = 1
      }
    },

    //搬离表单
    showLeaveForm: function (index, row) { 
      this.leaveFormVisible = true;
      console.log('搬离：', row)
      this.leaveData = row
      // 默认新增
      if (row != undefined && row != "undefined") {
        // 更新
        this.editForm.occupationId = row.occupationId;
        this.editForm.studentId = row.studentId;
        this.editForm.dormId = row.dormId;
        this.editForm.remark = row.remark;
        this.editForm.currStatus = -1 //搬离状态
        this.editForm.buildingId = row.buildingId;
      }
    },

    // 提交表单
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          SaveOccupation(this.editForm)
            .then((res) => {
              this.editFormVisible = false;
              this.leaveFormVisible = false;
              this.loading = false;
              if (res.success && res.code==0) {
                this.getdata(this.formInline);
                this.$message({
                  type: "success",
                  message: "更新成功！",
                });
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
    // 用户冻结与解冻
    offShelf(index, row, status) {
      let msg = "确定要注销该宿舍管理员吗？"
      if(status == 1){
        msg = "确定要解冻该用户吗？"
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          UpdateApartmentMgrStatus({occupationId: row.occupationId, status: status})
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

 
 