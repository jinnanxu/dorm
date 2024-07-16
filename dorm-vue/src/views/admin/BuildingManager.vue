<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item>楼宇管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.position"
          placeholder="输入楼宇名称"
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
      <el-table-column align="center" prop="buildingName" label="楼宇名称">
      </el-table-column>
      <el-table-column align="center" prop="position" label="位置">
      </el-table-column>
      <el-table-column align="center" prop="rommsCount" label="总宿舍数">
      </el-table-column>
      <el-table-column align="center" prop="floor" label="总楼层">
      </el-table-column>
      <el-table-column align="center" prop="remark" label="备注">
      </el-table-column>
      <el-table-column align="center" prop="role" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">正常</el-tag>
          <el-tag v-else-if="scope.row.status == -1" type="danger" size="small">已注销</el-tag>
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
          <el-button size="mini" type="danger" @click="offShelf(scope.$index, scope.row, -1)" icon="el-icon-lock" v-if="scope.row.status > -1">注销</el-button>
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
        <el-form-item label="楼宇名称" prop="buildingName">
          <el-input
            size="small"
            v-model="editForm.buildingName"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="位置" prop="position">
          <el-input
            size="small"
            v-model="editForm.position"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="总宿舍数" prop="rommsCount">
          <el-input
            size="small"
            v-model="editForm.rommsCount"
            type="number"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="总楼层" prop="floor">
          <el-input
            size="small"
            v-model="editForm.floor"
            auto-complete="off"
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
import axios from 'axios';
import { AdminGetBuildingList, AdminUpdateBuilding, AdminDeleteBuilding } from '@/api/api.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      dialogTitle: "添加用户",
      editForm: {
        buildingId: '',
        buildingName: '',
        position: '',
        floor: '',
        rommsCount: '',
        remark: '',
        status: 1
      },
      // rules表单验证 
      rules: {
        buildingName: [{ required: true, message: '请输入', trigger: 'blur' }],
        floor: [{ required: true, message: '请输入', trigger: 'blur' }],
        rommsCount: [{ required: true, message: '请输入', trigger: 'blur' }],
        position: [{ required: true, message: '请选择', trigger: 'blur' }],
      },
      formInline: {
        currentpage: 1,
        pageSize: 10,
        position: "",
      },
      listData: [], //用户数据
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
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取用户列表数据
    getdata(parameter) {
      this.loading = true;
      AdminGetBuildingList(parameter)
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
        this.dialogTitle = '修改楼宇信息'
        this.editForm.buildingId = row.buildingId;
        this.editForm.buildingName = row.buildingName;
        this.editForm.position = row.position;
        this.editForm.status = row.status;
        this.editForm.floor = row.floor;
        this.editForm.rommsCount = row.rommsCount;
        this.editForm.remark = row.remark;
      }else{
        this.dialogTitle = '添加楼宇信息'
        this.editForm.buildingId = ''
        this.editForm.pwd = ''
        this.editForm.buildingName = ''
        this.editForm.gender = ''
        this.editForm.position = ''
        this.editForm.status = 1
        this.editForm.floor = ''
        this.editForm.rommsCount = 0
        this.editForm.remark = ''
      }
    },
    // 提交修改楼宇表单
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          AdminUpdateBuilding(this.editForm)
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
      let msg = "确定要注销该楼宇吗？"
      if(status == 1){
        msg = "确定要解冻该用户吗？"
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          AdminDeleteBuilding({buildingId: row.buildingId, status: status})
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

 
 