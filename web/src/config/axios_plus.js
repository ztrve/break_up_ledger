import axios from 'taro-axios'
import Taro from '@tarojs/taro'
import {getBaseUrl} from "./api_config";
import {authLogin} from "../util/authUtil";

const showErrorToast = (msg) => {
    Taro.showToast({
        title: msg,
        icon: 'none',
    })
}

const instance = axios.create({
    baseURL: getBaseUrl(),
})

instance.interceptors.request.use(
    (config) => {
        Taro.showLoading({
            title: '加载中',
            mask: true,
        })
        let token = Taro.getStorageSync('token')
        if (typeof token == 'undefined') {
            token = ''
        }
        token = 'Bearer ' + token
        config.headers = {
            'Content-Type': 'application/json;charset=utf-8',
            Authorization: token,
        }
        return config
    },
    (error) => {
        console.log(error)
        return Promise.reject(error)
    },
)

// respone拦截器
instance.interceptors.response.use(
    (response) => {
        Taro.hideLoading()
        if (response.data.isError) {
            console.log('error response')
            console.log(response)
            showErrorToast(response.data.error.msg)
        } else {
            return response
        }
    },
    (error) => {
        if (error.response) {
            Taro.hideLoading()
            console.log('err', error)

            let res = error.response.data
            switch (res.code) {
                case 400:
                    showErrorToast(res.msg || '非法请求')
                    break
                case 401:
                    authLogin()
                    break
                case 403:
                    showErrorToast(res.msg || '非法请求')
                    break
                case 404:
                    showErrorToast(res.msg || '请求资源不存在')
                    break
                case 500:
                case 502:
                    showErrorToast(res.msg || '服务器开小差啦')
                    break
                default:
                    showErrorToast(res.msg || res.statusText)
            }
        } else {
            console.log(error)
            showErrorToast('请检查网络连接状态')
        }

        return Promise.reject(error)
    },
)

export default instance