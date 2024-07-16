<template>
  <div class="main-contain" style="width: 820px; padding-top: 15px;">
    <ul class="info-list" >
      <li class="item2" v-for="(item,id) in listData" :key="id">
        <div class="content" @click="toDetailPage(item)">
          <p class="title">
            <span class="ptime">{{item.publishTime|timestampToDayHM}}</span>
            <span class="tcontent">{{ item.title }}</span>
          </p>
          <div class="mtags">
            <span class="mtags">
              <el-tag type="warning" size="small">{{ item.tags }}</el-tag>
            </span>
          </div>
          <p class="ap">
            <span class="author">发布人：</span>
            <span v-if="item.teacher!=null">
              <span class="cspan">{{ item.teacher.college.collegeName }}</span>
              <span class="tspan">{{ item.teacher.realName }}</span>
            </span>
            <span v-else><span class="cspan">管理员</span></span>
          </p>
          <!-- <div class="pcontent">{{ item.content }}</div> -->
        </div>
        <div class="clear"></div>
      </li>
    </ul>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
  </div>
</template>

<script>
import { GetNoticeList } from '@/api/api.js'
import Pagination from '../../components/Pagination'
import { Loading } from 'element-ui'

export default {
  data() {
    return {
      loading: false, //是显示加载
      editFormVisible: false, //控制编辑页面显示与隐藏
      detailVisible: false, //详情对话框显示与隐藏
      query: {},
      listData: [], //列表数据
      // 分页参数
      pageparm: {
        currentPage: 1,
        pageSize: 10,
        total: 10
      },
      currDetailData: null
    }
  },
  // 注册组件
  components: {
    Pagination
  },
 
  created() {
    this.getdata(this.formInline)
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
     // 获取数据列表
     getdata(parameter) {
      console.log('开始获取列表！', parameter)
      let loadingInstance = Loading.service({target: '.info-list', text: '正在加载...'})
      this.loading = true
      GetNoticeList(parameter)
        .then(res => {
          this.loading = false
          loadingInstance.close()
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
          this.editFormVisible = false
          this.loading = false
          this.$message.error('系统繁忙，请稍后再试！')
        })
    },
    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage
      this.formInline.limit = parm.pageSize
      this.getdata(this.formInline)
    },

    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
    },

    //显示详情页面
    toDetailPage(row) {
      // this.detailVisible = true;
      // this.currDetailData=row
      this.$router.push({ path: '/notice/detail?nid='+row.noticeId })
    },

    closeDialog2(){
      this.detailVisible = false;
    },

    onSearch(){
      this.getdata(this.formInline)
    },

  }
}
</script>

<style scoped>
.search-div{
  margin: 12px 0;
}
.search-div .search-ipt{
  height: 35px;
  line-height: 35px;
  width: 220px;
  border-radius: 5px;
  border: 1px solid #808191;
  padding: 0 12px;
}
.cspan, .ktitle{
  color: #5d7db3;
}
.tspan{
  color: #40a9a8;
}
.el-tag{
  margin-right: 8px;
}
.ptime{
  color: #888;
  font-size: 14px;
  float: right;
}
.title .tcontent{
  font-size: 18px;
  color: #555;
}
.author{
  color: #888;
  font-size: 14px;
}
.item2 .title{
  margin: 5px 0;
}
.item2 .title .tcontent:hover{
  color: #5d7db3;
  cursor: pointer;
}
.info-list .item2{
  margin: 5px 0;
  padding: 5px 0 20px 0;
  border-bottom: 1px solid #cfcfcf;
}
.mtags{
  margin-bottom: 5px;
}
.item2 .content .ap{
  font-size: 14px;
  margin: 0;
}
</style>