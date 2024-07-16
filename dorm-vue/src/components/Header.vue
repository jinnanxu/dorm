<template>
  <header class="zui-header">
    <!--顶部LOGO与搜索栏-->
    <div class="top-bar">
      <div class="logo">
      </div>
      <!--导航菜单-->
      <ul class="menu-list">
        <li class="menu-li" v-for="(menu, id) in menuList" :key="id" @click="menu.action">
          <span>{{menu.text}}</span>
          <ul v-if="menu.hasChild" class="sub-menu">
            <li class="sub-item" v-for="(subMenu, id) in menu.children" :key="id" @click="subMenu.action">{{subMenu.text}}</li>
          </ul>
        </li>
      </ul>
      <ul class="menu-list" v-show="loginUser">
        <li class="menu-li" v-for="(menu, id) in userMenu" :key="id" @click="menu.action">
          <span>{{menu.text}}</span>
          <span class="msg" v-if="menu.id==6 && waitPayCount>0">{{ waitPayCount }}</span>
          <ul v-if="menu.hasChild" class="sub-menu">
            <li class="sub-item" v-for="(subMenu, id) in menu.children" :key="id" @click="subMenu.action">{{subMenu.text}}</li>
          </ul>
        </li>
      </ul>
      <!--右侧用户-->
      <div class="user-login">
        <ul v-if="!loginUser">
          <li @click="login()">登录</li>
          <li @click="reg()">激活注册</li>
        </ul>
        <!--登录后-->
        <ul v-else>
          <li @click="profile()">{{ loginUser.realName }}</li>
          <li @click="logout()">退出</li>
        </ul>
      </div>
      <!-- <div class="search-bar">
        <input type="text" class="search-ipt" placeholder="搜索">
        <i class="el-icon-search search-button" @click="onSearch()"></i>
      </div> -->
    </div>
  </header>
</template>
<script>
  import './less/header.less'
  import Logo from './Logo'
  import { MyWaitPayCount } from '@/api/api.js'
  export default {
    props: {
      logoType: {
        default: 'zhichou'
      }
    },
    components: {Logo},
    data () {
      return {
        menuList: [ // 设置下拉列表数据和对应的点击事件
          {id: 1, text: '首 页', hasChild: false, action: this.index},
          // {id: 3, text: '通知公告', hasChild: false, action: this.knowledeg},
        ],
        //登录后出现的菜单
        userMenu: [
          {id: 4, text: '离校留校登记', hasChild: false, action: this.toQuestion},
          {id: 6, text: '水电缴费', hasChild: false, action: this.myFee},
          {id: 7, text: '报修申请', hasChild: false, action: this.fix},
          {id: 9, text: '个人信息', hasChild: true, action: this.profile, children:[]},
        ],
        currUser: {},
        waitPayCount: 0
      }
    },
    created() {
      this.loadLoginUser()
      MyWaitPayCount({}).then(res => {
        if (res.success) {
          console.log('待缴纳账单数量：', res.data)
          this.waitPayCount = res.data
        }else{
          return 0
        }
      })
    },
    methods: {
      onButtonClick: function (e) {},
      void: function () {
        return;
      },
      openMenu: function () {
       this.$refs.droplist.show()
      },
      index: function () {
       this.$router.push({path:'/index'})
      },
      login: function () {
       this.$router.push({path:'/login'})
      },
      reg: function () {
       this.$router.push({path:'/reg'})
      },
      profile: function () {
       this.$router.push({path:'/profile'})
      },
      toQuestion: function () {
       this.$router.push({path:'/leave'})
      },
      fix: function () {
       this.$router.push({path:'/fix'})
      },
      myFee: function () {
       this.$router.push({path:'/fee'})
      },
      logout: function(){
        this.$confirm('退出登录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.commit('logout', {userId: null})
          console.log('登出后：',this.$store.state.loginUser)
          localStorage.removeItem('LoginUser')
          localStorage.removeItem('LoginUserRole')
          localStorage.removeItem('AuthToken')
          this.$router.push({path:'/index'})
          window.location.reload()
        })
      },
      //搜索
      onSearch: function(){
        this.$router.push({path:'/scenic', query: {kw: '关键字'}})
      },
      loadLoginUser: function(){
        this.currUser = localStorage.getItem('LoginUser')
      }
    },
    computed:{
      loginUser(){
        let lastLogin = parseFloat(localStorage.getItem('loginTime'))
        let now = (new Date()).getTime()
        console.log('当前登录，判断前：',this.$store.state.currUser)
        if(localStorage.getItem('loginTime')!='' && (now - lastLogin > 86400000)){
          localStorage.removeItem('LoginUser')
          console.log('登录超时！')
          return null
        }else if(localStorage.getItem('LoginUser')!=null){
          let currUser = JSON.parse(localStorage.getItem('LoginUser'))
          console.log('从storage里获取！')
          return currUser
        }else if(!this.$store.state.currUser.nickName){
          // if(localStorage.getItem('LoginUser')=='undefined'){return null}
          // let currUser = JSON.parse(localStorage.getItem('LoginUser'))
          // this.$store.commit('loginUser', currUser)
          // return currUser
          return null
        }else{
          console.log('当前登录???：',this.$store.state.loginUser)
          return this.$store.state.currUser
        }
      },
    }
  }
