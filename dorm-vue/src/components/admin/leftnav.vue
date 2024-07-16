/**
* 后台管理左边菜单
*/ 
<template>
  <div class="menulist">
    <el-menu :collapse="collapsed" collapse-transition router :default-active="$route.path"
      v-if="user.role==1"
     unique-opened class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b">
      <div class="logobox">
        <div>宿舍管理系统</div>
        <!-- <img class="logoimg" src="@/assets/img/logo.png" alt=""> -->
      </div>
      <el-submenu v-for="menu in adminMenu.adminMenus" :key="menu.menuid" :index="menu.menuname">
        <template slot="title">
          <font-awesome-icon :icon="['fas', menu.icon]"></font-awesome-icon>
          <span>{{menu.menuname}}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="'/'+chmenu.url" :key="chmenu.menuid">
            <font-awesome-icon :icon="['fas', chmenu.icon]"></font-awesome-icon>
            <span>{{chmenu.menuname}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
    <el-menu v-if="user.role==2" :collapse="collapsed" collapse-transition router :default-active="$route.path" unique-opened class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b">
      <div class="logobox">
        <div>宿舍管理系统</div>
      </div>
      <el-submenu v-for="menu in adminMenu.managerMenus" :key="menu.menuid" :index="menu.menuname">
        <template slot="title">
          <font-awesome-icon :icon="['fas', menu.icon]"></font-awesome-icon>
          <span>{{menu.menuname}}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="'/'+chmenu.url" :key="chmenu.menuid">
            <font-awesome-icon :icon="['fas', chmenu.icon]"></font-awesome-icon>
            <span>{{chmenu.menuname}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
    <el-menu v-if="user.role==3" :collapse="collapsed" collapse-transition router :default-active="$route.path" unique-opened class="el-menu-vertical-demo" background-color="#334157" text-color="#fff" active-text-color="#ffd04b">
      <div class="logobox">
        <div>宿舍管理系统</div>
      </div>
      <el-submenu v-for="menu in adminMenu.workerMenus" :key="menu.menuid" :index="menu.menuname">
        <template slot="title">
          <font-awesome-icon :icon="['fas', menu.icon]"></font-awesome-icon>
          <span>{{menu.menuname}}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item v-for="chmenu in menu.menus" :index="'/'+chmenu.url" :key="chmenu.menuid">
            <font-awesome-icon :icon="['fas', chmenu.icon]"></font-awesome-icon>
            <span>{{chmenu.menuname}}</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
    
  </div>
</template>
<script>
export default {
  name: 'leftnav',
  data() {
    return {
      collapsed: false,
      adminMenu: [],
      user: {}
    }
  },
  // 创建完毕状态(里面是操作)
  created() {
    this.user = JSON.parse(localStorage.getItem('AdminInfo'))
    // if(this.user==null){
    //   this.user = {userRole:1, realName: '开发模式'}
    // }
    // if(this.user.role==2){
    //   this.userRole = '管理员'
    // }
    this.adminMenu = {
      success: true,
      adminMenus: [
        {
          menuid: 1,
          icon: 'users',
          menuname: '用户管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 11,
              icon: 'user',
              menuname: '学生信息管理',
              hasThird: 'N',
              url: 'admin/usermgr',
              menus: null
            },
            {
              menuid: 12,
              icon: 'vcard',
              menuname: '宿舍管理员',
              hasThird: 'N',
              url: 'admin/expert/mgr',
              menus: null
            },{
              menuid: 13,
              icon: 'edit',
              menuname: '宿舍管理修改申请',
              hasThird: 'N',
              url: 'admin/expert/update/validate',
              menus: null
            }, {
              menuid: 14,
              icon: 'tools',
              menuname: '维修师管理',
              hasThird: 'N',
              url: 'admin/worker',
              menus: null
            },
          ]
        },
        {
          menuid: 2,
          icon: 'building',
          menuname: '宿舍楼宇管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 21,
              icon: 'building',
              menuname: '楼宇信息',
              hasThird: 'N',
              url: 'admin/building/list',
              menus: null
            },{
              menuid: 22,
              icon: 'home',
              menuname: '宿舍信息',
              hasThird: 'N',
              url: 'admin/dorm/list',
              menus: null
            }
          ]
        }, {
          menuid: 5,
          icon: 'cogs',
          menuname: '个人中心',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 51,
              icon: 'info-circle',
              menuname: '个人信息',
              hasThird: 'N',
              url: 'admin/profile',
              menus: null
            }
          ]
        }
      ],
      // 管理员菜单
      managerMenus: [
        {
          menuid: 1,
          icon: 'tachometer-alt',
          menuname: '数据看板',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 11,
              icon: 'line-chart',
              menuname: '数据概览',
              hasThird: 'N',
              url: 'manager/index',
              menus: null
            },{
              menuid: 12,
              icon: 'pie-chart',
              menuname: '数据统计',
              hasThird: 'N',
              url: 'manager/statistics',
              menus: null
            },
          ]
        }, {
          menuid: 3,
          icon: 'sliders-h',
          menuname: '宿舍事务管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 31,
              icon: 'bed',
              menuname: '入住管理',
              hasThird: 'N',
              url: 'manager/occ',
              menus: null
            },{
              menuid: 32,
              icon: 'cny',
              menuname: '水电费管理',
              hasThird: 'N',
              url: 'manager/fee',
              menus: null
            },{
              menuid: 33,
              icon: 'tools',
              menuname: '报修管理',
              hasThird: 'N',
              url: 'manager/fix',
              menus: null
            },{
              menuid: 34,
              icon: 'list',
              menuname: '离校管理',
              hasThird: 'N',
              url: 'manager/leave',
              menus: null
            }
          ]
        },{
          menuid: 4,
          icon: 'newspaper',
          menuname: '通知公告管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 41,
              icon: 'list-alt',
              menuname: '通知公告列表',
              hasThird: 'N',
              url: 'manager/notice',
              menus: null
            }
          ]
        }, {
          menuid: 71,
          icon: 'cogs',
          menuname: '个人中心',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 73,
              icon: 'info-circle',
              menuname: '个人信息',
              hasThird: 'N',
              url: 'manager/profile',
              menus: null
            }
          ]
        }
      ],
      workerMenus: [
        {
          menuid: 3,
          icon: 'sliders-h',
          menuname: '报修管理',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 33,
              icon: 'tools',
              menuname: '报修记录',
              hasThird: 'N',
              url: 'manager/fix',
              menus: null
            }
          ]
        },{
          menuid: 71,
          icon: 'cogs',
          menuname: '个人中心',
          hasThird: null,
          url: null,
          menus: [
            {
              menuid: 73,
              icon: 'info-circle',
              menuname: '个人信息',
              hasThird: 'N',
              url: 'manager/profile',
              menus: null
            }
          ]
        }
      ],
      msg: 'success'
    }
    
  }
}
</script>
<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 240px;
  min-height: 400px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  border: none;
  text-align: left;
}
.el-menu-item-group__title {
  padding: 0px;
}
.el-menu-bg {
  background-color: #1f2d3d !important;
}
.el-menu {
  border: none;
}
.logobox {
  height: 40px;
  line-height: 40px;
  color: #fff;
  font-size: 20px;
  text-align: center;
  padding: 10px 0px;
}
.logoimg {
  height: 40px;
}
</style>