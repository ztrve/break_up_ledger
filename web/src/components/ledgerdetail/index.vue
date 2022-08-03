<template>
  <nut-popup class="ledger-detail-wrapper" position="right" :style="{ width: '100%', height: '100%' }"
    v-model:visible="props.modelValue">
    <nut-navbar @on-click-back="close" title="记账详情" />
    <nut-cell-group>
      <nut-cell title="支出金额">
        <template v-slot:link>
          <nut-price :price="ledgerDetailData.payAmount" size="normal" :need-symbol="false" :thousands="true" />
        </template>
      </nut-cell>
      <nut-cell title="计算方式">
        <template v-slot:link>
          <nut-tag class="ledger-desc-item" :type="computeModeAsTagType(ledgerDetailData.computeModeId).tagType">
            {{ computeModeAsTagType(ledgerDetailData.computeModeId).name }}
          </nut-tag>
        </template>
      </nut-cell>
      <nut-cell title="支付对象" :desc="ledgerDetailData.payPeople"></nut-cell>
      <nut-cell :is-link="true" @click="showBalanceDetail">
        <template v-slot:title>
          <div class="flex-inline">
            <div>付前余额</div>
            <nut-icon name="ask"></nut-icon>
          </div>
        </template>
        <template v-slot:link>
          <nut-price :price="ledgerDetailData.commonWalletBalance.prev" size="normal" :need-symbol="false"
            :thousands="true" />
        </template>
      </nut-cell>
      <nut-cell :is-link="true" @click="showBalanceDetail">
        <template v-slot:title>
          <div class="flex-inline">
            <div>付后余额</div>
            <nut-icon name="ask"></nut-icon>
          </div>
        </template>
        <template v-slot:link>
          <nut-price :price="ledgerDetailData.commonWalletBalance.now" size="normal" :need-symbol="false"
            :thousands="true" />
        </template>
      </nut-cell>
      <nut-cell title="个人钱包" :center="true">
        <template v-slot:link>
          <nut-avatar-group size="small" :max-count="8">
            <nut-avatar v-for="user in ledgerDetailData.teamUsers" :key="user.id" :icon="user.avatarUrl"
              @active-avatar="openUserLedgerDetail = true">
            </nut-avatar>
          </nut-avatar-group>
        </template>
      </nut-cell>
    </nut-cell-group>
    <nut-popup position="bottom" :style="{ height: '50%' }" v-model:visible="openUserLedgerDetail">
      <div class="user-ledger-detail-wrapper">
        <h3 class="user-ledger-detail-title">ztrue的钱包</h3>
      </div>
      <nut-cell-group>
        <nut-cell title="支出金额">
          <template v-slot:link>
            <nut-price :price="userLedgerDetail.payAmount" size="normal" :need-symbol="false" :thousands="true" />
          </template>
        </nut-cell>
        <nut-cell title="付前余额">
          <template v-slot:link>
            <nut-price :price="userLedgerDetail.privateWalletBalance.prev" size="normal" :need-symbol="false" :thousands="true" />
          </template>
        </nut-cell>
        <nut-cell title="付后余额">
          <template v-slot:link>
            <nut-price :price="userLedgerDetail.privateWalletBalance.now" size="normal" :need-symbol="false" :thousands="true" />
          </template>
        </nut-cell>
      </nut-cell-group>
    </nut-popup>
  </nut-popup>

</template>

<script setup>
import { showToast } from '@tarojs/taro'
import { defineComponent, defineProps, defineEmits, onMounted, ref } from 'vue';

defineComponent({
  name: 'LedgerDetail'
})

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})
// modelValue处理
const emit = defineEmits(['update:modelValue'])
function close() {
  emit('update:modelValue', false)
}

const openUserLedgerDetail = ref(false)
const ledgerDetailData = ref({
  payAmount: 1140.345,
  computeModeId: 0,
  payPeople: 'ztrue',
  commonWalletBalance: {
    prev: 5234.674,
    now: 3245.3,
  },
  teamUsers: [
    { id: 1, avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg' },
    { id: 2, avatarUrl: 'http://images.xuejuzi.cn/1612/1_161230185104_1.jpg' },
    { id: 3, avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg' },
    { id: 4, avatarUrl: 'http://images.xuejuzi.cn/1612/1_161230185104_1.jpg' },
    { id: 5, avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg' },
    { id: 6, avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg' },
    { id: 7, avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg' },
    { id: 8, avatarUrl: 'https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F1018%2F446da79dj00r15fve001fc000hs00vmg.jpg&thumbnail=650x2147483647&quality=80&type=jpg' },
  ]
})

const userLedgerDetail = ref({
  payAmount: 33.30,
  privateWalletBalance: {
    prev: 99.89,
    now: 53.00,
  },
})

const computeModeTagTypeList = [
  { id: 0, name: '个人支出', tagType: 'primary' },
  { id: 0, name: '共同支出', tagType: 'success' },
]

function computeModeAsTagType(computeModeId) {
  return computeModeTagTypeList.filter(item => item.id === computeModeId)[0]
}

function showBalanceDetail() {
  showToast({
    title: '公共钱包余额',
    icon: 'none',
    duration: 2000
  })
}

onMounted(() => {

})
</script>

<style lang="scss">
// .ledger-detail-wrapper {
// }

.ledger-collapse-item>.collapse-item {
  padding-left: 16px;
  padding-right: 16px;
}

.flex-inline {
  display: flex;
  flex-direction: row;
}

.user-ledger-detail-wrapper {
  padding: 4px 5px
}

.user-ledger-detail-title {
  text-align: center;
  padding: 8px 2px;
}
</style>
