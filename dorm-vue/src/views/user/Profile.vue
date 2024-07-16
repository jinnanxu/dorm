<template>
  <div class="main-contain">
    <p class="page-title">个人信息</p>
    <div class="profile-div">
      <!-- <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div></div></el-col>
        <el-col :span="6" class="profile-item">
          <div class="profile-img">
            <img :src="userData.pic" alt="">
          </div>
        </el-col>
      </el-row> -->
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div>姓名：</div></el-col>
        <el-col :span="6" class="profile-item"><div>{{userData.realName}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div>院系/班级：</div></el-col>
        <el-col :span="6" class="profile-item"><div>{{userData.college}} {{userData.majorClass}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div>学号：</div></el-col>
        <el-col :span="6" class="profile-item"><div>{{userData.studentNo}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div>手机号：</div></el-col>
        <el-col :span="6" class="profile-item"><div>{{userData.mobile}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div>当前宿舍：</div></el-col>
        <el-col :span="6" class="profile-item"><div>{{userData.occ.building.buildingName}} {{userData.occ.dorm.roomNo}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile" style="margin-top: 15px;">
        <el-col :span="6">&nbsp;</el-col>
        <el-col :span="6">
          <el-button size="small" type="info" icon="el-icon-back" @click="goBack()">返回</el-button>
          <el-button class="green-button" @click="handleEdit()" size="small" icon="el-icon-edit">修改信息</el-button>
        </el-col>
      </el-row>
    </div>
    <!-- 编辑界面 -->
    <el-dialog :title="title" :visible.sync="editFormVisible" width="30%" @click='closeDialog("edit")'>
      <el-form label-width="80px" ref="editForm" :model="editForm" :rules="rules">
        <el-form-item label="昵称" prop="nickName">
          <el-input size="small" v-model="editForm.nickName"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input size="small" v-model="editForm.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input size="small" type="password" v-model="editForm.pwd" placeholder="请输入登录密码"></el-input>
        </el-form-item>
        <!-- <el-form-item label="头像" prop="pic" v-loading="uploadPicLoading">
          <el-upload 
            class="avatar-uploader showUploader"
            action="#"
            :auto-upload="false"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :on-change="uploadSectionFile"
            :limit="1"
          >
            <ul class="img-list">
              <li v-if="editForm.pic==null||editForm.pic==''" class="el-icon-plus avatar-uploader-icon"></li>
              <li class="img-li" v-else>
                <img :src="editForm.pic" class="avatar" />
                <i class="del-btn el-icon-delete-solid" @click="delPic()"></i>
              </li>
            </ul>
          </el-upload>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" class="orange-button" @click='closeDialog("edit")'>取消</el-button>
        <el-button size="small" class="green-button title" :loading="loading" @click="submitForm('editForm')">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { FrontUpdateProfile } from '@/api/api.js'
import { fileUploader } from '@/api/upload.js'
export default {
    data() {
    return {
      loading: false, //是显示加载
      title: '修改用户',
      editFormVisible: false, //控制编辑页面显示与隐藏
      // 编辑与添加
      editForm: {
        userId: '',
        nickName: '',
        mobile: '',
        pwd: '',
        farmName: '',
        pic: ''
      },     
      //用户数据
      userData: [],
      files: null,
      rules: {
        nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
        pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        mobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          {
            pattern: /^1[3456789]\d{9}$/,
            required: true,
            message: '请输入正确的手机号',
            trigger: 'blur'
          }
        ],
      },
      uploadPicLoading: false
    }
  },
  // 注册组件
  components: {
    
  },

  /**
   * 数据发生改变
   */
  watch: {},

  /**
   * 创建完毕
   */
  created() {
    console.log(localStorage.getItem('LoginUser'))
    this.userData = JSON.parse(localStorage.getItem('LoginUser'))
    //this.getdata()
    
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取数据方法
    getdata() {
      //获取用户信息
      let parameter = {userId: this.userData.userId}
      profile(parameter).then(res => {
        this.loading = false
        if (res.success == false) {
          this.$message({
            type: 'info',
            message: res.msg
          })
        } else {
          this.userData = res.data
        }
      })
    },
    
    // 返回
    goBack() {
      this.$router.back()
    },

    //显示编辑界面
    handleEdit() {
      this.editFormVisible = true
      this.title = '修改信息'
      this.editForm.userId = this.userData.userId
      this.editForm.mobile = this.userData.mobile
      this.editForm.pwd = this.userData.pwd
      this.editForm.farmName = this.userData.farmName
      this.editForm.nickName = this.userData.nickName
      this.editForm.pic = this.userData.pic      
    },
    // 编辑、添加提交方法
    submitForm(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          // 请求方法
          FrontUpdateProfile(this.editForm)
            .then(res => {
              this.editFormVisible = false
              this.loading = false
              if (res.success) {
                console.log('新修改的用户：', res.data)
                this.userData=res.data
                localStorage.removeItem('LoginUser')
                localStorage.setItem('LoginUser', JSON.stringify(res.data))
                this.$message({
                  type: 'success',
                  message: '数据更新成功！'
                })
                window.location.reload()
              } else {
                this.$message({
                  type: 'info',
                  message: res.msg
                })
              }
            })
            .catch(err => {
              this.editFormVisible = false
              this.loading = false
              console.log(err)
              this.$message.error('保存失败，请稍后再试！')
            })
        } else {
          return false
        }
      })
    },

    handlePreview(file) {
      console.log(file);
    },
    handleRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    //上传图片
    uploadSectionFile(file) {
      console.log('选定图片！')
      this.uploadPicLoading=true
      const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
      const isJPG = typeArr.indexOf(file.raw.type) !== -1;
      const isLt3M = file.size / 1024 / 1024 < 3;
      if (!isJPG) {
        this.$message.error("只能是图片!");
        this.files = null;
        this.uploadPicLoading=false
        return;
      }
      if (!isLt3M) {
        this.$message.error("上传图片大小不能超过 3MB!");
        this.files = null;
        this.uploadPicLoading=false
        return;
      }
      this.files = file.raw;
      // 文件对象
      var formData = new FormData();
      formData.append("myFile", file.raw)
      console.log('上传图片表单：', formData)
      fileUploader(formData).then((res) => {
        if (res.data.errno==0) {
          console.log('图片上传成功！', res.data)
          this.editForm.pic = res.data.data[0]
        } else {
          this.$message.error(res.data.message);
        }
        this.uploadPicLoading=false
      })
    },
    
    // 关闭编辑、增加弹出框
    closeDialog(dialog) {
      if (dialog == 'edit') {
        this.editFormVisible = false
      }
    },

    delPic(){
      this.editForm.pic=''
      this.files=null
    }
  }
}
</script>

<style>
.user-search {
  margin-top: 20px;
}
.userRole {
  width: 100%;
}
.profile-div{
  padding: 10px 0;
  height: 500px;
}
.profile-item{
  padding: 10px 0;
  font-size: 14px;
}
.profile-div .meta{
  text-align: right;
}
.role-div{
  background-color: #007acc;
  color: #fff;
  display: inline-block;
  padding: 1px 10px;
  border-radius: 15px;
}
.img-list .img-li{
  position: relative;
}
.img-li .del-btn{
  position: absolute;
  right: 10px;
  top: 10px;
  color: #d87f57;
  text-shadow: #fff 2px 2px;
  font-size: 22px;
  z-index: 999;
}
.img-li .del-btn:HOVER{
  cursor: pointer;
  color: #dd571a;
}
.profile-img img{
  width: 120px;
  height: 120px;
}
.avatar-uploader .avatar{
  width: 160px;
  height: 160px;
}
.img-list .el-icon-plus{
  font-size: 22px;
  padding: 25px;
  border: 1px dashed #ccc;
}
.avatar-uploader .avatar{
  width: 160px;
  height: 160px;
}
</style>