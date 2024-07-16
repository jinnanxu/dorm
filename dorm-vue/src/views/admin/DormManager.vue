<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item>宿舍管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="楼宇：" prop="buildingId">
          <el-select v-model="formInline.buildingId" clearable size="small">
            <el-option
              v-for="item in allBuildingList"
              :key="item.buildingId"
              :label="item.buildingName"
              :value="item.buildingId">
            </el-option>
          </el-select>
        </el-form-item>
      <el-form-item label="房间号：">
        <el-input
          size="small"
          v-model="formInline.roomNo"
          placeholder="输入房间号"
        ></el-input>
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
          >添加</el-button
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
      <!-- <el-table-column align="center" prop="pic" label="头像" width="80">
        <template slot-scope="scope">
          <el-avatar shape="square" size="medium" :src="scope.row.pic"></el-avatar>
        </template>
      </el-table-column> -->
      <el-table-column align="center" prop="building.buildingName" label="所在楼宇">
      </el-table-column>
      <el-table-column align="center" prop="floor" label="所在楼层" width="100">
      </el-table-column>
      <el-table-column align="center" prop="roomNo" label="宿舍号">
      </el-table-column>
      <el-table-column align="center" prop="maxCapacity" label="最大可住人数" width="110">
      </el-table-column>
      <el-table-column align="center" prop="occupied" label="已入住人数" width="110">
      </el-table-column>
      <el-table-column align="center" prop="remark" label="备注" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">正常</el-tag>
          <el-tag v-else-if="scope.row.status == -1" type="danger" size="small">已关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            v-if="scope.row.status > -1"
            icon="el-icon-edit"
            @click="handleEdit(scope.$index, scope.row)"
            >修改</el-button
          >
          <el-button size="mini" type="danger" @click="offShelf(scope.$index, scope.row, -1)" icon="el-icon-lock" v-if="scope.row.status > -1">关闭</el-button>
          <el-button size="mini" type="success" @click="offShelf(scope.$index, scope.row, 1)" icon="el-icon-check" v-else>恢复</el-button>
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
        <el-form-item label="所在楼宇" prop="buildingId">
          <el-select v-model="editForm.buildingId" placeholder="请选择" size="small">
            <el-option
              v-for="item in allBuildingList"
              :key="item.buildingId"
              :label="item.buildingName"
              :value="item.buildingId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="宿舍号" prop="roomNo">
          <el-input
            size="small"
            v-model="editForm.roomNo"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="楼层" prop="floor">
          <el-input
            size="small"
            v-model="editForm.floor"
            auto-complete="off"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="最大入住人数" prop="maxCapacity">
          <el-input
            size="small"
            v-model="editForm.maxCapacity"
            auto-complete="off"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="已入住人数" prop="occupied">
          <el-input
            size="small"
            v-model="editForm.occupied"
            auto-complete="off"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            size="small"
            v-model="editForm.remark"
            auto-complete="off"
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
import { AdminGetDormList, AdminUpdateDormStatus, AdminUpdateDorm, GetAllBuildingList } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      dialogTitle: "添加用户",
      editForm: {
        dormitoryId: '',
        floor: '',
        roomNo: '',
        maxCapacity: '',
        buildingId: '',
        occupied: 0,
        remark: '',
        status: 1
      },
      // rules表单验证 
      rules: {
        floor: [{ required: true, message: '请输入', trigger: 'blur' }],
        maxCapacity: [{ required: true, message: '请输入', trigger: 'blur' }],
        buildingId: [{ required: true, message: '请输入', trigger: 'blur' }],
        roomNo: [{ required: true, message: '请输入', trigger: 'blur' }],
      },
      formInline: {
        currentpage: 1,
        pageSize: 10,
        roomNo: "",
        buildingId: ''
      },
      listData: [], //用户数据
      allBuildingList: [],
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10,
      },
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
      AdminGetDormList(parameter)
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

    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
      // 默认新增
      if (row != undefined && row != "undefined") {
        // 更新
        this.dialogTitle = '修改宿舍信息'
        this.editForm.dormitoryId = row.dormitoryId;
        this.editForm.floor = row.floor;
        this.editForm.roomNo = row.roomNo;
        this.editForm.maxCapacity = row.maxCapacity;
        this.editForm.occupied = row.occupied;
        this.editForm.status = row.status;
        this.editForm.remark = row.remark;
        this.editForm.buildingId = row.buildingId;
      }else{
        this.dialogTitle = '添加宿舍信息'
        this.dormitoryId=''
        this.editForm.floor = ''
        this.editForm.remark = ''
        this.editForm.roomNo = ''
        this.editForm.maxCapacity = ''
        this.editForm.buildingId = ''
        this.editForm.occupied = 0
        this.editForm.status = 1
      }
    },
    // 提交修改宿舍表单
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          AdminUpdateDorm(this.editForm)
            .then((res) => {
              this.editFormVisible = false;
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
            .catch((err) => {
              this.editFormVisible = false;
              this.loading = false;
              this.$message.error("保存失败，请稍后再试！");
            });
        } else {
          return false;
        }
      });
    },
    // 用户冻结与解冻
    offShelf(index, row, status) {
      let msg = "确定要停用该宿舍吗？"
      if(status == 1){
        msg = "确定要恢复该宿舍吗？"
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          AdminUpdateDormStatus({dormitoryId: row.dormId, status: status})
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
      this.detailVisible = false;
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
</style>

 
 