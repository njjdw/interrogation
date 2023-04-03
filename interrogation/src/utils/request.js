import router from '@/router'
import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import Cookies from 'js-cookie'
const service = axios.create({
  baseURL: "http://127.0.0.1:88/interrogation/api", // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 10000 ,// request timeout
  withCredentials: true
})

service.interceptors.request.use(
	config => {
		if (localStorage.getItem("token")) {
			config.headers.set("token",localStorage.getItem("token"));
		}
		return config;
	},
	err => {
		return Promise.reject("未登录");
	}
)
service.interceptors.response.use(
response => {
    // if the custom code is not 20000 and 0, it is judged as an error.
	if (!response.data){
		Message({
		  message: response.data.message || '无响应结果',
		  type: 'error',
		  duration: 5 * 1000
		})
		return;
	}
    if (response.data.code&&response.data.code !== 20000 && response.data.code !== 0) {
      Message({
        message: response.data.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (response.data.code === 50008 || response.data.code === 50012 || response.data.code === 50014) {
		localStorage.clear();
        router.replace("/");
		console.log("token过期")
      }
      // return Promise.reject(new Error(res.message || 'Error'));
    } else {
      return response
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
export default service