<template>
  <div class="home-wrapper">
    <div class="empty-wrapper" v-if="ledgers.length === 0">
      <nut-empty image="network" description="你不会还没有账本吧?">
        <template v-slot:default>
          <div style="margin-top: 10px">
            <nut-button icon="order" type="primary" @click="openCreateLedgerPopup">新建账本</nut-button>
          </div>
        </template>
      </nut-empty>
    </div>
    <div class="ledger-wrapper" v-else>
      <nut-navbar :left-show="false">
        <template #content>
          <div class="home-title">
            {{ activeLedger.name }}
          </div>
        </template>
        <template #right>
          <nut-icon class="right" name="horizontal-n" @click="showHomeMenu = true"></nut-icon>
        </template>
      </nut-navbar>
      <!-- 账单列表 -->
      <div class="ledgers">
        <nut-cell-group>
          <nut-cell v-for="(ledgerRecord, index) in activeLedgerRecords" :key="index"
                    :is-link="true" :center="true"
                    @click="showLedgerRecordDetail = true">
            <template v-slot:icon>
              <nut-avatar size="small"
                          icon="https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png">
              </nut-avatar>
            </template>
            <template v-slot:title>
              <div class="ledger-desc">
                <div class="ledger-desc-item">{{ ledgerRecord.text }}</div>
                <nut-tag class="ledger-desc-item" type="primary">单</nut-tag>
              </div>
            </template>
            <template v-slot:link>
              <div>{{ ledgerRecord.time }}</div>
              <nut-icon name="right"></nut-icon>
            </template>
          </nut-cell>
        </nut-cell-group>
      </div>
    </div>
    <!-- 账单详情 -->
    <ledger-detail v-model="showLedgerRecordDetail"></ledger-detail>

    <!-- 首页菜单弹出 -->
    <nut-popup class="home-menu-wrapper"
               position="right"
               :style="{ width: '60%', height: '100%' }"
               v-model:visible="showHomeMenu">
      <home-menu :ledgers="ledgers" @change="changeActiveLedger" @create-new-ledger="openCreateLedgerPopup"></home-menu>
    </nut-popup>

    <!-- 创建账本弹出 -->
    <nut-popup class="home-menu-wrapper"
               position="bottom"
               :style="{ width: '100%', height: 'auto' }"
               v-model:visible="showCreateLedgerPopup">
      <div class="create-ledger-popup-wrapper">
        <div class="create-ledger-popup-title">新建账本</div>
        <nut-form class="create-ledger-popup-form" :model-value="createLedgerFormData">
          <nut-form-item label="账本名称" :required="true">
            <input class="nut-input-text" placeholder="请输入账本名称" type="text" v-model="createLedgerFormData.name"/>
          </nut-form-item>
          <nut-form-item label="所有成员可以提交账单" body-align="right">
            <nut-switch v-model="createLedgerFormData.canMemberCommit"></nut-switch>
          </nut-form-item>
          <nut-form-item label="邀请成员" body-align="right">
            <nut-avatar-group max-count="15" zIndex="left" max-content="...">
              <nut-avatar
                  v-for="(f, index) in createLedgerFormData.ledgerMembers"
                  :key="index"
                  :url="f.avatarUrl"
              >
              </nut-avatar>
              <nut-avatar icon="add" @active-avatar="showInviteFriendPopup = true"></nut-avatar>
            </nut-avatar-group>
          </nut-form-item>
        </nut-form>
        <nut-button class="create-ledger-popup-submit" shape="square" type="primary"
                    @click="submitCreateLedgerForm" :loading="createLedgerSubmitLoading">
          提交
        </nut-button>

        <!-- 邀请好友 -->
        <nut-popup class="home-menu-wrapper"
                   position="bottom"
                   :style="{ width: '100%', height: 'auto' }"
                   v-model:visible="showInviteFriendPopup">
          <nut-cell-group>
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
              <template v-slot:link>
                <nut-button :disabled="createLedgerFormData.ledgerMemberIds.indexOf(friend.id) >= 0"
                            type="primary" shape="square" size="mini" @click="inviteFriend(friend)">
                  邀请加入
                </nut-button>
              </template>
            </nut-cell>
          </nut-cell-group>
        </nut-popup>
      </div>
    </nut-popup>
  </div>
</template>

<script setup>
import Taro from "@tarojs/taro";
import {defineComponent, ref} from 'vue';
import LedgerDetail from '../ledgerdetail'
import HomeMenu from '/src/components/homemenu'
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'Home'
})
const ledgers = ref([])
const activeLedger = ref({})
/**
 * 格式：
 * [{ text: '买菜', time: '2022-07-21' }]
 */
const activeLedgerRecords = ref([
  // {id: 10, text: '买菜', time: '2022-07-21'},
])

const showLedgerRecordDetail = ref(false)
const showHomeMenu = ref(false)
const showCreateLedgerPopup = ref(false)
const showInviteFriendPopup = ref(false)
const createLedgerSubmitLoading = ref(false)

const createLedgerFormData = ref({})
const friendList = ref([])

function initCreateLedgerFormData() {
  const me = Taro.getStorageSync("user")
  createLedgerFormData.value = {
    name: '',
    canMemberCommit: true,
    ledgerMemberIds: [me.id],
    ledgerMembers: [me]
  }
}

function openCreateLedgerPopup() {
  showCreateLedgerPopup.value = true
  initCreateLedgerFormData()
}

function loadFriends() {
  axios_plus.get("/friend/my")
      .then(resp => {
        if (resp.data.code === 'E0001') {
          friendList.value = resp.data.data
          friendList.value.push(Taro.getStorageSync("user"))
        } else {
          friendList.value = []
        }
      }).catch(() => {
    friendList.value = []
  })
}

function inviteFriend(friend) {
  createLedgerFormData.value.ledgerMemberIds.push(friend.id)
  createLedgerFormData.value.ledgerMembers.push(friend)
}

function submitCreateLedgerForm() {
  createLedgerSubmitLoading.value = true
  axios_plus.post("/ledger", {
    name: createLedgerFormData.value.name,
    // 账本类型
    type: 0,
    canMemberCommit: createLedgerFormData.value.canMemberCommit,
    ledgerMemberIds: createLedgerFormData.value.ledgerMemberIds
  }).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      ledgers.value.push(resp.data)
      showCreateLedgerPopup.value = false
    }
    Taro.showToast({title: resp.msg, icon: 'none'})
  }).catch(e => {
  }).finally(() => {
    createLedgerSubmitLoading.value = false
  })
}

function loadLedgers() {
  axios_plus.get("/ledger/list")
      .then(response => {
        const resp = response.data
        if ('E0001' === resp.code && resp.data) {
          ledgers.value = resp.data
        }
      })
      .catch(e => {
        console.error(e)
      })
}

function changeActiveLedger(active) {
  activeLedger.value = active.value
  showHomeMenu.value = false
}

initCreateLedgerFormData()
loadFriends()
loadLedgers()

</script>

<style lang="scss">
.home-wrapper {
  height: 100%;
}

.home-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.ledger-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.ledgers {
  flex-grow: 1;
  overflow-x: hidden;
  overflow-y: auto;
}

.ledger-desc {
  padding: 0 10px;
  display: flex;
  flex-direction: row;
}

.ledger-desc-item {
  margin: 0 5px;
}

.create-ledger-popup-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.create-ledger-popup-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  width: 100%;
  text-align: center;
  margin: 10px 0;
}

.create-ledger-popup-form {
  flex-grow: 1;
}

.create-ledger-popup-submit {
  width: 100%;
}
</style>
