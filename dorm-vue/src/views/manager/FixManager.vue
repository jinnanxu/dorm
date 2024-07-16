<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>报修管理</el-breadcrumb-item>
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
      <el-form-item label="状态：">
        <el-select v-model="formInline.status" size="small" clearable>
          <!--0-待处理；1-已完成；2-已取消-->
          <el-option label="待处理" :value="0"></el-option>
          <el-option label="已完成" :value="1"></el-option>
          <el-option label="已取消" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="search"
          >搜索</el-button
        >
        <!-- <el-button
            size="small"
            type="success"
            icon="el-icon-plus"
            @click="handleNew()"
        >添加</el-button> -->
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
      <el-table-column align="center" prop="building.buildingName" label="楼宇/宿舍号">
        <template slot-scope="scope">
          <span class="blue">{{ scope.row.building.buildingName}}</span>
          <span>{{ scope.row.dorm.roomNo}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="报修时间">
        <template slot-scope="scope">
          <div>{{scope.row.createTime|timestampToTime}}</div>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="title" label="简述">
      </el-table-column>
      <el-table-column align="center" prop="content" label="详情" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="worker.realName" label="维修员" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <!--0-待处理；1-已完成；2-已取消-->
          <span v-if="scope.row.status == 0" style="color:#F56C6C;">待处理</span>
          <span v-else-if="scope.row.status == 1" style="color:#67C23A;">已完成</span>
          <span v-else-if="scope.row.status == 2" style="color:#666666;">已取消</span>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" sortable prop="floor" label="最后更新">
        <template slot-scope="scope">
          <div>{{scope.row.lastUpdate|timestampToTime}}</div>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDetail(scope.$index, scope.row)" icon="el-icon-document">详情</el-button>
          <el-button
            size="mini"
            type="success"
            icon="el-icon-check"
            v-if="scope.row.status == 0"
            @click="handleEdit(scope.$index, scope.row)"
            >完成</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      v-bind:child-msg="pageparm"
      @callFather="callFather"
    ></Pagination>

    <!-- 编辑界面 -->
    <el-dialog
      :title="title"
      :visible.sync="editFormVisible"
      width="50%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
        :model="editForm"
        ref="editForm"
      >
        <el-form-item label="报修简述" prop="title">
          <el-input
            size="small"
            v-model="currDetailData.title"
            auto-complete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="维修情况" prop="fixResult" id='quillEditorQiniu'>
          <el-input type="textarea" v-model="editForm.fixResult"></el-input>
        </el-form-item>
        <el-form-item label="维修图片" prop="role">
          <el-upload 
            class="avatar-uploader showUploader"
            ref="businessLicense"
            action
            multiple
            :auto-upload="false"
            :on-change="uploadSectionFile"
            :limit="3"
          >
            <ul class="img-list">
              <li v-for="(pic,i) in photos" :key="i" class="img-li">
                <img :src="pic" class="avatar" />
              </li>
              <li v-if="photos.length==0 || photos.length<6" class="el-icon-plus avatar-uploader-icon"></li>
            </ul>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          :loading="loading"
          class="title"
          @click="submitForm('editForm')"
          >提交</el-button
        >
      </div>
    </el-dialog>

    <!--新增表单对话框-->
    <el-dialog :title="title" :visible.sync="addFormVisible" width="50%" @click='closeDialog("edit")'>
      <el-form label-width="80px" ref="editForm2" :model="editForm2" :rules="rules">
        <el-form-item label="楼宇" prop="buildingId">
          <el-select v-model="editForm2.buildingId" placeholder="请选择" size="small" @change="onChangeBuilding">
            <el-option
              v-for="item in allBuildingList"
              :key="item.buildingId"
              :label="item.buildingName"
              :value="item.buildingId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="宿舍" prop="dormId">
          <el-select v-model="editForm2.dormId" placeholder="请选择" size="small" filterable>
            <el-option
              v-for="item in dormList"
              :key="item.dormitoryId"
              :label="item.roomNo"
              :value="item.dormitoryId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input size="small" v-model="editForm2.title" auto-complete="off" placeholder="请简要描述需求"></el-input>
        </el-form-item>
        <el-form-item label="详细描述" prop="content">
          <el-input size="small" v-model="editForm2.content" type="textarea" placeholder="请详细描述您的需求"></el-input>
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
              <li v-if="editForm2.pics==null||editForm2.pics==''" class="el-icon-plus avatar-uploader-icon"></li>
              <li class="img-li" v-else>
                <img :src="editForm2.pics" class="avatar" />
                <i class="del-btn el-icon-delete-solid" @click="delPic()"></i>
              </li>
            </ul>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" class="orange-button" @click='closeDialog("edit")'>取消</el-button>
        <el-button size="small" class="green-button title" :loading="loading" @click="submitForm2('editForm2')">保存</el-button>
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
    <el-descriptions class="margin-top" :column="1" border v-if="currDetailData.dorm!=null">
      <el-descriptions-item label="报修时间" labelStyle="width: 160px;">{{currDetailData.createTime|timestampToTime}}</el-descriptions-item>
      <el-descriptions-item label="房间号">{{ currDetailData.dorm.roomNo }}</el-descriptions-item>
      <el-descriptions-item label="简述">{{ currDetailData.title }}</el-descriptions-item>
      <!-- <el-descriptions-item label="标签">
        <el-tag size="small">{{ currDetailData.tags }}</el-tag>
      </el-descriptions-item> -->
      <el-descriptions-item label="报修详情">{{ currDetailData.content }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <!--0-待处理；1-已完成；2-已取消-->
        <el-tag v-if="currDetailData.status == 0" type="warning">待处理</el-tag>
        <el-tag v-else-if="currDetailData.status == 1" type="success">已完成</el-tag>
        <el-tag v-else-if="currDetailData.status == 2" type="info">已取消</el-tag>
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
    <div class="demo-image" v-if="currDetailFinishPics.length>0">
      <p>维修照片：</p>
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
import Pagination from "../../components/Pagination.vue";
import { fileUploader } from '@/api/upload.js'
import { ManagerFixList, ManagerFixUpdate, SaveRoomService2,GetAllBuildingList, AdminGetDormByBuilding } from '@/api/api.js'

export default {
  data() {
    return {
      addFormVisible: false, 
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      roomDescVisible: false, //详情对话框显示与隐藏
      title: "",
      editForm: {
        fixId: "",
        fixResult: "",
        status: 1,
        confirmPic: '',
      },
      editForm2: {
        dormId: '',
        title: '',
        status: 0,
        pics: '',
        content: '',
      },
      photos: [],
      formInline: {
        buildingId: "",
        status: '',
      },
      listData: [],
      allBuildingList: [],
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10,
      },
      currDetailData: {
        confirmPic: null,
        createTime: null,
        finishTime: null,
        pics: null,
        status: 0,
        title: "",
        userId: "",
      },
      currDetailPics: [],
      currDetailFinishPics: [],
      rules: {
        dormId: [{ required: true, message: '请选择', trigger: 'blur' }],
        title: [{ required: true, message: '请输入', trigger: 'blur' }],
      },
      dormList: [],
      uploadPicLoading: false,
    };
  },
  // 注册组件
  components: {
    Pagination,
  },
  /**
   * 数据发生改变
   */

  /**
   * 创建完毕
   */
  created() {
    this.getdata(this.formInline)
    GetAllBuildingList({}).then((res) => {
      if (res.success) {
        this.allBuildingList = res.data
      }
    })
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取房间列表
    getdata(parameter) {
      this.loading = true;
      ManagerFixList(parameter)
        .then((res) => {
          this.loading = false;
          if (res.success) {
            this.listData = res.data.records;
            this.pageparm.currentPage = res.data.currentPage;
            this.pageparm.pageSize = res.data.pageSize;
            this.pageparm.total = res.data.totalrecord;
          } else {
            this.$message({
              type: "info",
              message: res.msg,
            });
          }
        })
        .catch((err) => {
          this.editFormVisible = false;
          this.loading = false;
          this.$message.error("获取数据失败，请稍后再试！");
        });
    },
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentPage = parm.currentPage;
      this.formInline.pageSize = parm.pageSize;
      this.formInline.total = parm.total
      this.getdata(this.formInline);
    },
    // 搜索事件
    search() {
      console.log('搜索：', this.formInline)
      this.getdata(this.formInline)
    },
    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
      this.currDetailData = row
      if (row != undefined && row != "undefined") {
        this.title = "反馈维修结果";
        this.editForm.fixId = row.fixId;
        this.editForm.status = 1;
        this.editForm.fixResult = ''
      }
    },

    //显示编辑界面
    handleNew: function (row) {
      this.addFormVisible = true
      this.title = '新增报修/清洁'
      this.editForm.dormId = '',
      this.editForm.serviceType = 1,
      this.editForm.title = '',
      this.editForm.pics = '',
      this.editForm.content = ''
    },

    onChangeBuilding(val){
      AdminGetDormByBuilding({buildingId: val}).then((res) => {
        if (res.success) {
          this.dormList = res.data
        }
      })
    },
    
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.editForm.confirmPic = this.photos.join(",")
      ManagerFixUpdate(this.editForm).then((res) => {
        this.editFormVisible = false;
        this.loading = false;
        if (res.success) {
          this.getdata(this.formInline);
          this.$message({
            type: "success",
            message: "反馈成功！",
          });
          this.photos=[]
        } else {
          this.$message({
            type: "info",
            message: res.msg,
          });
        }
      })
      .catch((err) => {
        this.editFormVisible = false;
        this.loading = false;
        this.$message.error("提交失败，请稍后再试！");
      });

    },

    //新增报修
    submitForm2(editData) {
      this.$refs[editData].validate(valid => {
        if (valid) {
          // 请求方法
          SaveRoomService2(this.editForm2)
            .then(res => {
              this.addFormVisible = false
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
              this.addFormVisible = false
              this.loading = false
              console.log(err)
              this.$message.error('保存失败，请稍后再试！')
            })
        } else {
          return false
        }
      })
    },

    // 改变状态
    offShelf(index, row, status) {
      let msg = "确定要下架该房间吗？"
      if(status == 1){
        msg = "确定要重新上架该房间吗？"
      }
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          ModifyRoomStatus({roomId: row.roomId, status: status})
            .then((res) => {
              if (res.success) {
                this.$message({
                  type: "success",
                  message: "操作成功!",
                });
                this.getdata(this.formInline);
              } else {
                this.$message({
                  type: "info",
                  message: res.msg,
                });
              }
            })
            .catch((err) => {
              this.loading = false;
              this.$message.error("操作失败，请稍后再试！");
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    //显示详情界面
    handleDetail: function (index, row) {
      this.currDetailFinishPics=[]
      this.roomDescVisible = true
      this.currDetailData = row
      this.currDetailPics = row.pics.split(",")
      console.log('显示详情', this.currDetailData)
      this.currDetailFinishPics = row.confirmPic.split(",")
    },
    //进入房间管理页面
    handleRoom: function(index, row){
      this.$router.push({path:'/front/book?toyId='+toyId})
    },
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
    },
    closeDialog2() {
      this.roomDescVisible = false;
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
          this.editForm2.pics = res.data.data[0]
        } else {
          this.$message.error(res.data.message);
        }
        this.uploadPicLoading=false
      })
    },
  },
};
</script>

<style scoped>
.user-search {
  margin-top: 20px;
}
.userRole {
  width: 100%;
}
.user-search .el-form--inline .el-form-item{
  vertical-align: baseline;
}
.user-search .el-form-item{
  margin-bottom: 10px;
}
.avatar-uploader /deep/ .el-upload {
    border-radius: 6px;
    background-color: #fff;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100%;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #41a8a8;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    border: 1px dashed #8c939d;
    width: 32%;
    height: 140px;
    line-height: 140px;
    text-align: center;
  }
  .showUploader /deep/ .el-upload-list{
    display: none;
  }
  .avatar {
    width: 100%;
    height: 140px;
    display: block;
  }
  .img-list{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }
  .img-list .img-li{
    display: inline-block;
    margin-right: 1%;
    width: 32%;
    margin-bottom: 5px;
  }
  .quill-editor {
    height: 250px;
    margin-bottom: 100px;
  }
  .introduction, .demo-image{
    margin: 15px auto;
  }
  .el-image{
    cursor: zoom-in;
  }
  .block{
    display: inline-block;
    width: 100px;
    margin: 10px;
  }
</style>

 
 