<template>
  <div class="mine-wrapper">
    <div class="mine-user-display">
      <img class="mine-avatar-bg" :style="{
        width: avatarCssOptions.bgWidth,
        height: avatarCssOptions.bgheight
      }" :src="require('../../../assets/user-bg.jpeg')">
      <img size="large" class="mine-avatar" :style="{
        top: avatarCssOptions.top,
        left: avatarCssOptions.left,
        width: avatarCssOptions.width,
        height: avatarCssOptions.height,
        'border-radius': avatarCssOptions.borderRadius
      }" :src="userInfo.avatarUrl">
    </div>

    <nut-empty image="empty" image-size="90px" description="程序员正在秃头研发中..." />
  </div>
</template>

<script setup>
import Taro, { eventCenter, getCurrentInstance } from '@tarojs/taro'
import { defineComponent, onMounted, ref } from 'vue';

defineComponent({
  name: 'Mine'
})

const userInfo = ref({
  avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg'
})

const avatarCssOptions = ref({
  top: '0px',
  left: '0px',
  width: '0px',
  height: '0px',
  borderRadius: '0px',
  bgWidth: '0px',
  bgheight: '0px'
})

function computeBackground() {
  const systemInfo = Taro.getSystemInfoSync()
  const windowWidth = systemInfo.windowWidth
  // bg 1170 × 954
  const bgw = windowWidth
  const bgh = 954 / 1170 * bgw
  avatarCssOptions.value.bgWidth = bgw + 'px'
  avatarCssOptions.value.bgheight = bgh + 'px'
  Taro.createSelectorQuery().select('.user-display > .mine-avatar-bg')
    .boundingClientRect()
    .exec(bgs => {
      // const bgw = windowWidth
      // const bgh = bgs[0].height
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
