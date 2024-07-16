<template>
  <div class="main-contain">
    <div id="admin">
      <div class="pos" v-loading="loading">
        <p class="adminh1">宿舍管理系统</p>
        <p class="adminh3">后台登录</p>
        <el-form
          :model="ruleForm"
          status-icon
          :rules="rules"
          ref="ruleForm"
          label-width="150px"
          label-position="right"
          class="login-form"
        >
          <el-form-item prop="mobile">
            <template slot="label">
              <span class="label">手机号：</span>
            </template>
            <el-input
              prefix-icon="el-icon-mobile"
              style="width: 250px"
              type="text"
              v-model="ruleForm.mobile"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item prop="role">
            <template slot="label">
              <span class="label">角 色：</span>
            </template>
            <el-select v-model="ruleForm.role" placeholder="请选择" style="width: 250px" prefix-icon="el-icon-menu">
              <el-option label="宿舍管理员" :value="2"></el-option>
              <el-option label="维修员" :value="3"></el-option>
              <el-option label="系统管理员" :value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="pwd" class="label">
            <template slot="label">
              <span class="label">"密 码：</span>
            </template>
            <el-input
              prefix-icon="el-icon-lock"
              style="width: 250px"
              show-password
              type="password"
              v-model="ruleForm.pwd"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')" icon="el-icon-s-promotion">登录</el-button>
            <el-button type="success" @click="toReg()" icon="el-icon-s-custom">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import {AdminLogin} from '@/api/api.js'
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入用户手机号"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    return {
      loading: false,
      ruleForm: {
        mobile: "",
        pwd: "",
        role: 2
      },
      rules: {
        mobile: [{ required: true, validator: validatePass, trigger: "blur" }],
        pwd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择', trigger: 'blur' }]
      },
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
    //this.getdata(this.formInline)
    this.login()
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    login(parameter) {
      // this.$store.commit('login', {userId: 1, userName: 'admin'})
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        console.log('用户信息：',this.ruleForm)
        if (valid) {
          this.loading = true;
          let loginForm = {mobile: this.ruleForm.mobile, pwd: this.ruleForm.pwd, role: this.ruleForm.role}
          AdminLogin(loginForm)
            .then(res => {
              this.loading = false
              if (res.code==0&&res.data.status=="OK") {
                this.$message({
                  type: 'success',
                  message: '登录成功！',
                  showClose: true,
                  center: true
                })
                //设置用户信息
                console.log('登录成功！',res.data)
                // 保存Token
                localStorage.setItem('AuthToken', res.data.token)
                let nextPath = '/admin/usermgr' //登录后跳转路径
                if(this.ruleForm.role==1){
                  this.$store.commit('login', res.data) // 通过Vuex保存登录管理员信息
                }else{
                  this.$store.commit('login', res.data)
                  nextPath = '/manager/index'
                }
                setTimeout(() => {
                  this.$router.push({ path: nextPath })
                }, 800)
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
              this.$message.error('登录失败，请稍后再试！')
            })
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    toReg(){
      this.$router.push({path:'/reg'})
    }
  }
}
</script>

<style scoped>
.user-search {
  margin-top: 20px;
}
.userRole {
  width: 100%;
}
body {
  background: rgb(135, 206, 235);
}
.main-contain{
  width: 100%;
  height: 100vh;
  background-image: url('../../assets/img/login-bg.gif');
  background-repeat: no-repeat;
  background-size: cover;
}
#admin {
  color: #333;
  position: absolute;
  top: 15%;
  left: 30%;
  width: 40%;
  height: auto;
  background: rgba(255,255,255,.5);
  border-radius: 10px;
  box-shadow: 8px 10px 10px rgba(0,0,0,.6);
  border: 1px solid #fff;
}
.adminh1 {
  font-size: 22px;
  margin: 20px 0;
  text-align: center;
  color: #333;
}
.login-form {
  margin: 0 auto;
  display: block;
  width: 420px;
}
#admin .el-form-item__content{
  margin: 0;
  text-align: center;
}
.adminh3{
  font-size: 16px;
  margin: 10px 0 50px;
  text-align: center;
  color: #333;
}
.label{
  color:#000 !important;
}
</style>

 
 