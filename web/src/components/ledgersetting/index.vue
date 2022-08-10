<template>
  <!-- 账本配置弹出 -->
  <nut-popup position="bottom" :style="{ width: '100%', height: 'auto' }" :visible="popupVisible" @update:visible="close()">
    <div class="ledger-setting-popup-wrapper">
      <div class="ledger-setting-popup-title-wrapper">
        <div class="ledger-setting-popup-title">{{ createLedgerPopupTitle() }}</div>
        <nut-icon
            v-if="props.type !== 'create'" @click="canFormEdit = !canFormEdit" name="edit" size="15px"
            :class="{
              'create-ledger-popup-edit': true,
              'can-edit': canFormEdit,
              'cant-edit': !canFormEdit
            }"
        ></nut-icon>
      </div>
      <nut-form class="ledger-setting-popup-form" :model-value="ledgerFormData">
        <nut-form-item label="账本名称" :required="true">
          <div  @click="randomLedgerName" style="display: flex; flex-direction: row; justify-content: center; align-items: center;">
            <input style="flex-grow: 1" class="nut-input-text" placeholder="请输入账本名称" type="text" v-model="ledgerFormData.name"
                   :disabled="true"/>
            <nut-icon name="refresh" v-if="canFormEdit"></nut-icon>
          </div>
        </nut-form-item>
        <nut-form-item label="所有成员可以提交账单" body-align="right">
          <nut-switch v-model="ledgerFormData.canMemberCommit" :disable="!canFormEdit"></nut-switch>
        </nut-form-item>
        <nut-form-item label="邀请成员" body-align="right">
          <nut-avatar-group max-count="15" zIndex="left" max-content="...">
            <nut-avatar
                v-for="(friend, index) in ledgerFormData.ledgerMembers"
                :key="index"
                :url="friend.click ? '' : friend.avatarUrl"
                @active-avatar="clickFriendAvatar(friend)"
            >
              删
            </nut-avatar>
            <nut-avatar icon="add" @active-avatar="showInviteFriendPopup = true" v-if="canFormEdit"></nut-avatar>
          </nut-avatar-group>

          <!-- 删除朋友提示 -->
          <nut-dialog
              teleport="#app"
              title="移除朋友"
              content="删除账本操作无法撤销，请谨慎操作！"
              :close-on-click-overlay="true"
              ok-text="删除"
              @cancel="showRemoveFriendDialog = false"
              @ok="removeLedgerMember"
              v-model:visible="showRemoveFriendDialog"
          ></nut-dialog>
        </nut-form-item>
      </nut-form>

      <div class="ledger-setting-popup-submit">
        <nut-button class="ledger-setting-popup-submit-item" shape="square" type="danger"
                    @click="showRemoveDialog = true" :loading="removeLoading" :disabled="!canFormEdit"
                    v-if="props.type !== 'create'">
          删除
        </nut-button>
        <nut-button class="ledger-setting-popup-submit-item" shape="square" type="primary"
                    @click="submitLedger" :loading="submitLoading"
                    :disabled="!canFormEdit">
          提交
        </nut-button>
      </div>
      <!-- 删除账本提示 -->
      <nut-dialog
          teleport="#app"
          :title="'删除账本 ' + props.ledger.name"
          content="删除账本操作无法撤销，请谨慎操作！"
          :close-on-click-overlay="true"
          ok-text="删除"
          @cancel="showRemoveDialog = false"
          @ok="removeLedger"
          v-model:visible="showRemoveDialog"
      ></nut-dialog>

      <!-- 邀请好友 -->
      <nut-popup position="bottom" :style="{ width: '100%', height: 'auto' }" v-model:visible="showInviteFriendPopup">
        <nut-cell-group>
          <nut-cell v-for="(friend, index) in friendList" :key="index" :center="true">
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
              <nut-button :disabled="ledgerFormData.memberIds.indexOf(friend.id) >= 0"
                          type="primary" shape="square" size="mini" @click="inviteFriend(friend)">
                邀请加入
              </nut-button>
            </template>
          </nut-cell>
        </nut-cell-group>
      </nut-popup>
    </div>
  </nut-popup>
</template>

<script setup>
import {defineComponent, defineEmits, defineProps, ref, watch} from 'vue';
import axios_plus from "../../config/axios_plus";
import Taro from "@tarojs/taro";
import {LOCAL_STORAGE_KEYS} from "../../config/local_storage_keys";

defineComponent({
  name: 'LedgerSetting'
})

const props = defineProps({
  visible: Boolean,
  // 页面类型: create新建 setting配置
  type: {
    type: String,
    default: 'create',
    required: true,
    validator(value) {
      return 'create' === value || 'setting' === value
    }
  },
  ledger: {
    type: Object,
    default: {},
    required: true
  }
})

const ledgerFormData = ref({})
const waitRemoveLedgerFriend = ref({})
const friendList = ref([])
const ledgerNamePool = [
  '风雨同舟', '金戈铁马', '斗转星移', '乾坤一掷', '华山论剑', '唯我独尊',
  '满江红', '侠客行', '梦江南', '如梦令', '枫泾古镇', '幽月轮', '圣墓山',
  '笑傲江湖', '山雨欲来', '剑胆琴心', '金榜题名', '致青春', '战无不胜',
  '侠骨柔情', '中华乾', '唯满侠', '双梦', '白帝', '长安', '鹅满峡'
]

const popupVisible = ref(false)
const canFormEdit = ref(true)
const showRemoveDialog = ref(false)
const showRemoveFriendDialog = ref(false)
const submitLoading = ref(false)
const removeLoading = ref(false)
const showInviteFriendPopup = ref(false)

// 衔接父子组件弹出框开关
const emit = defineEmits(['update:visible', 'change-ledger', 'remove-ledger'])

