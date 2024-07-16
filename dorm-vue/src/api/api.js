//api.js
import service from './request'

//宿舍管理员注册
export const userRegist = data => {
    console.log('触发封装的Axios方法！')
    return service({
        url: '/dorm-server/admin/user/manager/save',
        method: 'post',
        data
    })
};

//后台用户登录
export const AdminLogin = data => {
    return service({
        url: '/dorm-server/admin/login',
        method: 'post',
        data
    })
};

//更新个人信息
export const updateProfile = data => {
    return service({
        url: '/dorm-server/admin/updateProfile',
        method: 'post',
        data
    })
};

//管理员更新用户信息
export const AdminUpdateUser = data => {
    return service({
        url: '/dorm-server/admin/user/save',
        method: 'post',
        data
    })
};

export const AdminUpdateUser2 = data => {
    return service({
        url: '/dorm-server/admin/user/update2',
        method: 'post',
        data
    })
};

//管理员获取前台用户列表
export const AdminUserList = data => {
    return service({
        url: '/dorm-server/admin/user/list',
        method: 'post',
        data
    })
};

//管理员获取宿舍管理员用户列表
export const AdminManagerList = data => {
    return service({
        url: '/dorm-server/admin/user/manager/list',
        method: 'post',
        data
    })
};

//更新宿舍管理员状态
export const UpdateApartmentMgrStatus = data => {
    return service({
        url: '/dorm-server/admin/user/manager/update',
        method: 'post',
        data
    })
};

export const AdminManagerModifyList = data => {
    return service({
        url: '/dorm-server/admin/user/manager/modify/list',
        method: 'post',
        data
    })
};

export const UpdateApartmentMgrModifyStatus = data => {
    return service({
        url: '/dorm-server/admin/user/manager/modify/update',
        method: 'post',
        data
    })
};

export const AdminWorkerList = data => {
    return service({
        url: '/dorm-server/admin/user/worker/list',
        method: 'post',
        data
    })
};

/********管理员：基础数据管理****** */
export const AdminGetExpertList = data => {
    return service({
        url: '/dorm-server/admin/user/manager/list',
        method: 'post',
        data
    })
};
export const AdminUpdateExpert = data => {
    return service({
        url: '/dorm-server/admin/user/manager/save',
        method: 'post',
        data
    })
};
export const AdminDeleteExpert = data => {
    return service({
        url: '/dorm-server/admin/user/manager/delete',
        method: 'post',
        data
    })
};
export const AdminGetStudentList = data => {
    return service({
        url: '/dorm-server/admin/user/list',
        method: 'post',
        data
    })
};

export const AdminGetAllStudentList = data => {
    return service({
        url: '/dorm-server/admin/user/list/all',
        method: 'post',
        data
    })
};

export const AdminUpdateStudent = data => {
    return service({
        url: '/dorm-server/admin/user/save',
        method: 'post',
        data
    })
};
export const AdminDeleteStudent = data => {
    return service({
        url: '/dorm-server/admin/user/delete',
        method: 'post',
        data
    })
};

//:获取报修列表
export const RoomServiceList = data => {
    return service({
        url: '/dorm-server/manager/service/list',
        method: 'post',
        data
    })
};

// ：更新报修信息
export const UpdateRoomService = data => {
    return service({
        url: '/dorm-server/manager/service/update',
        method: 'post',
        data
    })
};

// ：更新报修状态
export const UpdateRoomServiceStatus = data => {
    return service({
        url: '/dorm-server/manager/service/updateStatus',
        method: 'post',
        data
    })
};
/********管理员：基础数据管理****** */

/**********公告管理********* */
export const AdminGetNoticeList = data => {
    return service({
        url: '/dorm-server/admin/notice/list',
        method: 'post',
        data
    })
};
export const AdminUpdateNotice = data => {
    return service({
        url: '/dorm-server/admin/notice/save',
        method: 'post',
        data
    })
};
export const AdminDeleteNotice = data => {
    return service({
        url: '/dorm-server/admin/notice/delete',
        method: 'post',
        data
    })
};
/**********公告管理END********* */

export const AdminGetBuildingList = data => {
    return service({
        url: '/dorm-server/admin/building/list',
        method: 'post',
        data
    })
};
export const GetAllBuildingList = data => {
    return service({
        url: '/dorm-server/admin/building/list/all',
        method: 'post',
        data
    })
};

export const AdminUpdateBuilding = data => {
    return service({
        url: '/dorm-server/admin/building/save',
        method: 'post',
        data
    })
};
export const AdminDeleteBuilding = data => {
    return service({
        url: '/dorm-server/admin/building/delete',
        method: 'post',
        data
    })
};

export const AdminGetDormList = data => {
    return service({
        url: '/dorm-server/admin/dorm/list',
        method: 'post',
        data
    })
};

export const AdminGetDormByBuilding = data => {
    return service({
        url: '/dorm-server/admin/building/dorm',
        method: 'post',
        data
    })
};

export const AdminUpdateDorm = data => {
    return service({
        url: '/dorm-server/admin/dorm/save',
        method: 'post',
        data
    })
};

