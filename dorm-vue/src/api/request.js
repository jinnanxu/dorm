//request.js
import axios from "axios";
import qs from "qs";
import router from '../router'
import store from '@/store'
import { getToken } from '@/utils/auth'

/****** 创建axios实例 ******/
const service = axios.create({
    baseURL: process.env.BASE_URL,  // api的base_url
    timeout: 10000  // 请求超时时间
});

/****** request拦截器==>对请求参数做处理 ******/
service.interceptors.request.use(config => {
    config.method === 'post'
        ? config.data = qs.stringify({ ...config.data })
        : config.params = { ...config.params };
    config.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    // jwt的token
    config.headers['Authorization'] = getToken()
    return config;
}, error => {  //请求错误处理
    Promise.reject(error)
});

/****** respone拦截器==>对响应做处理 ******/
service.interceptors.response.use(
    response => {  //成功请求到数据
        //这里根据后端提供的数据进行对应的处理
        if(response.data.code == '401'||response.data.code == '403'){
            console.log('Axios响应拦截：未登录！')
            localStorage.removeItem('AuthToken')
            localStorage.removeItem('LoginUser')
            localStorage.removeItem('LoginAdmin')
            router.replace({
                path: '/login'
            })
        } else {
            return response.data;
        }
    },
    error => {  //响应错误处理
        console.log('响应错误处理error');
        console.log(error);
        console.log(JSON.stringify(error));
        if(JSON.parse(JSON.stringify(error)).response.status === 401){
            console.log('Axios响应拦截：未登录！')
            router.replace({
                path: '/login'
            })
        }

        let text = JSON.parse(JSON.stringify(error)).response.status === 404
            ? '404'
            : '网络异常，请重试';
        return Promise.reject(error)
    }
);

export default service;