function close() {
  initCreateLedgerFormData()
  popupVisible.value = false
  emit('update:visible', false)
}

watch(props, (newVal, oldValue) => {
  if (newVal.visible !== popupVisible.value) {
    popupVisible.value = newVal.visible
  }
  if ('create' === newVal.type) {
    canFormEdit.value = true
  } else if ('setting' === newVal.type) {
    canFormEdit.value = false
  }
  if ({} === newVal.ledger) {
    initCreateLedgerFormData()
  } else {
    initCreateLedgerFormData(newVal.ledger)
  }
})

function randomLedgerName() {
  if (!canFormEdit.value) {
    return
  }
  const randomIndex = parseInt(Math.random() * ledgerNamePool.length + '', 0)
  ledgerFormData.value.name = ledgerNamePool[randomIndex]
}

function loadFriends() {
  axios_plus.get("/friend/my")
      .then(resp => {
        if (resp.data.code === 'E0001') {
          friendList.value = resp.data.data
          friendList.value.push(Taro.getStorageSync(LOCAL_STORAGE_KEYS.user))
        } else {
          friendList.value = []
        }
      }).catch(() => {
    friendList.value = []
  })
}

function createLedgerPopupTitle() {
  if ('create' === props.type) {
    return '新建账本'
  } else if ('setting' === props.type) {
    if (canFormEdit.value) {
      return '修改账本'
    } else {
      return '账本详情'
    }
  }
  return 'error'
}

function initCreateLedgerFormData(ledger) {
  if (undefined === ledger || null === ledger || '{}' === JSON.stringify(ledger)) {
    const me = Taro.getStorageSync(LOCAL_STORAGE_KEYS.user)
    ledgerFormData.value = {
      id: null,
      name: '',
      canMemberCommit: true,
      memberIds: [me.id],
      ledgerMembers: [me]
    }
    randomLedgerName()
  } else {
    ledgerFormData.value = {
      id: ledger.id,
      name: ledger.name,
      canMemberCommit: ledger.canMemberCommit,
      memberIds: ledger.memberIds,
      ledgerMembers: friendList.value.filter(user => ledger.memberIds.indexOf(user.id) >= 0)
    }
  }
}

function submitCreateLedgerForm() {
  submitLoading.value = true
  axios_plus.post("/ledger", {
    name: ledgerFormData.value.name,
    // 账本类型
    type: 0,
    canMemberCommit: ledgerFormData.value.canMemberCommit,
    memberIds: ledgerFormData.value.memberIds
  }).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      emit('change-ledger', resp.data)
      close()
    }
    Taro.showToast({title: resp.msg, icon: 'none'})
  }).catch(e => {
  }).finally(() => {
    submitLoading.value = false
  })
}

function submitUpdateLedgerForm() {
  submitLoading.value = true
  axios_plus.put("/ledger", {
    id: ledgerFormData.value.id,
    name: ledgerFormData.value.name,
    // 账本类型
    type: 0,
    canMemberCommit: ledgerFormData.value.canMemberCommit,
    memberIds: ledgerFormData.value.memberIds
  }).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      emit('change-ledger', resp.data)
      close()
    }
    Taro.showToast({title: resp.msg, icon: 'none'})
  }).catch(e => {
  }).finally(() => {
    submitLoading.value = false
  })
}

function submitLedger() {
  if ('create' === props.type) {
    submitCreateLedgerForm()
  } else if ('setting' === props.type) {
    submitUpdateLedgerForm()
  }
}

function removeLedger() {
  removeLoading.value = true
  axios_plus.delete(
      `/ledger/${ledgerFormData.value.id}`
  ).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      emit('remove-ledger', props.ledger)
      close()
    }
    Taro.showToast({title: resp.msg, icon: 'none'})
  }).catch(e => {
  }).finally(() => {
    removeLoading.value = false
  })
}

function inviteFriend(friend) {
  ledgerFormData.value.memberIds.push(friend.id)
  ledgerFormData.value.ledgerMembers.push(friend)
}

function clickFriendAvatar(friend) {
  if (!canFormEdit.value) {
    return
  }
  if (friend.click) {
    waitRemoveLedgerFriend.value = friend
    showRemoveFriendDialog.value = true
    friend.click = false
  } else {
    friend.click = true
  }
}

function removeLedgerMember() {
  const friend = waitRemoveLedgerFriend.value
  const me = Taro.getStorageSync(LOCAL_STORAGE_KEYS.user)
  if (me.id === friend.id) {
    Taro.showToast({ icon: 'none', title: '无法移除自己' })
    return
  }
  if (friend.id === props.ledger.ownerId) {
    Taro.showToast({ icon: 'none', title: '无法移除拥有人和账门人' })
    return
  }

  ledgerFormData.value.ledgerMembers = ledgerFormData.value.ledgerMembers.filter(member => member.id !== friend.id)
  ledgerFormData.value.memberIds = ledgerFormData.value.memberIds.filter(memberId => memberId !== friend.id)
}

initCreateLedgerFormData()
loadFriends()

</script>

<style lang="scss">
// .home-menu-wrapper {
// }

.ledger-setting-popup-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.ledger-setting-popup-title-wrapper {
  width: 100%;
  margin: 10px 0;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-end;
}

.ledger-setting-popup-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.create-ledger-popup-edit {
  margin-left: 5px;
}

.can-edit {
  color: #4d90fe;
}

.cant-edit {
  color: #404040;
}

.ledger-setting-popup-form {
  flex-grow: 1;
}

.ledger-setting-popup-submit {
  display: flex;
  flex-direction: row;
  width: 100%;
}

.ledger-setting-popup-submit-item {
  flex: 1;
}
</style>