export const AdminUpdateDormStatus = data => {
    return service({
        url: '/dorm-server/admin/dorm/update/status',
        method: 'post',
        data
    })
};

export const FrontUserLogin = data => {
    return service({
        url: '/dorm-server/user/login',
        method: 'post',
        data
    })
};

export const FrontUserRegist = data => {
    return service({
        url: '/dorm-server/user/register',
        method: 'post',
        data
    })
};

export const FrontUpdateProfile = data => {
    return service({
        url: '/dorm-server/user/updateUser',
        method: 'post',
        data
    })
};

export const GetNoticeList = data => {
    return service({
        url: '/dorm-server/notice/list',
        method: 'post',
        data
    })
};

export const GetNoticeDetail = data => {
    return service({
        url: '/dorm-server/front/info/detail',
        method: 'post',
        data
    })
};

//我的报修记录
export const SelectMyServiceList = data => {
    return service({
        url: '/dorm-server/front/fix/list',
        method: 'post',
        data
    })
};

//提交报修请求
export const SaveRoomService = data => {
    return service({
        url: '/dorm-server/front/fix/save',
        method: 'post',
        data
    })
};

export const SaveRoomService2 = data => {
    return service({
        url: '/dorm-server/front/fix/manager/save',
        method: 'post',
        data
    })
};

export const CancelRoomService = data => {
    return service({
        url: '/dorm-server/front/fix/cancel',
        method: 'post',
        data
    })
}; 

//宿舍管理员：入住管理
export const OccupationManager = data => {
    return service({
        url: '/dorm-server/admin/occ/list',
        method: 'post',
        data
    })
};

export const SaveOccupation = data => {
    return service({
        url: '/dorm-server/admin/occ/save',
        method: 'post',
        data
    })
};


//宿舍管理员：水电费管理
export const ManagerGetFeeList = data => {
    return service({
        url: '/dorm-server/manager/fee/list',
        method: 'post',
        data
    })
};

export const ManagerSaveFee = data => {
    return service({
        url: '/dorm-server/manager/fee/save',
        method: 'post',
        data
    })
};

export const ManagerUpdateFeeStatus = data => {
    return service({
        url: '/dorm-server/manager/fee/update/status',
        method: 'post',
        data
    })
};

export const ManagerSendFeeMsg = data => {
    return service({
        url: '/dorm-server/manager/fee/send/message',
        method: 'post',
        data
    })
};

//宿舍管理员：报修管理
export const ManagerFixList = data => {
    return service({
        url: '/dorm-server/manager/fix/list',
        method: 'post',
        data
    })
};

export const ManagerFixUpdate = data => {
    return service({
        url: '/dorm-server/manager/fix/update',
        method: 'post',
        data
    })
};

//宿舍管理员：通知公告管理
export const ManagerNoticeList = data => {
    return service({
        url: '/dorm-server/manager/notice/list',
        method: 'post',
        data
    })
};

export const ManagerNoticeUpdate = data => {
    return service({
        url: '/dorm-server/manager/notice/save',
        method: 'post',
        data
    })
};

export const ManagerNoticeDelete = data => {
    return service({
        url: '/dorm-server/manager/notice/delete',
        method: 'post',
        data
    })
};

export const FrontNoticeList = data => {
    return service({
        url: '/dorm-server/front/info/list',
        method: 'post',
        data
    })
};

export const FrontNoticeDetail = data => {
    return service({
        url: '/dorm-server/info/detail',
        method: 'post',
        data
    })
};

export const FrontIndexInfo = data => {
    return service({
        url: '/dorm-server/front/index/info',
        method: 'post',
        data
    })
};

export const FrontReadMsg = data => {
    return service({
        url: '/dorm-server/front/index/read/msg',
        method: 'post',
        data
    })
};

export const FrontFeeList = data => {
    return service({
        url: '/dorm-server/front/fee/list',
        method: 'post',
        data
    })
};

export const FrontPayFee = data => {
    return service({
        url: '/dorm-server/front/fee/pay',
        method: 'post',
        data
    })
};

export const FrontFeeStatistics = data => {
    return service({
        url: '/dorm-server/front/fee/statics/data',
        method: 'post',
        data
    })
};

export const MyWaitPayCount = data => {
    return service({
        url: '/dorm-server/front/index/wait/pay',
        method: 'post',
        data
    })
};

export const Statistics = data => {
    return service({
        url: '/dorm-server/admin/statics/data',
        method: 'post',
        data
    })
};

export const BashboardData = data => {
    return service({
        url: '/dorm-server/admin/index/data',
        method: 'post',
        data
    })
};

export const SaveLeaveApp = data => {
    return service({
        url: '/dorm-server/front/leave/save',
        method: 'post',
        data
    })
};

export const GetMyLeaveApp = data => {
    return service({
        url: '/dorm-server/front/leave/list',
        method: 'post',
        data
    })
};

export const GetLeaveList = data => {
    return service({
        url: '/dorm-server/manager/leave/list',
        method: 'post',
        data
    })
}