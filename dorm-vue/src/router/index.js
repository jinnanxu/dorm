import { Statistic } from 'element-ui'
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)
//前台
const HomeFramePage = () => import('../views/HomeFramePage')
const LoginPage = () => import('../views/user/Login')
const RegistPage = () => import('../views/user/Regist')
const IndexPage = () => import('../views/front/IndexPage')
const Profile = () => import('../views/user/Profile')
const NoticeDetail = () => import('../views/front/NoticeDetail')
const FixService = () => import('../views/front/FixService')
const MyFee = () => import('@/views/front/MyFee')
const LeaveApp = () => import('@/views/front/LeaveApp')
const MyFeeStatistics = () => import('@/views/front/MyFeeStatistics')

const AdminPage = () => import('@/views/admin/AdminFramwork')
const AdminLoginPage = () => import('../views/admin/AdminLogin')
const UserManager = () => import('@/views/admin/UserManager')
const AdminUserManager = () => import('@/views/admin/AdminUserManager')
const AdminUserUpdateManager = () => import('@/views/admin/AdminUserUpdateManager')
const AdminProfile = () => import('@/views/admin/AdminProfile')
const BuildingManager = () =>  import('@/views/admin/BuildingManager')
const DormManager = () =>  import('@/views/admin/DormManager')
const AdminWorkerManager = () =>  import('@/views/admin/AdminWorkerManager')

const Dashboard = () => import('@/views/manager/Dashboard')
const OccupationManager = () =>  import('@/views/manager/OccupationManager')
const FeeManager = () =>  import('@/views/manager/FeeManager')
const FixManager = () =>  import('@/views/manager/FixManager')
const NoticeManager = () =>  import('@/views/manager/NoticeManager')
const Statistics = () =>  import('@/views/manager/Statistics')
const LeaveManager = () =>  import('@/views/manager/LeaveManager')

const router = new VueRouter({
  mode: 'history',
  base: '/dorm/', //增加前缀，这样可以在云服务器的nginx配置多个项目，涉及到nginx的配置 如果不要前台，则填“__dirname”,
  routes: [
    {
      path: "/",
      name: "登录页面",
      component: LoginPage
    },{
      //前台页面路由
      path: '/',
      component: HomeFramePage,
      children: [
        {
          path: "index",
          name: "登录页面",
          component: IndexPage
        },{
          path: "login",
          name: "登录页面",
          component: LoginPage
        }, {
          path: "reg",
          name: "注册页面",
          component: RegistPage
        }, {
          path: "profile",
          name: "个人信息",
          component: Profile
        }, {
          path: "notice/detail",
          name: "公告详情",
          component: NoticeDetail
        }, {
          path: "fix",
          name: "报修",
          component: FixService
        }, {
          path: "fee",
          name: "交费",
          component: MyFee
        }, {
          path: "fee/charts",
          name: "水电统计",
          component: MyFeeStatistics
        }, {
          path: "leave",
          name: "离校申请",
          component: LeaveApp
        },
      ]
    }, {
      path: "/admin/login",
      name: "管理员登录",
      component: AdminLoginPage
    }, {
      path: '/admin',
      component: AdminPage,
      children: [
        {
          path: "usermgr",
          name: "用户管理",
          component: UserManager
        },{
          path: "profile",
          name: "个人信息",
          component: AdminProfile
        },{
          path: "expert/mgr",
          name: "宿舍管理员管理",
          component: AdminUserManager
        },{
          path: "expert/update/validate",
          name: "宿舍管理员修改审核",
          component: AdminUserUpdateManager
        },{
          path: "worker",
          name: "维修工人",
          component: AdminWorkerManager
        },{
          path: "building/list",
          name: "楼宇管理",
          component: BuildingManager
        },{
          path: "dorm/list",
          name: "宿舍管理",
          component: DormManager
        },
      ]
    }, {
      path: '/manager', // 宿舍管理员 
      component: AdminPage,
      children: [
        {
          path: "index",
          name: "管理主页",
          component: Dashboard
        },{
          path: "occ",
          name: "入住管理",
          component: OccupationManager
        },{
          path: "fee",
          name: "水电费管理",
          component: FeeManager
        },{
          path: "fix",
          name: "水电费管理",
          component: FixManager
        },{
          path: "statistics",
          name: "数据统计",
          component: Statistics
        },{
          path: "notice",
          name: "通知公告管理",
          component: NoticeManager
        },{
          path: "leave",
          name: "离校管理",
          component: LeaveManager
        },{
          path: "profile",
          name: "个人信息",
          component: AdminProfile
        },
      ]
    },
  ]
});


export default router;
