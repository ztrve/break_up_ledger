<template>
  <view class="index" :style="`height: ${indexHeight}`">
    <view class="main-wrapper" :style="`height: ${mainWrapperHeight}`">
      <home v-if="pageIsShow(3)" />
      <friends v-if="pageIsShow(1)" />
      <notices v-if="pageIsShow(2)" />
      <mine v-if="pageIsShow(0)" />
    </view>

    <nut-tabbar :value="activePageIndex" @change="changeActivePageIndex" :bottom="true" @tab-switch="activePage">
      <nut-tabbar-item v-for="tabbar in tobbarList" :key="tabbar.name" :tab-title="tabbar.name" :icon="tabbar.icon" />
    </nut-tabbar>
  </view>
</template>

<script>
import Taro, { eventCenter, getCurrentInstance } from '@tarojs/taro'
import { reactive, onMounted, toRefs } from 'vue';
import Home from '/components/home'
import Friends from '/components/friends'
import Notices from '/components/notices'
import Mine from '/components/mine'

export default {
  name: 'Index',
  components: {
    Home, Friends, Notices, Mine
  },
  setup() {
    const state = reactive({
      indexHeight: '0px',
      mainWrapperHeight: '0px',
      activePageIndex: 0,
      tobbarList: [
        { name: "首页", icon: "home" },
        { name: "好友", icon: "people" },
        { name: "通知", icon: "notice" },
        { name: "我的", icon: "my" }
      ]
    })

    const methods = {
      activePage: (obj) => {
        Taro.nextTick(() => {
          if (state.activePageIndex !== obj.state.active) {
            state.activePageIndex = obj.state.active
          }
        })
      },
      pageIsShow: (pageIndex) => {
        return pageIndex === state.activePageIndex
      },
      changeActivePageIndex: (index) => {
        state.activePageIndex = index
      },
    }

    onMounted(() => {
      eventCenter.once(getCurrentInstance().router.onReady, () => {
        const systemInfo = Taro.getSystemInfoSync()
        const windowHeight = systemInfo.windowHeight
        state.indexHeight = windowHeight + 'px'
        Taro.createSelectorQuery().select('.index > .nut-tabbar')
          .boundingClientRect()
          .exec(tabbars => {
            const tabbarHeight = tabbars[0].height
            state.mainWrapperHeight = windowHeight - tabbarHeight + 'px'
          })
      })
    })

    return {
      ...methods,
      ...toRefs(state)
    }
  }
}

</script>

<style lang="scss">
.index {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.main-wrapper {
  overflow: hidden;
}
</style>
