/**
* 图表界面
*/ 
<template>
  <!-- 组件主盒子 -->
  <div class="stbox">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>数据可视化</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索筛选 -->
    <el-form :inline="true" :model="formInline" class="user-search">
      <el-form-item label="楼宇" prop="buildingId">
        <el-select v-model="formInline.buildingId" placeholder="请选择" clearable size="small" style="width: 160px;">
          <el-option
            v-for="item in allBuildingList"
            :key="item.buildingId"
            :label="item.buildingName"
            :value="item.buildingId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="year">
        <el-date-picker
          size="small"
          v-model="formInline.dateRange"
          value-format="yyyy-MM"
          type="monthrange"
          range-separator="至"
          start-placeholder="开始月份"
          end-placeholder="结束月份">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-search"
          @click="initChart"
        >统计</el-button>
      </el-form-item>
    </el-form>
    <!--饼图-->
    <el-row :gutter="24">
      <el-col :span="12" class="text-c">
        <div class="st-gbox">
          <div id="pieChart" style="height: 450px"></div>
        </div>
      </el-col>
      <el-col :span="12" class="text-c">
        <div class="st-gbox">
          <div id="pieChart2" style="height: 450px"></div>
        </div>
      </el-col>
    </el-row>
    <div style="color:#fff;">.</div>
  </div>
</template>
<script type="text/ecmascript-6">
import { Statistics, GetAllBuildingList } from '@/api/api.js'
import { timestampToDate } from '@/utils/util.js'

import * as echarts from 'echarts';
export default {
  name: "welcome",
  data() {
    return {
      type: 'day',
      chartData: {},
      dateRange: [],
      allBuildingList: [],
      formInline: {
        currentPage: 1,
        pageSize: 10,
        buildingId: "",
        dormId: "",
        dateRange: ''
      },
    }
  },
  // 导入组件
  components: {
    // 点聚合组件
  },
  // 创建完毕状态(里面是操作)
  created() {
    GetAllBuildingList({}).then((res) => {
      if (res.success) {
        this.allBuildingList = res.data
      }
    })
  },
  // 挂载结束状态(里面是操作)
  mounted() {
    //this.initChart()
  },
  // 里面的函数只有调用才会执行
  methods: {
    initChart(){
      console.log(this.formInline)
      let req = {
        buildingId: this.formInline.buildingId,
        dateRangeStr: this.formInline.dateRange.join(',')
      }
      console.log(req)
      //初始化图表
      this.loading = true
      //向服务端请求统计数据报文
      Statistics(req).then(res => {
        this.loading = false
        if (res.success) {
          console.log(res.data)
          this.chartData = res.data
          this.showPieChart()
          this.showLine()
        } else {
          this.$message({
            type: 'info',
            message: res.msg
          })
        }
      }).catch(err => {
        console.log(err)
        this.loading = false
        this.$message.error('加载失败，请稍后再试！')
      })
    },
    
    //饼图
    showPieChart(){
      let pie1 = {
        backgroundColor: '#fff',
        title: {text: '迁入迁出比'},
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        series: [
          {
            name: '迁入迁出统计',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: this.chartData.occ,
            //roseType: 'radius',
            label:{normal:{textStyle:{color:'#333'}}},animationType:'scale',animationEasing:'elasticOut',animationDelay:function(idx){return Math.random()*200;}
          }
        ]
      }
      var myChart = echarts.init(document.getElementById('pieChart'));
      myChart.setOption(pie1)
    },

    //柱状图
    showLine(){
      let option = {
        title: {text: '水电使用量'},
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          show: true,
          data: ['用电量', '用水量']
        },
        xAxis: {
          type: 'category',
          data: this.chartData.fee.dayXAxis
        },
        yAxis: [{
            type: 'value',
            name: '用电量',
            position: 'left',
            alignTicks: true,
            axisLabel: {
              formatter: '{value} 度'
            }
          },
          {
            type: 'value',
            name: '用水量',
            position: 'right',
            alignTicks: true,
            axisLabel: {
              formatter: '{value} 吨'
            }
        }],
        series: [
          {
            name: "用电量",
            data: this.chartData.fee.dayYAxis[0],
            yAxisIndex: 0,
            type: 'bar'
          },{
            name: "用水量",
            data: this.chartData.fee.dayYAxis[1],
            yAxisIndex: 1,
            type: 'bar'
          }
        ],
      }
      var myChart2 = echarts.init(document.getElementById('pieChart2'));
      myChart2.setOption(option)
    }
  }
};
</script>
<style>
.stbox {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.stbgc {
  background-color: #fff;
  height: 60px;
  line-height: 60px;
  border-radius: 5px;
  padding: 0px 16px;
}
.stsearch {
  text-align: center;
}
.text-c {
  text-align: center;
}
.st-gbox {
  background-color: #fff;
  margin-top: 20px;
  border-radius: 5px;
  box-sizing: border-box;
  padding: 10px;
  border: 1px solid #ccc;
}
.cavasbox {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
}
.paybox {
  width: 100%;
  background-color: #fff;
  box-sizing: border-box;
  border-radius: 5px;
  margin-top: 20px;
}
</style>