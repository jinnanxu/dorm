/**
* 图表界面
*/ 
<template>
  <!-- 组件主盒子 -->
  <div class="stbox">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>数据可视化</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索，切换 -->
    <!-- <el-row :gutter="23">
      <el-col :span="18">
        <el-form :inline="true" class="user-search">
          <el-form-item label="统计日期：">
            <el-date-picker v-model="dateRange" type="daterange" :picker-options="pickerOptions"
                range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="query()">统计</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row> -->
    <!-- 统计图 -->
    <el-row :gutter="24">
      <el-col :span="12" class="text-c">
        <div class="st-gbox">
          <div class="cavasbox" ref="StorageEchart"></div>
        </div>
      </el-col>
    </el-row>
    <div>.</div>
  </div>
</template>
<script type="text/ecmascript-6">
import { StatisticsPie } from '@/api/api.js'
import  * as  Chart from 'echarts'
export default {
  name: "welcome",
  data() {
    return {
      machineNo: '',
      type: 'day',
      quxiandata: [],
      //  饼图
      pieOption: {
        backgroundColor: '#fff',
        
        title: {text: '景点订单统计'},
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },

        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        series: [
          {
            name: '景点订单统计',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [],
            //roseType: 'radius',
            label: {
              normal: {
                textStyle: {
                  color: '#333'
                }
              }
            },
            
            itemStyle: {
              normal: {
                
              }
            },
            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
              return Math.random() * 200;
            }
          }
        ]
      },
      pickerMinDate: '',
      dateRange: [],
      pickerOptions: {
        onPick: ({ maxDate, minDate }) => {
          this.pickerMinDate = minDate.getTime()
          if (maxDate) {
            this.pickerMinDate = ''
          }
        },
        disabledDate: (time) => {
          if (this.pickerMinDate !== '') {
            const day7 = (31 - 1) * 24 * 3600 * 1000
            let maxTime = this.pickerMinDate + day7
            if (maxTime > new Date()) {
              maxTime = new Date()
            }
            return time.getTime() > maxTime
          }
          return time.getTime() > Date.now()
        }
      },
    }
  },
  // 导入组件
  components: {
    // 点聚合组件
  },
  // 创建完毕状态(里面是操作)
  created() { },
  // 挂载结束状态(里面是操作)
  mounted() {
    this.initPieChart()
  },
  // 里面的函数只有调用才会执行
  methods: {
    // 饼图
    getCurrStorage() {
      this.chart = Chart.init(this.$refs.StorageEchart)
      this.pieOption.series[0].data=this.pieData
      console.log('饼图的数据：', this.pieOption, 'data中的数据：', this.pieData)
      this.chart.setOption(this.pieOption)
    },
    
    //读取饼图数据
    initPieChart(){
      this.loading = true
      StatisticsPie({}).then(res => {
        console.log('向服务端读取饼图数据：', res)
        this.loading = false
        if (res.success) {
          this.pieData = res.data
          console.log('读取饼图数据', this.pieData)
          this.getCurrStorage()
        } else {
          // this.$message({
          //   type: 'info',
          //   message: res.msg
          // })
        }
      }).catch(err => {
        console.error('向服务端读取饼图数据发生异常：',err)
        this.editFormVisible = false
        this.loading = false
        this.$message.error('加载失败，请稍后再试！')
      })

    },
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
  height: 50vh;
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
  height: 50vh;
}
</style>