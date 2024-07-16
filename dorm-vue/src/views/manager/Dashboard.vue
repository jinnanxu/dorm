<template>
  <div style="margin-top: 10px; max-width: 1280px;">
    <el-row :gutter="24">
      <el-col :span="24">
        <div class="building-info">{{ userData.building.buildingName }} 数据看板</div>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="8">
        <div class="ditem">
          <router-link :to="{path: '/manager/fix'}">
            <el-statistic title="待处理报修">
              <template slot="formatter">
                <i class="el-icon-setting" style="color: #67c23a; font-size: 32px;"></i>
                <span class="number">{{ dashboard.waitFix }}</span>
                <span class="unit">条</span>
              </template>
            </el-statistic>
          </router-link>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="ditem">
          <router-link :to="{path: '/manager/fee'}">
            <el-statistic title="待收水电费">
              <template slot="formatter">
                <i class="el-icon-money" style="color: #409eff; font-size: 32px;"></i>
                <span class="number">{{ dashboard.waitFee }}</span>
                <span class="unit">项</span>
              </template>
            </el-statistic>
          </router-link>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="ditem">
          <router-link :to="{path: '/manager/occ'}">
            <el-statistic title="总寝室/有空床铺寝室">
              <template slot="formatter">
                <i class="el-icon-office-building" style="color: #E6A23C; font-size: 32px;"></i>
                <span class="number" style="color: #409eff;">{{ dashboard.totalDorm }}</span><span>/</span><span class="number" style="color: #67c23a;">{{ dashboard.avDorm }}</span>
                <span class="unit">间</span>
              </template>
            </el-statistic>
          </router-link>
        </div>
      </el-col>
    </el-row>

    
    <el-row :gutter="24">
      <el-col :span="24">
        <div class="building-info" style="margin-top: 20px;">各宿舍数入住人数</div>
      </el-col>
    </el-row>
    <div class="rooms">
      <div class="rdata" v-for="build in dashboard.buildingOccData" :key="build">
        <div class="room-name">{{build.building}}</div>
        <div class="room-num">
          <span class="big">{{ build.total }}</span>
          <span>人</span>
        </div>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="18">
        <div class="building-info" style="margin-top: 20px;"></div>
      </el-col>
    </el-row>
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
  </div>
</template>

<script>

import { BashboardData } from '@/api/api.js'
import * as echarts from 'echarts';
export default {
  data() {
    return {
      dashboard: {},
      userData: {}
    };
  },
  created() {
    this.getdata({})
    this.loadAdmin()
  },

  /**
   * 里面的方法只有被调用才会执行
   */
  methods: {
    // 获取控制台数据
    getdata(parameter) {
      this.loading = true;
      BashboardData(parameter)
        .then((res) => {
          this.loading = false;
          console.log(res)
          this.dashboard=res.data
          this.showLine()
          this.showPieChart()
        })
    },
    loadAdmin(){
      let userData = JSON.parse(localStorage.getItem('AdminInfo'))
      if(this.userRole==1){//管理员
        this.userRoleStr = '系统管理员'
      }else if(this.userRole==2){//宿舍管理员
        this.userRoleStr = '宿舍管理员'
      }
      this.userData = userData
    },

    //柱状图
    showLine(){
      let option = {
        title: {text: '各院系新生人数'},
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        xAxis: {
          type: 'category',
          data: this.dashboard.newStudentKey
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.dashboard.newStudentData,
            type: 'bar'
          }
        ]
      }
      var myChart2 = echarts.init(document.getElementById('pieChart2'));
      myChart2.setOption(option)
    },
    //饼图
    showPieChart(){
      let pie1 = {
        backgroundColor: '#fff',
        title: {text: '离校人数及原因'},
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        series: [
          {
            name: '离校人数及原因',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: this.dashboard.leaveData,
            //roseType: 'radius',
            label:{normal:{textStyle:{color:'#333'}}},animationType:'scale',animationEasing:'elasticOut',animationDelay:function(idx){return Math.random()*200;}
          }
        ]
      }
      var myChart = echarts.init(document.getElementById('pieChart'));
      myChart.setOption(pie1)
    },
  }
}
</script>
<style>
  .like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
  }
  .el-statistic .con .number{
    font-size: 32px;
    padding: 10px 4px;
    color: #666;
  }
  .remark{
    font-size: 14px; color: #909399;
  }
  .remark .tip{
    padding: 4px 25px;
  }
  .ditem{
    cursor: pointer;
  }
  .ditem a{
    text-decoration: none;
  }
  .opt-btns{
    margin-top: 50px;
    text-align: center;
  }
  .ditem .unit{
    color: #909399;
    font-size: 18px;
  }
  .building-info{
    text-align: center; font-size: 20px;
    margin-bottom: 25px;
    color: #666;
  }
  .rooms{
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  .rooms .rdata{
    width: 150px;
    padding: 10px;
    margin: 10px 25px;
    text-align: center;
    background-color: #41a8a8;
    color: #fff;
    box-shadow: 2px 1px 5px rgba(0,0,0,.3);
  }
  .rdata .room-name{
    padding: 5px 0;
  }
  .rdata .room-num{
    margin-bottom: 5px;
  }
  .rdata .room-num .big{
    font-size: 28px;
  }
</style>