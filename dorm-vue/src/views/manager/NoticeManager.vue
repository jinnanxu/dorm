<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '#' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>通知公告管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="搜索：">
        <el-input
          size="small"
          v-model="formInline.kw"
          placeholder="输入关键字"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="search"
          >搜索</el-button
        >
        <el-button
            size="small"
            type="success"
            icon="el-icon-plus"
            @click="handleEdit()"
        >发布</el-button>
      </el-form-item>
    </el-form>
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
      <el-table-column align="center" prop="publishDate" label="发布时间">
        <template slot-scope="scope">
          {{ scope.row.publishTime|timestampToDayHM }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="title" label="标题" show-overflow-tooltip>
      </el-table-column>
      <el-table-column align="center" prop="tag" label="关键词">
        <template slot-scope="scope">
          <el-tag type="warning" size="small" v-for="(itemChild,indexChild) in scope.row.tag.split(';')" :key="indexChild">{{ itemChild }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="350">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="handleDetail(scope.row)" icon="el-icon-document">详情</el-button>
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            >修改</el-button
          >
          <el-button size="mini" type="danger" @click="offShelf(scope.row, -1)" icon="el-icon-delete">删除</el-button>
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
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog"
    >
      <el-form
        label-width="120px"
        :model="editForm"
        :rules="rules"
        ref="editForm"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            size="small"
            v-model="editForm.title"
            auto-complete="off"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="关键字" prop="tag">
          <el-input
            size="small"
            v-model="editForm.tag"
            auto-complete="off"
            placeholder="如：停水通知;放假通知"
          ></el-input>
        </el-form-item>
        <el-form-item label="内容正文" prop="content" id='quillEditorQiniu'>
          <quill-editor class="editor" v-model="editForm.content" ref="customQuillEditor" :options="editorOption" >
          </quill-editor>
          <!-- 基于elementUi的上传组件 el-upload-->
          <el-upload
              class="avatar-uploader"
              :action="uploadImgUrl"
              :accept="'image/*'"
              :show-file-list="false"
              :on-success="uploadEditorSuccess"
              :on-error="uploadEditorError"
              :before-upload="beforeEditorUpload">
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button
          size="small"
          type="primary"
          class="title"
          @click="submitForm('editForm')"
          >保存</el-button
        >
      </div>
    </el-dialog>
    <!--详情对话框-->
    <el-dialog
      title="通知公告详情"
      :visible.sync="detailVisible"
      width="60%"
      :close-on-click-modal=false
      @click="closeDialog2"
    >
    <!-- <el-descriptions class="margin-top" :column="1" border>
        <el-descriptions-item label="状态" labelStyle="width: 160px;">
          <span v-if="currDetailData.status == 1" style="color:#67C23A;">正常</span>
          <span v-else-if="currDetailData.status == -1" style="color:#F56C6C;">已下架</span>
        </el-descriptions-item>
    </el-descriptions> -->
    <div>
      <h3>{{ currDetailData.title }}</h3>
      <p>{{ currDetailData.publishTime|timestampToDayHM }}</p>
    </div>
    <div v-html="currDetailData.content" class="introduction"></div>
    </el-dialog>
  </div>
</template>

<script>
import { ManagerNoticeUpdate,ManagerNoticeList,ManagerNoticeDelete } from '@/api/api.js'
import Pagination from "../../components/Pagination.vue";
import { fileUploader } from '@/api/upload.js'
import * as Quill from 'quill'

const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'],        // toggled buttons

  [{'list': 'ordered'}, {'list': 'bullet'}],
  [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
  [{'direction': 'rtl'}],                         // text direction

  [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
  [{'header': [1, 2, 3, 4, 5, 6, false]}],

  [{'color': []}, {'background': []}],          // dropdown with defaults from theme
  [{'font': []}],
  [{'align': []}],
  ['link', 'image'],
  ['clean']                                         // remove formatting button
];

export default {
  data() {
    return {
      currExpert: null,
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      title: "编辑知识库条目信息",
      editForm: {
        noticeId: '',
        title: "",
        tag: "",
        content: '',
      },
      formInline: {
        page: 1,
        limit: 10,
        kw: "",
        currentpage: 1,
        pageSize: 10,
      },
      files: {},
      // rules表单验证
      rules: {
        title: [{ required: true, message: "请输入", trigger: "blur" }],
        openTime: [{ required: true, message: "请输入", trigger: "blur" }],
      },
      listData: [],
      currDetailData: {},
      currDetailPics: [],
      uploadImgUrl:"/dorm-server/upload2",
      uploadUrlPath:"没有文件上传",
      quillUpdateImg:false,
      editorOption:{
        placeholder:'请输入正文',
        modules: {
          toolbar: {
            container: toolbarOptions,  // 工具栏
            handlers: {
              'image': function (value) {
                if (value) {
                  document.querySelector('#quillEditorQiniu .avatar-uploader input').click()
                } else {
                  this.quill.format('image', false);
                }
              },
            }
          }
        }
      },
      // 分页参数
      pageparm: {
        currentpage: 1,
        pageSize: 10,
        total: 10,
      },
    };
  },
  // 注册组件
  components: {
    Pagination
  },

  /**
   * 创建完毕
   */
  created() {
    this.getdata(this.formInline)
    this.currExpert = JSON.parse(localStorage.getItem('AdminInfo'))
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取列表信息
    getdata(parameter) {
      this.loading = true
      ManagerNoticeList(parameter)
        .then((res) => {
          this.loading = false;
          if (res.success) {
            this.listData = res.data.records;
            this.pageparm.currentpage = res.data.currentpage;
            this.pageparm.pageSize = res.data.pageSize;
            this.pageparm.total = res.data.totalrecord;
          } else {
            this.$message({
              type: "info",
              message: res.msg,
            });
          }
        })
    },
    
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage;
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
    handleEdit: function (row) {
      console.log('显示编辑界面',row)
      this.editFormVisible = true;
      if (row!=null) {
        console.log('编辑……')
        this.editForm.noticeId = row.noticeId;
        this.editForm.title = row.title;
        this.editForm.tag = row.tag
        this.editForm.content = row.content
      } else {
        console.log('新增……')
        this.editForm.noticeId = null
        this.editForm.title = ""
        this.editForm.tag = ""
        this.editForm.content = ""
      }
    },
    // 编辑、增加页面保存方法
    submitForm(editData) {
      this.$refs[editData].validate((valid) => {
        if (!valid) {
          return false;
        }
        // 调用保存接口
        ManagerNoticeUpdate(this.editForm)
          .then((res) => {
            this.editFormVisible = false;
            this.loading = false;
            if (res.success) {
              this.getdata(this.formInline);
              this.$message({
                type: "success",
                message: "保存成功！",
              });
            } else {
              this.$message({
                type: "info",
                message: res.msg,
              });
            }
            this.getdata(this.formInline)
          })
      });
    },
    
    // 关闭编辑、增加弹出框
    closeDialog() {
      this.editFormVisible = false;
    },
    closeDialog2() {
      this.detailVisible = false;
    },
    //编辑新增
    handleDetail(row) {
      this.detailVisible = true;
      this.currDetailData = row
    },
    //删除
    offShelf(row){
      let msg = "确定要删除《"+row.title+"》吗？"
      this.$confirm(msg, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        ManagerNoticeDelete({kid: row.noticeId})
          .then((res) => {
            if(res.success){
              this.$message({
                type: "success",
                message: "操作成功!",
              });
              this.getdata(this.formInline)
            }else{
              this.$message({
                type: "info",
                message: res.msg,
              });
            }
          })
      })
    },

    // 富文本编辑器：上传图片成功
    uploadEditorSuccess(res, file) {
      console.log("上传成功")
      // this.$emit('upload',res, file)
      console.log(res, file);
      //拼接出上传的图片在服务器的完整地址
      let imgUrl=res.data[0];
      let type=imgUrl.substring(imgUrl.lastIndexOf(".")+1);
      console.log(type);
      // 获取富文本组件实例
      let quill = this.$refs.customQuillEditor.quill;
      // 获取光标所在位置
      let length = quill.getSelection().index;
      // 插入图片 res.info为服务器返回的图片地址
      quill.insertEmbed(length, 'image', imgUrl)
      // 调整光标到最后
      quill.setSelection(length + 1)
      //取消上传动画
      this.quillUpdateImg = false;
    },
    // 上传(文件)图片失败
    uploadEditorError(res, file) {
      console.log(res);
      console.log(file);
      //页面提示
      this.$message.error('上传图片失败')
      //取消上传动画
      this.quillUpdateImg = false;
    },
    //上传图片之前async
    beforeEditorUpload(res, file){
      //显示上传动画
      this.quillUpdateImg = true;
      console.log(res);
    },
    //上传组件返回的结果
    uploadResult:function (res){
      this.uploadUrlPath=res;
    },
    // 返回
    goBack() {
      this.$router.back()
    },
  },
};
</script>

<style>

.margin-top{
  width: 80%;
  margin: 0 auto;
}
.userRole {
  width: 100%;
}
.comm-item{
    width: 30%;
}

.show-btn{
  text-align: center;
  margin: 10px 0 30px 0;
}

.toy-pic{
  margin: 10px auto;
  width: 150px;
}
.el-tag{
  margin-right: 4px;
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
    border-color: #5d7db3;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    border: 1px dashed #8c939d;
    width: 220px;
    height: 160px;
    line-height: 160px;
    text-align: center;
  }
  .showUploader /deep/ .el-upload-list{
    display: none;
  }
  .avatar {
    width: 220px;
    height: 160px;
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
    margin-bottom: 5px;
  }
  .demo-image{
    margin-top: 15px;
  }
  .quill-editor {
    height: 250px;
    margin-bottom: 100px;
  }
  .el-upload{
    width: 100%;
  }
  .img-list .img-li{
    position: relative;
  }
  .logo-remove{
    font-size: 22px;
    position: absolute;
    top: 10px;
    right: 10px;
    color: #fff;
    text-shadow: gray 4px 4px 5px;
  }
  .logo-remove:HOVER{
    color: #F56C6C;
  }
  .info-pic{
    width: 80%;
    margin: 35px auto;
  }
  .cspan{
    color: #5d7db3;
  }
  .tspan{
    color: #40a9a8;
    font-weight: bold;
  }
</style>