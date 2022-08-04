<template>
  <div class="mine-wrapper">
    <div class="mine-user-display">
      <img class="mine-avatar-bg" :style="{
        width: avatarCssOptions.bgWidth,
        height: avatarCssOptions.bgHeight
      }" :src="require('../../../assets/user-bg.jpeg')">
      <img size="large" class="mine-avatar" :style="{
        top: avatarCssOptions.top,
        left: avatarCssOptions.left,
        width: avatarCssOptions.width,
        height: avatarCssOptions.height,
        'border-radius': avatarCssOptions.borderRadius
      }" :src="userInfo.avatarUrl">
    </div>

    <nut-cell-group>
      <nut-cell title="昵称">
        <template #link>
          {{ userInfo.nickname }}
        </template>
      </nut-cell>
      <nut-cell title="好友码" :is-link="true" @click="clickCode">
        <template #link>
          {{ userInfo.code }}
        </template>
      </nut-cell>
      <nut-cell title="手机号">
        <template #link>
          <div v-if="hasUserPhone" @click="showEditPhone = true">{{ userInfo.phone }}</div>
          <div v-else @click="showEditPhone = true">点击更新手机号</div>
        </template>
      </nut-cell>
    </nut-cell-group>
    <!-- 删除朋友提示 -->
    <nut-dialog
        teleport="#app"
        title="更新手机号"
        :close-on-click-overlay="true"
        ok-text="提交"
        @cancel="cancelEditPhone"
        @ok="commitEditPhone"
        v-model:visible="showEditPhone"
    >
      <input class="nut-input-text" placeholder="请输入手机号" type="text" v-model="formPhone"/>
    </nut-dialog>
  </div>
</template>

<script setup>
import Taro from '@tarojs/taro'
import {defineComponent, onMounted, ref} from 'vue';
import {LOCAL_STORAGE_KEYS} from "../../config/local_storage_keys";
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'Mine'
})

const showEditPhone = ref(false)

const userInfo = ref(Taro.getStorageSync(LOCAL_STORAGE_KEYS.user))
const avatarCssOptions = ref({
  top: '0px',
  left: '0px',
  width: '0px',
  height: '0px',
  borderRadius: '0px',
  bgWidth: '0px',
  bgHeight: '0px'
})
const formPhone = ref('')

function commitEditPhone() {
  axios_plus.put('/user/phone', {
    phone: formPhone.value
  }).then(({data}) => {
    if ('E0001' === data.code) {
      userInfo.value.phone = formPhone.value
      formPhone.value = ''
      showEditPhone.value = false
      Taro.setStorageSync(LOCAL_STORAGE_KEYS.user, userInfo.value)
    }
  })
}

function cancelEditPhone() {
  showEditPhone.value = false
  formPhone.value = ''
}

function hasUserPhone() {
  const phone = userInfo.value.phone
  return undefined !== phone && null !== phone && '' !== phone
}

function clickCode() {
  Taro.setClipboardData({
    data: userInfo.value.code,
  })
}

function computeBackground() {
  const systemInfo = Taro.getSystemInfoSync()
  const windowWidth = systemInfo.windowWidth
  // bg 1170 × 954
  const bgw = windowWidth
  const bgh = 954 / 1170 * bgw
  avatarCssOptions.value.bgWidth = bgw + 'px'
  avatarCssOptions.value.bgHeight = bgh + 'px'
  Taro.createSelectorQuery().select('.user-display > .mine-avatar-bg')
      .boundingClientRect()
      .exec(bgs => {
        const avaw = bgw * 0.26
        avatarCssOptions.value.width = avaw + 'px'
        avatarCssOptions.value.height = avaw + 'px'
        avatarCssOptions.value.left = (bgw - avaw) / 2 + 'px'
        avatarCssOptions.value.top = bgh * 0.525 + 'px'
        avatarCssOptions.value.borderRadius = avaw / 2 + 'px'
      })
}

onMounted(() => {
  Taro.nextTick(() => {
    computeBackground()
  })
})
</script>

<style lang="scss">
.mine-wrapper {
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}

.mine-user-display {
  position: relative;
}

.mine-avatar-bg {
  height: auto;
}

.mine-avatar {
  position: absolute;
  z-index: 10;
}
</style>
