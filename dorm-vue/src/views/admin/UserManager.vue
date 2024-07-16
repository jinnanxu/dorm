<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item >首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.mobile"
          placeholder="输入手机号"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="search"
          >搜索</el-button>
        <el-button
          size="small"
          type="success"
          icon="el-icon-plus"
          @click="handleEdit()"
          >添加</el-button>
      </el-form-item>
      <el-form-item>
        <el-upload ref="upload" class="upload-demo" action="http://localhost:9081/dorm-server/admin/user/uploadExcel" :limit="1"
          :before-upload="beforeUpload" :on-success="handleSuccess" :on-error="uploadError" :file-list="fileList">
          <el-button type="success" size="small" icon="el-icon-document" v-loading.fullscreen.lock="fullscreenLoading">批量导入</el-button>
        </el-upload>
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
      <el-table-column align="center" prop="college" label="学院">
      </el-table-column>
      <el-table-column align="grade" prop="majorClass" label="年级">
      </el-table-column>
      <el-table-column align="center" prop="majorClass" label="专业班级">
      </el-table-column>
      <el-table-column align="center" prop="realName" label="姓名">
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="手机号">
      </el-table-column>
      <el-table-column align="center" prop="studentNo" label="学号">
      </el-table-column>
      <el-table-column align="center" prop="gender" label="性别">
      </el-table-column>
      <el-table-column align="center" prop="role" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">正常</el-tag>
          <el-tag v-else-if="scope.row.status == 0" type="warning" size="small">待激活</el-tag>
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
        <el-form-item label="院系" prop="college">
          <el-input
            size="small"
            v-model="editForm.college"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input
            size="small"
            type="number"
            v-model="editForm.grade"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="专业班级" prop="majorClass">
          <el-input
            size="small"
            v-model="editForm.majorClass"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="学号" prop="studentNo">
          <el-input
            size="small"
            v-model="editForm.studentNo"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input
            size="small"
            v-model="editForm.realName"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio v-model="editForm.gender" label="男">男</el-radio>
          <el-radio v-model="editForm.gender" label="女">女</el-radio>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input
            size="small"
            v-model="editForm.mobile"
            auto-complete="off"
          ></el-input>
        </el-form-item>
        <!-- <el-form-item label="密码" prop="pwd">
          <el-input
            size="small"
            v-model="editForm.pwd"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item> -->
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
import { AdminUserList, AdminUpdateUser, AdminUpdateUser2 } from '@/api/api.js'
import { ExcelUploader } from '@/api/upload.js'
export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      dialogTitle: "添加用户",
      editForm: {
        studentId: '',
        pwd: '',
        realName: '',
        gender: '',
        mobile: '',
        studentNo: '',
        grade: '',
        college: '',
        majorClass: '',
        status: ''
      },
      fullscreenLoading: false,
      // rules表单验证 
      rules: {
        realName: [{ required: true, message: '请输入', trigger: 'blur' }],
        studentNo: [{ required: true, message: '请输入', trigger: 'blur' }],
        college: [{ required: true, message: '请输入', trigger: 'blur' }],
        gender: [{ required: true, message: '请选择', trigger: 'blur' }],
        majorClass: [{ required: true, message: '请输入', trigger: 'blur' }],
        mobile: [
          { required: true, message: '请输入', trigger: 'blur' },
          {
            pattern: /^1(3\d|47|5((?!4)\d)|7(0|1|[6-8])|8\d)\d{8,8}$/,
            required: true,
            message: '请输入正确的手机号',
            trigger: 'blur'
          }
        ],
      },
      formInline: {
        currentpage: 1,
        pageSize: 10,
        mobile: "",
      },
      listData: [], //用户数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10,
      },
      fileList: []
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
      AdminUserList(parameter)
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
        this.dialogTitle = '修改学生信息'
        this.editForm.studentId = row.studentId;
        this.editForm.pwd = row.pwd;
        this.editForm.realName = row.realName;
        this.editForm.gender = row.gender;
        this.editForm.mobile = row.mobile;
        this.editForm.status = row.status;
        this.editForm.studentNo = row.studentNo;
        this.editForm.college = row.college;
        this.editForm.majorClass = row.majorClass;
        this.editForm.grade = row.grade;
      }else{
        this.dialogTitle = '添加学生信息'
        this.editForm.studentId = ''
        this.editForm.pwd = ''
        this.editForm.realName = ''
        this.editForm.gender = ''
        this.editForm.mobile = ''
        this.editForm.status = 0
        this.editForm.studentNo = ''
        this.editForm.college = ''
        this.editForm.majorClass = ''
        this.editForm.grade = ''
      }
    },
    // 提交修改学生表单
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (valid) {
          AdminUpdateUser(this.editForm)
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
      let msg = "确定要注销该学生吗？"
      if(status == 1){
        msg = "确定要解冻该用户吗？"
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          AdminUpdateUser2({studentId: row.studentId, status: status})
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

    beforeUpload(file) {
      console.log("file.type:" + file.type);
      this.fullscreenLoading = true
      const fileSize = file.size / Math.pow(2, 20);
      if (file.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
          this.$message.error('只能上传excel文件')
          return false;
      }

      if (fileSize > 10) {
          this.$message.error("图片不能超过10MB")
          return false;
      }

      return true;
    },
    //图片上传成功之后：将上传图片的数据添加到fileList
    handleSuccess(response, file, fileList) {
      // 根据后端返回的数据修改fileList集合
      console.log("图片上传成功之后response:" + JSON.stringify(response));
      this.files = file.raw;
      // 文件对象
      var formData = new FormData();
      formData.append("myFile", file.raw)
      console.log('上传文件：', formData)
      this.fullscreenLoading = false
      if(response.success){
        this.$message({type: "success", message: "批量导入成功!"});
      }else{
        this.$message.error("批量导入失败")
      }
      // ExcelUploader(formData).then((res) => {
      //   if (res.data.errno==0) {
      //     console.log('批量导入成功！', res.data)
      //     this.editForm.pics = res.data.data[0]
      //   } else {
      //     this.$message.error(res.data.message);
      //   }
      //   this.uploadPicLoading=false
      // })
    },

    //上传失败的逻辑
    uploadError(err, file, fileList) {
        this.$message({
            message: err.message,
            type: "error",
        });
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

 
 