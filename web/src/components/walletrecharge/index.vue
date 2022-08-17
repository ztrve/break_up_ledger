<template>
  <nut-popup
      class="wr-popup"
      position="bottom"
      :style="{ width: '100%', height: 'auto' }"
      :visible="props.visible"
      @close="close">
    <div class="pop-title">钱包充值</div>
    <nut-tabs v-model="activeTab" type="smile">
      <nut-tabpane title="钱包充值" pane-key="normal-recharge">
        <nut-form :model-value="form">
          <nut-form-item label="充值对象" body-align="right">
            <nut-avatar-group size="small" :max-count="8">
              <nut-avatar v-for="user in members" :key="user.id" :icon="user.avatarUrl"></nut-avatar>
            </nut-avatar-group>
          </nut-form-item>
          <nut-form-item
              label="公共充值" body-align="right" :required="true" prop="amount"
              :rules="[
              { required: true, message: '请输入账单金额' },
              { regex: /^(0*[1-9]+\d*(\.\d{1,2})?)|(0+\.0[1-9])|(0+\.[1-9]\d?)$/, message: '请输入正确的账单金额。小数点后最多两位' },
              ]"
          >
            <div style="display: flex; justify-content: flex-end; color: #000; align-items: center">
              <input v-model="form.ledgerRechargeAmount" type="digit" placeholder="0.0"
                     @input="changeLedgerRechargeAmount"/>¥
            </div>
          </nut-form-item>
          <nut-form-item
              label="各成员充值" body-align="right" :required="true" prop="amount"
              :rules="[
              { required: true, message: '请输入账单金额' },
              { regex: /^(0*[1-9]+\d*(\.\d{1,2})?)|(0+\.0[1-9])|(0+\.[1-9]\d?)$/, message: '请输入正确的账单金额。小数点后最多两位' },
              ]"
          >
            <div style="display: flex; justify-content: flex-end; color: #000; align-items: center">
              <input v-model="form.memberRechargeAmount" type="digit" placeholder="0.0"
                     @input="changeMemberRechargeAmount"/>¥
            </div>
          </nut-form-item>
          <nut-button style="width: 100%;" shape="square" type="primary" :loading="commitLoading" @click="commit">
            提交
          </nut-button>
        </nut-form>
      </nut-tabpane>
      <nut-tabpane title="单独充值" pane-key="single-recharge">
        尽请期待
      </nut-tabpane>
    </nut-tabs>

  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, defineEmits, watch} from 'vue'
import axios_plus from '../../config/axios_plus'
import Taro from '@tarojs/taro'
import {LOCAL_STORAGE_KEYS} from '../../config/local_storage_keys'

defineComponent({
  name: 'WalletRecharge'
})

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  ledger: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:visible', 'recharge-success'])

function close() {
  emit('update:visible', false)
}

const activeTab = ref('normal-recharge')
const form = ref({
  ledgerRechargeAmount: '',
  memberRechargeAmount: ''
})
const members = ref([])

const commitLoading = ref(false)

function commit() {
  axios_plus.post(
      '/ledger/record/recharge',
      {
        ledgerId: props.ledger.id,
        amount: form.value.ledgerRechargeAmount * 100
      }
  ).then(({ data }) => {
    if (data.code === 'E0001') {
      close()
      emit('recharge-success')
    }
  })
}

function changeLedgerRechargeAmount(e) {
  const money = (e.target.value.match(/^\d*(\.?\d{0,2})/g)[0]) || null
  Taro.nextTick(() => {
    form.value.ledgerRechargeAmount = money
    form.value.memberRechargeAmount = ((money / members.value.length + '').match(/^\d*(\.?\d{0,2})/g)[0]) || null
  })
}

function changeMemberRechargeAmount(e) {
  const money = (e.target.value.match(/^\d*(\.?\d{0,2})/g)[0]) || null
  Taro.nextTick(() => {
    form.value.memberRechargeAmount = money
    form.value.ledgerRechargeAmount = ((money * members.value.length + '').match(/^\d*(\.?\d{0,2})/g)[0]) || null
  })
}

function loadMembers() {
  axios_plus.get(
      `/ledger/member/wallet?ledgerId=${props.ledger.id}`
  ).then(({data}) => {
    if (data.code === 'E0001') {
      members.value = data.data.map(item => item.member)
    }
  })
}

watch(props, (newVal, oldVal) => {
  loadMembers()
})
</script>

<style lang="scss">
.wr-popup {
}
</style>
