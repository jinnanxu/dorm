import axios from "axios";
import qs from "qs";
import { getToken } from '@/utils/auth'

const request = axios.create({
    baseURL: process.env.BASE_URL,  // api的base_url
    timeout: 60000  // 请求超时时间
});

request.interceptors.request.use(config => {
    // 文件上传的content-type
    config.headers['Content-Type'] = 'multipart/form-data'
    // jwt的token
    config.headers['Authorization'] = getToken()
    return config;
}, error => {  //请求错误处理
    Promise.reject(error)
});

// axios.post("http://localhost:8021/dorm-server/upload", formData, config).then((res) => {
//         if (res.data.success) {
//           //this.photoObj = res.data.data.objectId;
//           console.log('图片上传成功！', res.data)
//           this.pics.push(res.data.data);
//           if(this.editForm.pics!=""){
//             this.editForm.pics = this.editForm.pics+","+res.data.data
//           }else{
//             this.editForm.pics = res.data.data
//           }
//           console.log(this.editForm.pics)
//         } else {
//           this.$message.error(res.data.message);
//         }
//       });

export const fileUploader = data => {
    console.log('文件上传方法：', data)
    return request({
        url: '/dorm-server/upload',
        method: 'post',
        data
    })
};

export const ExcelUploader = data => {
    console.log('Excel文件上传方法：', data)
    return request({
        url: '/dorm-server/admin/user/uploadExcel',
        method: 'post',
        data
    })
};