import Taro from "@tarojs/taro";
import axios_plus from "../config/axios_plus";
import {LOCAL_STORAGE_KEYS} from "../config/local_storage_keys";

function authLogin () {
    console.log('登陆')
    Taro.login({
        success: res => {
            const wxLoginCode = res.code
            if (wxLoginCode) {
                console.log(`wx login code ${wxLoginCode}`);
                axios_plus.post("/user/login", {
                    reqPlatform: 0,
                    data: {
                        jsCode: wxLoginCode,
                    }
                }).then(resp => {
                    const data = resp.data
                    if ('E0001' === data.code) {
                        console.log(data.data)
                        // 存储用户信息
                        Taro.setStorageSync(LOCAL_STORAGE_KEYS.user, data.data.user)
                        Taro.setStorageSync(LOCAL_STORAGE_KEYS.token, data.data.token)
                        Taro.showToast({ title: '自动登陆成功', duration: 4000 })
                        // TODO 获取当前路由并重定向到当前路由
                    } else {
                        console.error('自动登录失败, 请退出小程序重试');
                    }
                }).catch(() => {
                    console.log('用户未注册, 跳转登录')
                    Taro.redirectTo({ url: '/packageA/pages/login/index' })
                })
            } else {
                console.log('登录失败！' + res.errMsg)
                Taro.showToast({
                    icon: 'none',
                    title: '登录失败'
                })
            }
        }
    })
}

export { authLogin }