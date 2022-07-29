<template>
  <div class="friends-wrapper">
    <!-- 空 -->
    <nut-empty v-if="undefined === friendList || friendList.length === 0" image="error">
      <template v-slot:description>
        <p>不是吧，不是吧</p>
        <p>不会还有人连个好友都没有吧</p>
      </template>
      <template v-slot:default>
        <div style="margin-top: 10px">
          <nut-button icon="add" type="primary" @click="showAddFriendPopup = true">添加好友</nut-button>
        </div>
      </template>
    </nut-empty>
    <!-- 好友列表 -->
    <nut-cell-group>
<!--      <template v-slot:title>-->
<!--        <div class="friends-index-first-letter nut-cell-group__title">A</div>-->
<!--      </template>-->
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

    <nut-popup position="bottom" :style="{ height: '80%' }" v-model:visible="showAddFriendPopup">
      <div class="friend-request-pop-title">添加好友</div>
      <nut-input
          v-model="friendRequestInput"
          label="请输入"
          placeholder="输入手机号或好友码"
      />
      <nut-button class="" shape="square" type="primary" @click="sendFriendRequest">
        发送好友申请
      </nut-button>
    </nut-popup>
  </div>
</template>

<script setup>
import {defineComponent, onMounted, ref} from 'vue';
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'Friends'
})

const showAddFriendPopup = ref(false)
const friendRequestInput = ref("")

/**
 *
 * @type {UnwrapNestedRefs<[{code: string, name: string, icon: string},null]>}
 */
const friendList = ref([])

function loadFriends() {
  axios_plus.get("/friend/my")
      .then(resp => {
        if (resp.data.code === 'E0001') {
          friendList.value = resp.data.data
        } else {
          friendList.value = []
        }
      }).catch(() => { friendList.value = [] })
}

/**
 * 发送好友申请
 */
function sendFriendRequest() {
  axios_plus.post("/notice/friend", {
    userCharacteristics: friendRequestInput
  })
}

loadFriends()

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

.friend-request-pop-title {
  text-align: center;
  padding: 5px;
}
</style>
