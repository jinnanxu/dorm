<template>
  <div class="main-contain">
    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>水电缴费</span>
        <el-button size="small" icon="el-icon-pie-chart" class="green-button" style="float: right;" @click="toStatistics()">水电费统计</el-button>
      </div>
    </div>
    <!--列表-->
    <el-table
      size="small"
      :data="listData"
      highlight-current-row
      v-loading="loading"
      border
      element-loading-text="拼命加载中"
      style="width: 100%"
    >
    <el-table-column align="center" prop="building.buildingName" label="楼宇/宿舍号">
        <template slot-scope="scope">
          <span>{{ scope.row.building.buildingName}}</span>
          <span>{{ scope.row.dorm.roomNo}}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="student.college" label="院系">
      </el-table-column> -->
      <el-table-column align="center" prop="student.majorClass" label="费用类型">
        <template slot-scope="scope">
          <span v-if="scope.row.feeType==1">电费</span>
          <span v-else>水费</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="billYear" label="账单月度">
        <template slot-scope="scope">
          <strong class="blue">{{ scope.row.billYear }}年{{ scope.row.billMonth }}月</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="unitPrice" label="水电度量">
        <template slot-scope="scope">
          <span v-if="scope.row.feeType==1">{{ scope.row.usages }}度</span>
          <span v-else>{{ scope.row.usages }}吨</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="费用总额">
        <template slot-scope="scope">
          ￥<strong class="warning">{{ scope.row.amount|amountString }}</strong>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="status" label="缴费状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 1" type="success" size="small">已交费</el-tag>
          <el-tag v-else-if="scope.row.status == 0" type="danger" size="small">待交费</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="缴费时间">
        <template slot-scope="scope">
          <span v-if="scope.row.payTime!=null">{{ scope.row.payTime|timestampToDayHM }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="90">
        <template slot-scope="scope">
          <!-- <el-button size="mini" type="danger" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-lock">详情</el-button> -->
          <el-button size="mini" type="success" @click="onShowPay(scope.row)" icon="el-icon-check" v-if="scope.row.status==0" class="orange-button">支付</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>

    <!--详情对话框-->
    <el-dialog
      title="水电费信息详情"
      :visible.sync="roomDescVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <el-descriptions class="margin-top" :column="1" border v-if="currDetailData!=null">
      <el-descriptions-item label="报修时间" labelStyle="width: 160px;">{{currDetailData.createTime|timestampToTime}}</el-descriptions-item>
      <el-descriptions-item label="房间号">{{ currDetailData.dorm.roomNo }}</el-descriptions-item>
      <el-descriptions-item label="简述">{{ currDetailData.title }}</el-descriptions-item>
      <!-- <el-descriptions-item label="标签">
        <el-tag size="small">{{ currDetailData.tags }}</el-tag>
      </el-descriptions-item> -->
      <el-descriptions-item label="报修详情">{{ currDetailData.content }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <!--0-待处理；1-已完成；2-已取消-->
        <el-tag v-if="currDetailData.status == 0" type="warning" size="small">待处理</el-tag>
        <el-tag v-else-if="currDetailData.status == 1" type="success" size="small">已完成</el-tag>
        <el-tag v-else-if="currDetailData.status == 2" type="info" size="small">已取消</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <div class="demo-image">
      <div class="block" v-for="fit in currDetailPics" :key="fit">
        <a :href="fit" target="_blank">
        <el-image
          style="width: 100px; height: 100px"
          :src="fit"
          fit="cover"></el-image>
        </a>
      </div>
    </div>
    <el-descriptions class="margin-top" :column="1" border v-if="currDetailData.status == 1">
      <el-descriptions-item label="完成时间" labelStyle="width: 160px;">{{currDetailData.createTime|timestampToTime}}</el-descriptions-item>
      <el-descriptions-item label="完成情况">{{ currDetailData.fixResult }}</el-descriptions-item>
    </el-descriptions>
    <div class="demo-image" v-if="this.currDetailFinishPics.length>0">
      <p>完成照片：</p>
      <div class="block" v-for="fit in currDetailFinishPics" :key="fit">
        <a :href="fit" target="_blank">
        <el-image
          style="width: 100px; height: 100px"
          :src="fit"
          fit="cover"></el-image>
        </a>
      </div>
    </div>
    </el-dialog>

    <!--支付对话框-->
    <el-dialog
      title="支付水电费"
      :visible.sync="payVisible"
      width="60%"
      :close-on-click-modal=false
      :show-close="false"
    >
    <el-descriptions class="margin-top" :column="2" border v-if="orderInfo!=null">
      <el-descriptions-item label="账单日期">{{orderInfo.billYear}}年{{orderInfo.billMonth}}月</el-descriptions-item>
      <el-descriptions-item label="订单金额">
        ￥
        <span style="font-size: 22px; color: #d4571d;">
          {{ orderInfo.amount|amountString }}
        </span>
      </el-descriptions-item>
    </el-descriptions>
    <div class="pay-btn">
      <div class="qr-code">
        <img src="http://118.25.89.125:18080/images/pay.png" alt="" style="width: 200px;height: 200px;">
      </div>
      <el-button size="mini" icon="el-icon-wallet" class="green-button" @click="onPay()">已支付</el-button>
      <el-button size="mini" icon="el-icon-close" class="orange-button" @click="closeDialog2()">取消</el-button>
    </div>
    </el-dialog>
  </div>
</template>

<script>
import { FrontFeeList, FrontPayFee } from '@/api/api.js'
import Pagination from '../../components/Pagination'
export default {
  data() {
    return {
      editForm: {
        dormId: '',
        title: '',
        status: 0,
        pics: '',
        content: '',
      },
      listData: [], //用户数据
      roomList: [],
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
      },
      editFormVisible: false,
      uploadPicLoading: false, //详情对话框显示与隐藏
      roomDescVisible: false,
      payVisible: false,
      orderInfo: null,
      rules: {
        title: [{ required: true, message: '请输入', trigger: 'blur' }],
      },
      currDetailData: {
        confirmPic: null,
        createTime: null,
        finishTime: null,
        pics: null,
        dorm: {},
        dormId: null,
        fixId: null,
        content: "",
        serviceType: 1,
        status: 0,
        title: "",
        userId: "",
      },
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
        return null
      }else{
        console.log('当前登录???：',this.$store.state.loginUser)
        return this.$store.state.currUser
      }
    }
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取列表
    getdata(parameter) {
      this.loading = true
      FrontFeeList(parameter)
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
      this.currDetailFinishPics=[]
      this.roomDescVisible = true
      this.currDetailData = row
      this.currDetailPics = row.pics.split(",")
      if(row.confirmPic!=null){
        this.currDetailFinishPics = row.confirmPic.split(",")
      }
    },

    //显示编辑界面
    handleEdit() {
      this.editFormVisible = true
      this.title = '我要报修/清洁'
      this.editForm.dormId = '',
      this.editForm.serviceType = 1,
      this.editForm.title = '',
      this.editForm.pics = '',
      this.editForm.content = ''
    },

    // 交费
    onPay(order){
      let req={
        feeId: this.orderInfo.feeId,
      }
      FrontPayFee(req).then((res) => {
        if (res.code==0) {
          this.$message({
            type: 'success',
            message: '支付成功'
          })
          // this.getdata(this.formInline)
          this.payVisible = false
          window.location.reload()
        } else {
          this.$message.error(res.msg);
        }
      }).catch((err) => {
        this.$message.error("支付失败！");
      });
    },

    onShowPay(row){
      this.payVisible = true
      console.log('支付窗口：', row)
      this.orderInfo=row
    },

    toStatistics(){
      this.$router.push({path:'/fee/charts'})
    },

    // 关闭编辑、增加弹出框
    closeDialog(dialog) {
      if (dialog == 'edit') {
        this.editFormVisible = false
      }
    },

    closeDialog2(){
      this.roomDescVisible = false
      this.payVisible = false
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
.blue{
    color: #5d7db3;
  }
  .success{
    color: #67C23A;
  }
  .warning{
    color: #E6A23C;
  }
</style>