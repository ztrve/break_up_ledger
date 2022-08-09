<template>
  <view class="index" :style="`height: ${indexHeight}`">
    <view class="main-wrapper" :style="`height: ${mainWrapperHeight}`">
      <home v-if="pageIsShow(0)"/>
      <friends v-if="pageIsShow(1)"/>
      <notices v-if="pageIsShow(2)"/>
      <mine v-if="pageIsShow(3)"/>
    </view>

    <nut-tabbar :value="activePageIndex" @change="changeActivePageIndex" :bottom="true" @tab-switch="activePage">
      <nut-tabbar-item v-for="tabbar in tabbarList" :key="tabbar.name" :tab-title="tabbar.name" :icon="tabbar.icon"/>
    </nut-tabbar>

    <nut-dialog
        teleport="#app"
        title="注册后体验后续功能"
        :close-on-click-overlay="true"
        ok-text="去注册"
        @cancel="showLoginDialog = false"
        @ok="pageToLogin"
        v-model:visible="showLoginDialog"
    ></nut-dialog>
  </view>
</template>

<script>
import Taro, { eventCenter, getCurrentInstance } from '@tarojs/taro'
import { reactive, onMounted, toRefs, ref, watch } from 'vue';
import Home from '/src/components/home'
import Friends from '/src/components/friends'
import Notices from '/src/components/notices'
import Mine from '/src/components/mine'
import { LoginDialogStore } from '/store/index'

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
      tabbarList: [
        {name: "首页", icon: "home"},
        {name: "好友", icon: "people"},
        {name: "通知", icon: "notice"},
        {name: "我的", icon: "my"}
      ],
    })

    const showLoginDialog = ref(false)

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
      pageToLogin: () => {
        Taro.redirectTo({url: '/packageA/pages/login/index'})
      }
    }

    const loginDialogStore = LoginDialogStore()
    watch(showLoginDialog, (newVal, oldValue) => {
      if (loginDialogStore.show === newVal.value) return
      loginDialogStore.changeShow(newVal.value)
    })
    loginDialogStore.$subscribe((mutation, state) => {  // state变化时回调。有变化信息和状态两个参数
      if (state.show === undefined || state.show === showLoginDialog.value) return
      showLoginDialog.value = state.show
    }, {
      detached: false,  // 在组件卸载时是否继续监听
      deep: true,  // 是否深度监听
      flush: 'post',  // post:组件更新后执行；sync:始终同步触发；pre:组件更新前执行
    })

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
      ...toRefs(state),
      showLoginDialog
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
