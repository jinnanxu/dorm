import Vue from 'vue'
import store from './store'
import router from './router'
import App from './components/App'
import ElementUI from 'element-ui'
import axios from "axios";
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/common.css'
import './assets/css/shop.css'
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { 
  fas,
} from '@fortawesome/free-solid-svg-icons';

//引入quill-editor编辑器
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
Vue.use(VueQuillEditor)

// 引入echarts
import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts

Vue.use(ElementUI)
// 过滤器
import * as custom from './utils/util'
Object.keys(custom).forEach(key => {
  Vue.filter(key, custom[key])
})

// 路由拦截
axios.interceptors.response.use(
  response => {
      console.log('Axios响应拦截：普通',response)
      return response;
  },
  error => {
      if (error.response) {
          switch (error.response.status) {
              case 401:
                  // 返回 401 清除token信息并跳转到登录页面
                  // store.commit(types.LOGOUT);
                  console.log('Axios响应拦截：未登录！')
                  this.$store.commit('login', {userId: null})
                  router.replace({
                      path: '/login'
                  })
          }
      }
      return Promise.reject(error.response.data)   // 返回接口返回的错误信息
  }
)

Vue.component('font-awesome-icon', FontAwesomeIcon);
library.add(
  fas,
);

new Vue({
  router,
  store,
  template: '<App/>',
  components: { App },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'margin:0;')
  },
}).$mount('#app')
 