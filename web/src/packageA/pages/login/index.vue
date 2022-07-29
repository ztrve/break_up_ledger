<template>
  <div class="login-wrapper" :style="{
    width: win.w,
    height: win.h
  }">
    <img :src="require('./login-bg.jpeg')" class="bg" :style="{
      width: win.w,
      height: win.h
    }">
    <div class="login-top">
      <!-- 占位 -->
    </div>
    <div class="login-interact">
      <div class="login-title login-interact-item">微信授权登陆/注册</div>
      <nut-button class="login-button login-interact-item" color="#07c160" size="small"
        @click="clickLoginInteractButton">
        点击授权
      </nut-button>
      <div class="login-other-operate login-interact-item login-interact-item-last">
        <div class="user-agree">
          <div class="agree-botton" @click="agreeUserProtocol = !agreeUserProtocol">
            <div :class="{
              circle: true,
              check: agreeUserProtocol
            }">
              <div v-if="agreeUserProtocol">✔</div>
            </div>
            同意
          </div>
          <div class="user-protocol" @click="clickUserProtocol">《用户协议》</div>
        </div>
        <div class="has-problem" @click="clickHasProblem">遇到问题？</div>
      </div>
    </div>
    <div class="login-bottom">
      <!-- 占位 -->
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import Taro from '@tarojs/taro'
import axios_plus from "../../../config/axios_plus";
import { LOCAL_STORAGE_KEYS } from "../../../config/local_storage_keys";

const win = ref({
  w: '0',
  h: '0'
})

const agreeUserProtocol = ref(false)

function clickHasProblem() {
  Taro.showToast({
    title: '不, 你没有问题！',
    icon: 'none',
  })
}

function clickUserProtocol() {
  Taro.showToast({
    title: '没有用户协议...........',
    icon: 'none',
  })
}

function clickLoginInteractButton() {
  if (agreeUserProtocol.value === false) {
      Taro.showToast({
            title: '请先阅读并勾选用户协议...',
            icon: 'none',
        })
    return
  }

  // 获取用户资料
  Taro.getUserProfile({
    desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
    success: (userProfileResp) => {
      // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
      console.log('--- userProfile --');
      console.log(userProfileResp);

      Taro.login({
        success: function (res) {
          const wxLoginCode = res.code
          if (wxLoginCode) {
            console.log(`wx login code ${wxLoginCode}`);
            axios_plus.post("/user/register",{
              reqPlatform: 0,
              data: {
                jsCode: wxLoginCode,
                userProfile: userProfileResp
              }
            }).then(backendLoginResp => {
              const data = backendLoginResp.data
              if ('E0001' === data.code) {
                console.log(data.data)
                // 存储用户信息
                Taro.setStorageSync(LOCAL_STORAGE_KEYS.user, data.data.user)
                Taro.setStorageSync(LOCAL_STORAGE_KEYS.token, data.data.token)
                // 跳转主页
                Taro.redirectTo({ url: '/pages/index/index' })
              } else {
                console.error('request backendLogin fault');
              }
            })
          } else {
            console.log('登录失败！' + res.errMsg)
          }
        }
      })
    }
  })


}

onMounted(() => {
  const systemInfo = Taro.getSystemInfoSync()
  const windowWidth = systemInfo.windowWidth
  const windowHeight = systemInfo.windowHeight

  win.value.w = windowWidth + 'px'
  win.value.h = windowHeight + 'px'
})

</script>

<style lang="scss">
.login-wrapper {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  display: flex;
  flex-direction: column;
}

.login-top {
  flex-grow: 6;
}

.login-interact {
  flex: 5;
  margin-left: auto;
  margin-right: auto;
  display: flex;
  flex-direction: column;
  justify-self: center;
  width: 80%;
}

.login-interact>.login-interact-item {
  margin-bottom: 14px;
}

.login-interact>.login-interact-item-last {
  margin-bottom: 0;
}

.login-interact>.login-title {
  color: #FFF;
  font-size: 25px;
  font-weight: 600;
  margin-bottom: 20px;
}

.login-interact>.login-button {}

.login-interact>.login-other-operate {
  display: flex;
  flex-direction: row;
  font-size: 14px;
  color: #FFF;
  justify-content: space-between;
  align-items: center;
}

.login-other-operate>.user-agree {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.user-agree>.agree-botton {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.agree-botton>.circle {
  border: 1px solid #FFF;
  width: 16px;
  height: 16px;
  border-radius: 16px;
  margin-right: 5px;
  font-weight: 400;
  display: flex;
  justify-content: center;
  align-items: center;
}

.agree-botton>.check {
  background-color: #07c160;
}

.user-agree>.user-protocol {
  color: #4d90fe
}

.login-bottom {
  flex-grow: 4;
}

.bg {
  position: absolute;
  z-index: -1;
}
</style>
