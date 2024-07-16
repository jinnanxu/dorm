<template>
  <!--前台页面骨架-->
  <div>
    <zui-header></zui-header>
    <div class="home-page">
      <router-view></router-view>
    </div>
    <zui-footer/>
  </div>
</template>
<script>
  import './less/home-page.less'
  import Logo from '../components/Logo'
  import ZuiHeader from '../components/Header'
  import ZuiFooter from '../components/Footer'
  import DropList from 'vue-droplist'
  export default{
    components: {Logo, ZuiHeader, ZuiFooter,DropList},
    data () {
      return {
        configData : {
            position: {  // 设置显示位置，position
              top: '60px',
              right: '15px',
              bottom: '',
              left: ''
            },
            width: '40%', // 设置宽度
        }
      }
    },
   methods: {
      
    }

  }
</script>
<style scoped>

</style>