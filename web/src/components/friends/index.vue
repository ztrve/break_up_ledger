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
    <nut-cell-group v-if="undefined !== friendList && friendList.length !== 0" class="friends">
      <nut-cell v-for="(friend, index) in friendList" :key="index" :is-link="true" :center="true">
        <template v-slot:icon>
          <nut-avatar
              :icon="friend.avatarUrl">
          </nut-avatar>
        </template>
        <template v-slot:title>
          <div class="friend-title">
            <div>{{ friend.nickname }}</div>
          </div>
        </template>
      </nut-cell>
    </nut-cell-group>
    <nut-button
        class="global-add-friend" shape="square" type="primary" icon="add"
        v-if="undefined !== friendList && friendList.length !== 0"
        @click="showAddFriendPopup = true">
      添加好友
    </nut-button>

    <nut-popup position="bottom" :style="{ height: '80%' }" v-model:visible="showAddFriendPopup">
      <div class="friend-request-pop-wrapper">
        <div class="friend-request-pop-title">添加好友</div>
        <div class="friend-request-pop-form">
          <nut-input
              v-model="friendRequestInput"
              label="请输入"
              placeholder="输入手机号或好友码"
          />
        </div>

        <nut-button class="friend-request-pop-send-friend-request-button" shape="square" type="primary"
                    :loading="friendRequestButtonLoading"
                    @click="sendFriendRequest">
          发送好友申请
        </nut-button>
      </div>
    </nut-popup>

  </div>
</template>

<script setup>
import {defineComponent, onMounted, ref} from 'vue';
import axios_plus from "../../config/axios_plus";
import Taro from "@tarojs/taro";

defineComponent({
  name: 'Friends'
})

const showAddFriendPopup = ref(false)
const friendRequestInput = ref("")
const friendRequestButtonLoading = ref(false)

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
      }).catch(() => {
    friendList.value = []
  })
}

/**
 * 发送好友申请
 */
function sendFriendRequest() {
  friendRequestButtonLoading.value = true
  axios_plus.post("/notice/friend", {
    userCharacteristics: friendRequestInput.value
  }).then(resp => {
    friendRequestButtonLoading.value = false
    if (resp.data.code === 'E0001') {
      Taro.showToast({icon: 'none', title: '申请成功, 请耐心等待'})
      showAddFriendPopup.value = false
      friendRequestInput.value = ''
    }
  }).catch(() => {
    friendRequestButtonLoading.value = false
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
  display: flex;
  flex-direction: column;
}

.friends-index-first-letter {
  background: #b9b9b9;
  padding: 5px 10px;
  color: #404040;
}

.friends {
  flex-grow: 1;
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

.friend-request-pop-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.friend-request-pop-title {
  font-size: 18px;
  font-weight: bold;
  padding: 10px 0;
}

.friend-request-pop-form {
  flex-grow: 1;
}

.friend-request-pop-send-friend-request-button {

}
</style>
