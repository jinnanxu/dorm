<template>
  <div class="main-contain">
    <div id="admin">
      <div class="pos" v-loading="loading" style="margin: 0 10%;">
        <p class="adminh1">用户绑定</p>
        <p class="bindtip">请输入个人信息激活账号</p>
        <el-form label-width="100px" ref="editForm" :model="editForm" :rules="rules">
          <el-form-item label="姓名" prop="realName">
            <el-input size="small" v-model="editForm.realName" auto-complete="off" placeholder="请输入姓名"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="mobile">
            <el-input size="small" v-model="editForm.mobile" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="学号" prop="studentNo">
            <el-input size="small" v-model="editForm.studentNo" auto-complete="off" placeholder="请输入学号"></el-input>
          </el-form-item>
          <!-- <el-form-item label="个人简介" prop="shortDesc">
            <el-input size="small" v-model="editForm.shortDesc" auto-complete="off" placeholder="请输入个人简介"></el-input>
          </el-form-item> -->
          <el-form-item label="登录密码" prop="pwd">
            <el-input size="small" type="password" v-model="editForm.pwd" placeholder="请设置登录密码"></el-input>
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
                <li v-if="photo==null" class="el-icon-plus avatar-uploader-icon"></li>
                <li class="img-li" v-else>
                  <img :src="photo" class="avatar" />
                </li>
              </ul>
            </el-upload>
          </el-form-item> -->
          <el-form-item>
            <el-button class="green-button" @click="submitForm('editForm')" icon="el-icon-s-promotion">激活</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import {FrontUserRegist,FindAvCollege,FindClassByCollege} from '@/api/api.js'
import { fileUploader } from '@/api/upload.js'
export default {
  data() {
    return {
      loading: false,
      editForm: {
        pwd: '',
        realName: '',
        mobile: '',
        studentNo:''
      },
      uploadPicLoading: false,
      // rules表单验证 
      rules: {
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        studentNo: [{ required: true, message: '请输入', trigger: 'blur' }],
        mobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          {
            pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
            required: true,
            message: '请输入正确的手机号',
            trigger: 'blur'
          }
        ],
      },
      photo: null,
    };
  },
  // 注册组件
  components: {
    
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    this.getdata()
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    getdata(){

    },

    submitForm(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          FrontUserRegist(this.editForm)
            .then(res => {
              this.editFormVisible = false
              this.loading = false
              console.log('注册成功回调', res.data)
              if (res.success) {
                this.$message({
                  type: 'success',
                  message: '账号绑定成功！'
                })
                setTimeout(() => {
                  this.$router.push({ path: '/login' })
                }, 2000)
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
              this.$message.error('注册失败，请稍后再试！')
            })
        } else {
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
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
          this.photo = res.data.data[0]
          this.editForm.pic = res.data.data[0]
        } else {
          this.$message.error(res.data.message);
        }
        this.uploadPicLoading=false
      })
    },
  }
}
</script>

<style scoped>
body {
  background: rgb(135, 206, 235);
}
.main-contain{
  width: 100%;
  height: 100vh;
  background-repeat: no-repeat;
  background-size: cover;
}
#admin {
  color: #333;
  margin: 30px auto;
  width: 40%;
  height: auto;
  background: rgba(255,255,255,.5);
  border-radius: 10px;
  box-shadow: 8px 10px 10px rgba(0,0,0,.2);
  border: 1px solid #b8cad8;
}
.adminh1 {
  font-size: 22px;
  margin: 20px 0 0 0;
  text-align: center;
  color: #333;
}
.bindtip{
  padding: 8px 0;
  margin: 0 0 10px 0;
  color: #888;
  font-size: 14px;
  text-align: center;
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

 
 