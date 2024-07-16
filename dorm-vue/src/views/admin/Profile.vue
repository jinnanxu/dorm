<template>
  <div class="main-contain">
    <div class="profile-div">
      <el-row :gutter="20">
        <el-col :span="6" class="profile-item meta"><div class="grid-content bg-purple">角色：</div></el-col>
        <el-col :span="6" class="profile-item">
          <div class="grid-content bg-purple"><span class="role-div">教师</span></div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div class="grid-content bg-purple">院系：</div></el-col>
        <el-col :span="6" class="profile-item"><div class="grid-content bg-purple">{{userData.college.collegeName}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div class="grid-content bg-purple">姓名：</div></el-col>
        <el-col :span="6" class="profile-item"><div class="grid-content bg-purple">{{userData.realName}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div class="grid-content bg-purple">手机号：</div></el-col>
        <el-col :span="6" class="profile-item"><div class="grid-content bg-purple">{{userData.mobile}}</div></el-col>
      </el-row>
      <el-row :gutter="20" class="profile">
        <el-col :span="6" class="profile-item meta"><div class="grid-content bg-purple">评分：</div></el-col>
        <el-col :span="6" class="profile-item">
          <div class="grid-content bg-purple">
            <el-rate
              v-model="userData.rate"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}">
            </el-rate>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="profile" style="padding-top: 20px;">
        <el-col :span="6">&nbsp;</el-col>
        <el-col :span="6">
          <el-button size="small" type="info" icon="el-icon-back" @click="goBack()">返回</el-button>
          <el-button type="primary" @click="handleEdit()" size="small" icon="el-icon-edit">修改信息</el-button>
        </el-col>
      </el-row>
    </div>
    <!-- 编辑界面 -->
    <el-dialog :title="title" :visible.sync="editFormVisible" width="30%" @click='closeDialog("edit")'>
      <el-form label-width="80px" ref="editForm" :model="editForm" :rules="rules">
        <el-form-item label="姓名" prop="realName">
          <el-input size="small" v-model="editForm.realName" auto-complete="off" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input size="small" v-model="editForm.mobile" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input size="small" type="password" v-model="editForm.pwd" placeholder="请输入密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click='closeDialog("edit")'>取消</el-button>
        <el-button size="small" type="primary" :loading="loading" class="title" @click="submitForm('editForm')">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { updateProfile } from '@/api/api.js'
export default {
    data() {
    return {
      loading: false, //是显示加载
      title: '修改用户',
      editFormVisible: false, //控制编辑页面显示与隐藏
      // 编辑与添加
      editForm: {
        adminId: '',
        realName: '',
        mobile: '',
        pwd: '',
        gender: '',
      },     
      //用户数据
      userData: [],
      rules: {
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'blur' }],
        mobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          {
            pattern: /^1[3456789]\d{9}$/,
            required: true,
            message: '请输入正确的手机号',
            trigger: 'blur'
          }
        ],
        gender: [{ required: true, message: '请选择性别', trigger: 'blur' }]
      },
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
    console.log(localStorage.getItem('TeacherInfo'))
    this.userData = JSON.parse(localStorage.getItem('TeacherInfo'))
    
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 返回
    goBack() {
      this.$router.back()
    },

    //显示编辑界面
    handleEdit() {
      this.editFormVisible = true
      this.title = '修改信息'
      this.editForm.adminId = this.userData.adminId
      this.editForm.mobile = this.userData.mobile
      this.editForm.position = this.userData.position
      this.editForm.pwd = this.userData.pwd
      this.editForm.realName = this.userData.realName
      this.editForm.role = this.userData.role
      this.editForm.userNo = this.userData.userNo
      this.editForm.gender = this.userData.gender
    },
    // 编辑、添加提交方法
    submitForm(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          // 请求方法
          updateProfile(this.editForm)
            .then(res => {
              this.editFormVisible = false
              this.loading = false
              if (res.success) {
                console.log('新修改的用户：', res.data)
                this.userData=res.data
                localStorage.removeItem('AdminInfo')
                localStorage.setItem('AdminInfo', JSON.stringify(res.data))
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
    
    // 关闭编辑、增加弹出框
    closeDialog(dialog) {
      if (dialog == 'edit') {
        this.editFormVisible = false
      } else if (dialog == 'perm') {
        this.dataAccessshow = false
      } else if (dialog == 'unit') {
        this.unitAccessshow = false
      }
    },
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
</style>