<template>
  <view class="friends-wrapper">
    <!-- 空 -->
    <nut-empty v-if="undefined === friendList || friendList.length === 0" image="error">
      <template v-slot:description>
        <p>不是吧，不是吧</p>
        <p>不会还有人连个好友都没有吧</p>
      </template>
      <template v-slot:default>
        <div style="margin-top: 10px">
          <nut-button icon="add" type="primary">添加好友</nut-button>
        </div>
      </template>
    </nut-empty>
    <!-- 好友列表 -->
    <nut-cell-group v-for="groupIndex in 2" :key="groupIndex">
      <template v-slot:title>
        <div class="friends-index-first-letter nut-cell-group__title">A</div>
      </template>
      <nut-cell v-for="(friend, index) in friendList" :key="index" :is-link="true">
        <template v-slot:icon>
          <nut-avatar
            icon="https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png">
          </nut-avatar>
        </template>
        <template v-slot:title>
          <div class="friend-title">
            <div>{{ friend.name }}</div>
          </div>
        </template>
      </nut-cell>
    </nut-cell-group>
  </view>
</template>

<script setup>
import { defineComponent, onMounted, reactive } from 'vue';

defineComponent({
  name: 'Friends'
})

const friendList = reactive([
  { code: 'zhangsan', name: '张三', icon: '' },
  { code: 'lisi', name: '李四', icon: '' },
  { code: 'wangwu', name: '王五', icon: '' },
  { code: 'zhaoliu', name: '赵六', icon: '' },
  { code: 'hengqi', name: '横七', icon: '' },
  { code: 'shuba', name: '竖八', icon: '' }
])

onMounted(() => {

})
</script>

<style lang="scss">
.friends-wrapper {
  height: 100%;
  overflow-y: auto;
}

.friends-index-first-letter {
  background: #b9b9b9;
  padding: 5px 10px;
  color: #404040;
}

.friend-title {
  height: 100%;
  display: flex;
  align-items: center;
  padding: 5px 10px;
}
</style>
