<template>
 <img alt="xiaoyanyun" class="zui-logo" :src="src" @click="onClick"/>
</template>
<script>
  import './less/logo.less'
  import logo from './img/logo.png'
  export default {
    name: 'Logo',
    props: {
      type: {
        default: 'xiaoyanyun'
      },
      href: {
        default: '/'
      }
    },
    computed: {
      src: function () {
        return logo
      }
    },
    methods: {
      onClick: function (e) {
        this.$router.push({path:'/'})
      }
    }
  }
</script>
