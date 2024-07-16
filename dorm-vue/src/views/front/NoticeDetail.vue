<template>
  <div class="main-contain">
    <!-- <p class="page-title">{{ currDetailData.title }}</p> -->
    <div class="show-title">{{notice.title}}</div>
    <div class="auth">
      <p class="ptime">
        <el-tag type="danger" size="mini">{{ notice.tag }}</el-tag>
        {{notice.publishTime|timestampToDayHM}}
      </p>
    </div>
    <div v-html="notice.content" class="introduction"></div>
    <div class="show-btn" style="margin-top: 35px;" v-if="loginUser!=null">
      <el-button size="small" type="info" icon="el-icon-refresh-left" @click="goBack()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { GetNoticeDetail } from '@/api/api.js'

export default {
  data() {
    return {
      loading: false,
      notice: {},
      editFormVisible: false,
      editFormVisible2: false,
      currDetailData: null,
      currUser: {},
      uploadImgUrl:"/dorm-server/upload2",
      uploadUrlPath:"没有文件上传",
      quillUpdateImg:false,
      editForm: {
        noticeId: '',
        answerType: 2,
        content: '',
      },
      editForm2:{
        noticeId: '',
        rate: 0,
      },
      rules: {
        content: [{ required: true, message: "请输入内容", trigger: "blur" }],
      },
    }
  },
 
  created() {
    this.getdata()
  },

  computed:{
    loginUser(){
      console.log('当前登录???：',this.$store.state.loginUser)
      return this.$store.state.currUser
    }
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    getdata() {
      console.log('拉取数据！', this.$route.query)
      const nid = this.$route.query.cid
      let parameter = {infoId: nid}
      GetNoticeDetail(parameter)
        .then(res => {
          this.loading = false
          if (res.success) {
            this.notice = res.data
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
          this.$message.error('详情查询失败，请稍后再试！')
        })
    },
    
    // 分页插件事件
    callFather(parm) {
      this.formInline.page = parm.currentPage
      this.formInline.limit = parm.pageSize
      this.getdata(this.formInline)
    },
    
    // 返回
    goBack(index, row) {
      this.$router.go(-1)
    },

  }
}
</script>

<style scoped >
.detail-pics{
  margin: 12px auto;
  width: 750px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
}
.detail-pics .el-image {
  display: inline-block;
  margin-right: 1%;
  width: 23% !important;
  height: auto !important;
  margin-bottom: 5px;
}
.show-title{
  margin: 15px 0;
  font-size: 22px;
  color: #5d7db3;
  text-align: center;
}
.show-btn{
  text-align: center;
  margin: 10px 0 30px 0;
}
.el-tag{
  margin-right: 4px;
}
.introduction{
  padding: 10px;
  min-height: 60px;
  color: #666;
  font-size: 14px;
  overflow: auto;
  width: 860px;
  margin: 0 auto;
  box-sizing: border-box;
}
.main-contain{
  padding-bottom: 20px;
}
.introduction img{
  width: 680px;
  height: auto;
  padding-left: 100px;
}
.auth{
  text-align: center;
  color: #666;
}
.ptime{
  font-size: 12px;
  margin: 8px 0;
}
.pic-title{
  font-size: 16px;
  width: 820px;
  margin: 10px auto;
  color: #888;
  padding: 15px 20px;
  border-top: 1px solid #ccc;
}
.student{
  font-size: 14px;
  margin: 0;
}
.cspan, .ktitle{
  color: #5d7db3;
}
.answers .info-item{
  padding-left: 10px;
}
.info-item{
  margin: 6px 0;
  padding-left: 100px;
}
.info-item .meta{
  color: #666;
  font-size: 14px;
}
.info-item .value{
  color: #5d7db3;
}
.answers{
  border: #b6cad7 1px solid;
  width: 860px;
  margin: 10px auto;
  position: relative;
}
.answers .floor{
  position: absolute;
  right: 10px;
  top: 10px;
  color: rgba(93, 125, 179, .4);
  font-size: 26px;
}
.answers .teacher{
  color: #5d7db3;
  font-weight: bold;
}
.answers .ptime{
  color: #888;
  font-weight: normal;
  padding-left: 8px;
}
.quill-editor {
  height: 250px;
  margin-bottom: 100px;
}
.el-icon-warning{
  color: #d77f57;
  font-size: 16px;
}
.qtip{
  font-size: 14px;
  color: #d77f57;
}
.antitle{
  width: 860px;
  margin: 15px auto 0 auto;
  border-top: #5d7db3 dashed 1px;
  font-size: 14px;
  color: #888;
  padding-top: 15px;;
}
.author{
  color: #888;
  font-size: 14px;
}
.tspan{
  color: #40a9a8;
}
</style>