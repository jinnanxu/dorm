<template>
  <div class="main-contain">
    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>报修记录</span>
        <el-button size="small" icon="el-icon-plus" class="orange-button" style="float: right;" @click="handleEdit()">我要报修</el-button>
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
      <el-table-column align="center" prop="dorm.roomNo" label="房间号">
        <template slot-scope="scope">
          <span>{{ scope.row.building.buildingName }} {{ scope.row.dorm.roomNo }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="title" label="事项标题">
      </el-table-column>
      <el-table-column align="center" label="报修时间">
        <template slot-scope="scope">
          <div>{{scope.row.createTime|timestampToDayHM}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态">
        <template slot-scope="scope">
          <!--0-待处理；1-已完成；2-已取消-->
          <el-tag v-if="scope.row.status == 1" type="success" size="small">已完成</el-tag>
          <el-tag v-if="scope.row.status == 0" type="danger" size="small">待处理</el-tag>
          <el-tag v-if="scope.row.status == 2" type="info" size="small">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="处理结果" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.fixResult }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" class="green-button" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-document">详情</el-button>
          <el-button
            size="mini"
            class="orange-button"
            icon="el-icon-close"
            v-if="scope.row.status == 0"
            @click="onCancel(scope.row)"
            >取消</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>

    <!--表单对话框-->
    <el-dialog :title="title" :visible.sync="editFormVisible" width="50%" @click='closeDialog("edit")'>
      <el-form label-width="80px" ref="editForm" :model="editForm" :rules="rules">
        <el-form-item label="宿舍" prop="dormId">
          <el-input size="small" :value="loginUser.occ.building.buildingName+' '+loginUser.occ.dorm.roomNo" disabled ></el-input>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input size="small" v-model="editForm.title" auto-complete="off" placeholder="请简要描述需求"></el-input>
        </el-form-item>
        <el-form-item label="详细描述" prop="content">
          <el-input size="small" v-model="editForm.content" type="textarea" placeholder="请详细描述您的需求"></el-input>
        </el-form-item>
        <el-form-item label="附加图片" prop="pic" v-loading="uploadPicLoading">
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
              <li v-if="editForm.pics==null||editForm.pics==''" class="el-icon-plus avatar-uploader-icon"></li>
              <li class="img-li" v-else>
                <img :src="editForm.pics" class="avatar" />
                <i class="del-btn el-icon-delete-solid" @click="delPic()"></i>
              </li>
            </ul>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" class="orange-button" @click='closeDialog("edit")'>取消</el-button>
        <el-button size="small" class="green-button title" :loading="loading" @click="submitForm('editForm')">保存</el-button>
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog
      title="报修信息详情"
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
      
      <el-descriptions-item label="维修师傅" v-if="currDetailData.worker!=null">{{ currDetailData.worker.realName }}</el-descriptions-item>
      <el-descriptions-item label="联系电话" v-if="currDetailData.worker!=null">{{ currDetailData.worker.mobile }}</el-descriptions-item>
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
  </div>
</template>

<script>
import { SelectMyServiceList, SaveRoomService, CancelRoomService } from '@/api/api.js'
import { fileUploader } from '@/api/upload.js'
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
      SelectMyServiceList(parameter)
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
      this.title = '我要报修'
      this.editForm.dormId = '',
      this.editForm.serviceType = 1,
      this.editForm.title = '',
      this.editForm.pics = '',
      this.editForm.content = ''
    },

    // 编辑、添加提交方法
    submitForm(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          // 请求方法
          SaveRoomService(this.editForm)
            .then(res => {
              this.editFormVisible = false
              this.loading = false
              if (res.success) {
                this.$message({
                  type: 'success',
                  message: '数据更新成功！'
                })
                this.getdata(this.formInline)
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