</script>
<style scoped>
body, html{
  padding: 0;
  margin: 0;
}
.h-content{
  width: 100%;
}
.h-content{
  max-width: 820px;
  margin: 0 auto;
}
.menu-list{
  list-style: none;
  padding: 0;
  margin: 0;
  display: inline-block;
}
.menu-list .menu-li{
  display: inline-block;
  padding: 5px 20px;
  line-height: 45px;
  font-size: 16px;
  position: relative;
  color: #fff;
}
.menu-list .menu-li:hover{
  background-color: #fff;
  color: #333;
  cursor: pointer;
}
.menu-list .menu-li:hover .msg{
  background-color: #d87f57;
  color: #fff;
}
.menu-li .msg{
  background-color: #fff;
  display: inline-block;
  width: 18px;
  height: 18px;
  line-height: 18px;
  border-radius: 10px;
  color: #d87f57;
  font-size: 14px;
  text-align: center;
}
.menu-list .menu-logo{
  width: 160px;
  height: 45px;
}
.zui-header{
  background-color: #5d7db3;
  height: auto;
}
.zui-header .zui-logo{
  width: 280px;
  height: 55px;
  vertical-align: middle;
  float: left;
  margin: 4px 0 0;
}
.sub-menu{
  display: none;
  list-style: none;
  position: absolute;
  top: 50px;
  background-color: #fff;
  color: #006dd9;
  width: 122px;
  left: 0;
  padding: 0;
  z-index: 99999;
}
.sub-menu .sub-item:hover{
  background-color: #3f68b0;
  color: #fff;
}
.sub-menu .sub-item{
  padding: 0 20px;
  line-height: 50px;
  font-size: 16px;
  border-top: none;
}
.menu-list .menu-li:hover .sub-menu{
  display: block;
}
.top-bar{
  max-width: 1024px;
  margin: 0 auto;
}
.top-bar .logo{
  background-image: url('../assets/img/head-logo.png');
  background-size: contain;
  background-position: center;
  height: 55px;
  width: 185px;
  background-repeat: no-repeat;
  float: left;
}
.top-bar .search-bar{
  width: 350px;
  float: right;
  position: relative;
}
.search-bar .search-ipt{
  height: 35px;
  width: 280px;
  border-radius: 25px;
  margin-top: 10px;
  border: 1px solid #3f68b0;
  padding: 0 12px;
}
.search-bar .search-button{
  position: absolute;
  right: 48px;
  top: 12px;
  background: #e2a388;
  color: #fff;
  font-weight: bold;
  width: 32px;
  height: 32px;
  border-radius: 16px;
  text-align: center;
  line-height: 32px;
  cursor: pointer;
}
.top-bar .user-login{
  float: right;
  /* width: 200px; */
  margin-top: 10px;
  font-size: 14px;
}
.user-login li{
  display: inline-block;
  min-width: 45px;
  text-align: center;
  cursor: pointer;
  line-height: 35px;
  height: 35px;
  margin: 0 5px;
  font-size: 14px;
  color: #fff;
}
.user-login li:hover{
  color: #e2a388;
}
</style>
