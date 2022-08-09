import Taro from "@tarojs/taro";
import axios_plus from "../config/axios_plus";
import {LOCAL_STORAGE_KEYS} from "../config/local_storage_keys";

let isLoading = false

function authLogin() {
    if (isLoading) {
        return
    }

    isLoading = true
    console.log('尝试静默登陆')
    Taro.login({
        success: res => {
            const wxJsCode = res.code
            if (wxJsCode) {
                console.log(`wx login code ${wxJsCode}`);
                axios_plus.post("/user/login", {
                    reqPlatform: 0,
                    data: {
                        jsCode: wxJsCode,
                    }
                }).then(resp => {
                    const data = resp.data
                    if ('E0001' === data.code) {
                        console.log(data.data)
                        // 存储用户信息
                        Taro.setStorageSync(LOCAL_STORAGE_KEYS.user, data.data.user)
                        Taro.setStorageSync(LOCAL_STORAGE_KEYS.token, data.data.token)
                        console.log('静默登陆成功，重载当前页面')
                        const routeConf = Taro.getCurrentInstance().router
                        Taro.redirectTo({ url: routeConf.path })
                    } else {
                        console.error('自动登录失败, 请退出小程序重试');
                    }
                }).catch(() => {
                    console.log('静默登陆失败，用户未注册')
                }).finally(() => {
                    isLoading = false
                })
            } else {
                console.log('登录失败！' + res.errMsg)
                Taro.showToast({icon: 'none', title: '登录失败'})
                isLoading = false
            }
        }
    })
}

export {authLogin}