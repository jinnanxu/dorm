<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>学生离校留校申请</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="楼宇" prop="buildingId">
        <el-select v-model="formInline.buildingId" placeholder="请选择" clearable size="small">
          <el-option
            v-for="item in allBuildingList"
            :key="item.buildingId"
            :label="item.buildingName"
            :value="item.buildingId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="getdata"
          >搜索</el-button
        >
      </el-form-item>
    </el-form>
    <!--列表 hotelId roomNo location floor price status-->
    <el-table
      size="small"
      :data="listData"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="拼命加载中"
      style="width: 100%"
    >
      <el-table-column align="center" label="申请时间">
        <template slot-scope="scope">
          <div>{{scope.row.createTime|timestampToDayHM}}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="dorm.roomNo" label="宿舍">
        <template slot-scope="scope">
          <span>{{ scope.row.building.buildingName }} {{ scope.row.dorm.roomNo }}</span>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="类型">
        <template slot-scope="scope">
          <div v-if="scope.row.leaveType==1">离校</div>
          <div v-if="scope.row.leaveType==2">节假日留校</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="离/留校时间">
        <template slot-scope="scope">
          <div>{{scope.row.leaveTime}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="reason" label="事由" show-overflow-tooltip>
      </el-table-column>
      <!-- <el-table-column align="center" prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">已完成</el-tag>
          <el-tag v-if="scope.row.status == 0" type="danger" size="small">待处理</el-tag>
          <el-tag v-if="scope.row.status == 2" type="info" size="small">已取消</el-tag>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-document">详情</el-button>
          <!-- <el-button
            size="mini"
            class="orange-button"
            icon="el-icon-close"
            v-if="scope.row.status == 0"
            @click="onCancel(scope.row)"
            >取消</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      v-bind:child-msg="pageparm"
      @callFather="callFather"
    ></Pagination>

    <!--详情对话框-->
    <el-dialog
      title="信息详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="1" border v-if="currDetailData!=null">
      <el-descriptions-item label="申请时间" labelStyle="width: 160px;">{{currDetailData.createTime|timestampToTime}}</el-descriptions-item>
      <el-descriptions-item label="宿舍">{{currDetailData.building.buildingName}} {{ currDetailData.dorm.roomNo }}</el-descriptions-item>
      <el-descriptions-item label="类型">
        <div v-if="currDetailData.leaveType==1">离校</div>
        <div v-if="currDetailData.leaveType==2">节假日留校</div>
      </el-descriptions-item>
      <!-- <el-descriptions-item label="标签">
        <el-tag size="small">{{ currDetailData.tags }}</el-tag>
      </el-descriptions-item> -->
      <el-descriptions-item label="事由">{{ currDetailData.reason }}</el-descriptions-item>
    </el-descriptions>
    <div class="demo-image" v-if="currDetailPics.length>0">
      <p>请假单：</p>
      <div class="block" v-for="fit in currDetailPics" :key="fit">
        <a :href="fit" target="_blank">
        <el-image
          style="width: 100px; height: 100px"
          :src="fit"
          fit="cover"></el-image>
        </a>
      </div>
    </div>
    </el-dialog>
  </div>
</template>

<script>
import { GetLeaveList, SaveLeaveApp, GetAllBuildingList } from '@/api/api.js'
import { fileUploader } from '@/api/upload.js'
import Pagination from '../../components/Pagination'
export default {
  data() {
    return {
      editForm: {
        leaveType: 1,
        leaveTime: '',
        pics: '',
        reason: '',
      },
      listData: [], //用户数据
      allBuildingList: [],
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10
      },
      title: '',
      formInline: {
        currentpage: 1,
        limit: 10,
        buildingId: ''
      },
      editFormVisible: false,
      uploadPicLoading: false, //详情对话框显示与隐藏
      roomDescVisible: false,
      rules: {
        reason: [{ required: true, message: '请输入', trigger: 'blur' }],
        leaveType: [{ required: true, message: '请选择', trigger: 'blur' }],
        leaveTime: [{ required: true, message: '请选择', trigger: 'blur' }],
      },
      currDetailData: {
        confirmPic: null,
        createTime: null,
        finishTime: null,
        pics: null,
        dorm: {},
        building: {},
        dormId: null,
        fixId: null,
        reason: "",
        serviceType: 1,
        status: 0,
        title: "",
        userId: "",
      },
      dateRange: '',
      currDetailPics: [],
      currDetailFinishPics: []
    }
  },
  // 注册组件
  components: {
    Pagination
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    const cId = this.$route.query.nid
    this.getdata(this.formInline)
    GetAllBuildingList({}).then((res) => {
      if (res.success) {
        this.allBuildingList = res.data
      }
    })
  },

  computed:{
    
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取列表
    getdata(parameter) {
      this.loading = true
      GetLeaveList(this.formInline)
        .then(res => {
          this.loading = false
          console.log('订单列表', res)
          if (res.success) {
            this.listData = res.data.records
            this.pageparm.currentPage = res.data.currentpage
            this.pageparm.pageSize = res.data.pageSize
            this.pageparm.total = res.data.totalrecord;
          } else {
            this.$message({
              type: 'info',
              message: res.msg
            })
          }
        })
        .catch(err => {
          this.loading = false
          this.$message.error('数据拉取失败，请稍后再试！')
        })
    },
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage
      this.formInline.limit = parm.pageSize
      this.getdata(this.formInline)
    },
    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },

    //显示详情界面
    handleDetail: function (index, row) {
      this.currDetailPics=[]
      this.roomDescVisible = true
      this.currDetailData = row
      if(row.pics!=null&&row.pics!=''){
        this.currDetailPics = row.pics.split(",")
      }
    },

    //显示编辑界面
    handleEdit() {
      this.editFormVisible = true
      this.title = '我要申请'
      this.editForm.leaveType = 1
      this.editForm.title = ''
      this.editForm.pics = ''
      this.editForm.reason = ''
      this.editForm.leaveTime = ''
    },

    // 编辑、添加提交方法
    submitForm(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          // 请求方法
          let ltime = this.editForm.leaveTime.join("至")
          this.editForm.leaveTime = ltime
          SaveLeaveApp(this.editForm)
            .then(res => {
              this.editFormVisible = false
              this.loading = false
              if (res.success) {
                this.$message({
                  type: 'success',
                  message: '提交成功！'
                })
                this.getdata(this.formInline)
              } else {
                this.$message({
                  type: 'info',
                  message: res.msg
                })
              }
            }).catch(err => {
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

    // 取消订单
    onCancel(order){
      this.$confirm('确认要取消该报修申请吗？', '信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let req={
          fixId: order.fixId,
        }
        CancelRoomService(req).then((res) => {
          if (res.code==0) {
            this.$message({
              type: 'success',
              message: '取消成功'
            })
            this.getdata(this.formInline)
          } else {
            this.$message.error(res.msg);
          }
        }).catch((err) => {
          this.$message.error("取消失败！");
        });
      })
    },

    onShowPay(row){
      this.payVisible = true
      this.orderInfo=row
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
          this.editForm.pics = res.data.data[0]
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

    closeDialog2(){
      this.roomDescVisible = false
    },

    delPic(){
      this.editForm.pic=''
      this.files=null
    }
  }
}
</script>

<style scoped>
.main-contain .el-button{
  margin-left: 2px;
}
.dtitle{
  font-size: 16px;
  margin: 10px 0;
  color: #666;
}
.introduction{
  margin: 15px 0;
}
.price{
  color: #d77f57;
}
.show-btn{
  text-align: center;
  margin-top: 100px;
  clear: both;
}
.opname{
  vertical-align: top;
}
.qr-code, .pay-btn{
  text-align: center;
}
.qr-code{
  margin: 12px 0;
}
.main-contain .detail-button{
  background-color: #8cb9be;
  color: #fff;
}
.main-contain .detail-button:hover{
  background-color: #91b5a9;
}
.pay-type{
  margin: 15px 0;
}
.top-nav .page-title{
  box-sizing: border-box;
  padding-right: 0;
  width: 98%;
  line-height: 32px;
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
  height: 120px;
}
</style> 
 