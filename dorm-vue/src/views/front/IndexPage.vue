<template>
  <div class="main-contain" style="width: 1024px;">
    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>水电费催缴通知</span>
      </div>
    </div>
    <el-table
      size="small"
      :data="msgList"
      border
      element-loading-text="拼命加载中"
      style="width: 100%">
      <el-table-column align="center" prop="realName" label="催缴时间">
        <template slot-scope="scope">
          <span>{{ scope.row.msgTime|timestampToDayHM }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="building.buildingName" label="标题">
        <template slot-scope="scope">
          <span class="msg-title" @click="toFee(scope.row.msgId)">{{ scope.row.title}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="realName" label="内容" show-overflow-tooltip min-width="280">
        <template slot-scope="scope">
          <span>{{ scope.row.content }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>宿管联系方式</span>
      </div>
    </div>
    <el-table
      size="small"
      :data="listData"
      border
      element-loading-text="拼命加载中"
      style="width: 100%">
      <el-table-column align="center" prop="building.buildingName" label="楼宇">
        <template slot-scope="scope">
          <span class="blue">{{ scope.row.building.position}} {{ scope.row.building.buildingName}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="realName" label="宿管">
        <template slot-scope="scope">
          <span class="f14">{{ scope.row.realName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="mobile" label="手机号">
        <template slot-scope="scope">
          <span class="f14">{{ scope.row.mobile }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>通知公告</span>
      </div>
    </div>
    <ul class="info-list">
      <li class="item2" v-for="(item,id) in noticeList" :key="id">
        <div class="content" @click="toDetailPage(item.noticeId)">
          <p class="title">{{ item.title }}</p>
          <div class="tags">
            <el-tag type="primary" size="small">{{ item.tag }}</el-tag>
            <span class="ptime">{{item.publishTime|timestampToDayHM}}</span>
          </div>
          <div class="pcontent">{{ item.content }}</div>
        </div>
        <div class="clear"></div>
      </li>
    </ul>
    <!-- 分页组件 -->
    <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
  </div>
</template>

<script>
import { FrontNoticeList, FrontIndexInfo,FrontReadMsg } from '@/api/api.js'
import Pagination from '../../components/Pagination'
export default {
  data() {
    return {
      kw: '',
      swipeImages: [],
      noticeList: [],
      listData: [],
      msgList: [],
      pageparm: {
        currentpage: 1,
        pageSize: 5,
        total: 0
      },
      formInline: {
        kw: '',
        type: '',
        status: 1,
        currentpage: 1,
        pageSize: 5,
      },
      loading: false
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
    this.getdata()
  },

  mounted() {
   
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取首页信息数据
    getdata(parameter) {
      console.log('开始获取首页资源列表！')
      this.loading = true
      
      FrontNoticeList(parameter).then(res => {
        if (res.success) {
          this.noticeList = res.data.records
          this.pageparm.currentpage = res.data.currentPage
          this.pageparm.pageSize = 5
          this.pageparm.total = res.data.totalrecord;
        } else {}
      })

      FrontIndexInfo({}).then(res => {
        this.loading = false
        if (res.success) {
          this.listData = res.data.contact
          this.msgList = res.data.msg
        } else {}
      })
    },

    //详情
    toDetailPage(sid) {
      this.$router.push({ path: '/notice/detail?cid='+sid })
    },

    toFee(msgId){
      // FrontReadMsg({msgId: msgId}).then(res => {
      //   this.loading = false
      //   if (res.success) {
      //     this.$router.push({ path: '/fee' })
      //   } else {}
      // })
      this.$router.push({ path: '/fee' })
    },

    // 分页插件事件
    callFather(parm) {
      this.formInline.currentpage = parm.currentPage
      this.formInline.pageSize = 5
      this.getdata(this.formInline)
    },
  }
}
</script>

<style scoped>
#mapContainer{
  width: 1024px;
  height: 380px;
}
.info-list .item2{
  clear: both;
  margin: 18px auto;
  width: 840px;
  border: 1px solid #ccc;
  padding: 10px 5px;
  box-shadow: 2px 2px 8px #ccc;
}
.info-list .item2 img{
  width: 160px;
  height: 120px;
  float: left;
}
.info-list .item2 .content{
  display: inline-block;
  padding: 0 15px;
}
.item2 .content .title{
  margin: 0 0 8px 0;
  font-weight: lighter;
  font-size: 20px;
}
.info-list .item2 .content:hover .title{
  color: #1ea5a3;
}
.info-list .item2 .content:hover{
  cursor: pointer;
}
.info-list .item2 img:hover{
  cursor: pointer;
}
.content .desc{
  margin: 8px 0;
  font-size: 14px;
  color: #666;
  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
.clear{
  clear: both;
}
.ptime{
  color: #666;
  font-size: 14px;
  padding: 5px 10px;
}
.area-list .aitem{
  padding: 4px 12px;
  border: 1px solid #262e34;
  color: #262e34;
  border-radius: 3px;
  display: inline-block;
  margin: 0 6px;
  font-size: 14px;
  cursor: pointer;
}
.area-list li.active{
  background-color: #262e34;
  color: #fff;
}
.area-list .meta{
  color: #666;
  padding: 2px 8px;
  display: inline-block;
}
.area-list{
  margin: 12px 0;
  font-size: 14px;
  float: left;
}
.info-list .pcontent{
  overflow: hidden; /* 隐藏超出容器范围的内容 */
  text-overflow: ellipsis; /* 当文字溢出时显示省略号 */
  display: -webkit-box; /* 必须指定display为-webkit-box才能生效 */
  -webkit-line-clamp: 2; /* 限制最大显示行数为3行 */
  -webkit-box-orient: vertical; /* 垂直布局 */
  font-size: 14px;
  height: 42px;
  line-height: 22px;
  padding-top: 8px;
  color: #666;
}
.blue{
  color: #409EFF;
  font-size: 14px;
}
.msg-title{
  color: #409EFF;
  cursor: pointer;
}
.f14{
  font-size: 14px;
}
a{
  text-decoration: none;
}
</style>