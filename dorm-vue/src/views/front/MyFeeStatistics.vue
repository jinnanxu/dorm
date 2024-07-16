<template>
  <div class="main-contain">
    <div class="top-nav">
      <div class="page-title" style="float: left;">
        <span>最近半年水电费统计</span>
      </div>
    </div>
    <el-row :gutter="24">
      <el-col :span="22" class="text-c">
        <div class="st-gbox">
          <div id="pieChart" style="height: 450px"></div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="22" class="text-c">
        <div class="st-gbox">
          <div id="pieChart2" style="height: 450px"></div>
        </div>
      </el-col>
    </el-row>
    <div style="color:#fff;">.</div>
  </div>
</template>
<script type="text/ecmascript-6">
import { FrontFeeStatistics } from '@/api/api.js'
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
    
  },
  // 挂载结束状态(里面是操作)
  mounted() {
    this.initChart()
  },
  // 里面的函数只有调用才会执行
  methods: {
    initChart(){
      console.log(this.formInline)
      //初始化图表
      this.loading = true
      //向服务端请求统计数据报文
      FrontFeeStatistics(this.formInline).then(res => {
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
    
    //水电用量
    showPieChart(){
      let pie1 = {
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
            data: this.chartData.usage.dayYAxis[0],
            yAxisIndex: 0,
            type: 'bar'
          },{
            name: "用水量",
            data: this.chartData.usage.dayYAxis[1],
            yAxisIndex: 1,
            type: 'bar'
          }
        ],
      }
      var myChart = echarts.init(document.getElementById('pieChart'));
      myChart.setOption(pie1)
    },

    //柱状图
    showLine(){
      let option = {
        title: {text: '水电费统计'},
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          show: true,
          data: ['电费', '水费']
        },
        xAxis: {
          type: 'category',
          data: this.chartData.fee.dayXAxis
        },
        yAxis: {
            type: 'value',
            name: '费用金额',
            position: 'right',
            alignTicks: true,
            axisLabel: {
              formatter: '{value} 元'
            }
          },
        series: [
          {
            name: "电费",
            data: this.chartData.fee.dayYAxis[0],
            type: 'bar'
          },{
            name: "水费",
            data: this.chartData.fee.dayYAxis[1],
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
<style scoped>
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