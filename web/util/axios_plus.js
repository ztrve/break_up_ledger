import axios from 'taro-axios'
import Taro from '@tarojs/taro'
import { useAuthStore } from '@/stores/modules/auth'

const showErrorToast = (msg) => {
  Taro.showToast({
    title: msg,
    icon: 'none',
  })
}

const instance = axios.create({
  baseURL: process.env.BASE_URL,
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
      showErrorToast(response.data.error.message)
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
          showErrorToast(res.message || '非法请求')
          break
        case 401:
          const authStore = useAuthStore()
          authStore.login()
          // showErrorToast(res.message || '当前登录已过期，请重新登录')
          // Taro.navigateTo({
          //   url: '/pages/login/index'
          // })
          break
        case 403:
          showErrorToast(res.message || '非法请求')
          break
        case 404:
          showErrorToast(res.message || '请求资源不存在')
          break
        case 500:
        case 502:
          showErrorToast(res.message || '服务器开小差啦')
          break
        default:
          showErrorToast(res.message || res.statusText)
      }
    } else {
      console.log(error)
      showErrorToast('请检查网络连接状态')
    }

    return Promise.reject(error)
  },
)

export default instance