import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    toastText: '',
    loadingVisible: false,
    currUser: {}
  },
  mutations: {
    increment (state) {
      state.count++
    },
    toast (state, text) {
      state.toastText = text
      setTimeout(() => {
        state.toastText = ''
      }, 2000)
    },
    loading (state, loadingVisible) {
      state.loadingVisible = loadingVisible
    },
    requireLogin (state) {
      location.assign('http://localhost/#/me/login?url=' + encodeURIComponent(location.href))
    },
    login(state, data) {
      console.log('Vuex被触发，管理员登录：', data)
      state.currUser = data.user;
      let userString = JSON.stringify(data.admin)
      localStorage.setItem("AdminInfo", userString);
      if(data.admin.role==1){
        localStorage.setItem("AdminRole", 1);
      }else{
        localStorage.setItem("AdminRole", 2);
      }
      let loginTime = new Date()
      loginTime = loginTime.getTime()
      localStorage.setItem('loginTime', loginTime)
    },
    //教师登录
    loginTeacher(state, data) {
      console.log('Vuex被触发，宿舍管理员登录：', data)
      state.currUser = data.user;
      let userString = JSON.stringify(data.admin)
      localStorage.setItem("TeacherInfo", userString);
      localStorage.setItem("AdminRole", 2);
      let loginTime = new Date()
      loginTime = loginTime.getTime()
      localStorage.setItem('loginTime', loginTime)
    },
    //前台用户登录
    loginUser(state, data) {
      console.log('Vuex被触发，学生登录：', data)
      state.currUser = data.user;
      let userString = JSON.stringify(data.user)
      localStorage.setItem('LoginUser', userString)
      let loginTime = new Date()
      loginTime = loginTime.getTime()
      localStorage.setItem('loginTime', loginTime)
    },
    logout(state, p2) {
      console.log('退出登录Vuex被触发：', p2)
      state.currUser = null
      localStorage.removeItem("AdminInfo")
      localStorage.removeItem("LoginUser")
      localStorage.removeItem("AuthToken")
    }
  },
  getters
})
