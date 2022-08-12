<template>
  <nut-popup class="ledger-detail-wrapper" position="right" :style="{ width: '100%', height: '100%' }"
             :visible="props.visible" @close="close">
    <nut-navbar @on-click-back="close" title="记账详情"/>
    <nut-cell-group v-if="props.visible">
      <nut-cell title="账本" :center="true">
        <template #link>
          {{ props.ledger.name }}
        </template>
      </nut-cell>
      <nut-cell title="标签" :center="true">
        <template #link>
          {{ props.ledgerRecord.tag }}
        </template>
      </nut-cell>
      <nut-cell title="金额变化">
        <template v-slot:link>
          <nut-price :price="props.ledgerRecord.amount / 100" size="normal" :need-symbol="false" :thousands="true"/>
        </template>
      </nut-cell>

      <!--      不可删除 后面有用-->
      <!--      <nut-cell title="计算方式">-->
      <!--        <template v-slot:link>-->
      <!--          <nut-tag class="ledger-desc-item" :type="computeModeAsTagType(ledgerDetailData.computeModeId).tagType">-->
      <!--            {{ computeModeAsTagType(ledgerDetailData.computeModeId).name }}-->
      <!--          </nut-tag>-->
      <!--        </template>-->
      <!--      </nut-cell>-->
      <nut-cell title="记录人" :center="true">
        <template v-slot:link>
          <div class="ledger-desc">
            <nut-avatar size="small"
                        :icon="props.ledgerRecord.creator.avatarUrl">
            </nut-avatar>
            <!--            <div class="ledger-desc-item">{{ props.ledgerRecord.creator.nickname }}</div>-->
            <nut-tag v-if="false" class="ledger-desc-item" type="primary">单</nut-tag>
          </div>
        </template>
      </nut-cell>
      <nut-cell :is-link="true" @click="showBalanceDetail">
        <template v-slot:title>
          <div class="flex-inline">
            <div>结算前</div>
            <nut-icon name="ask"></nut-icon>
          </div>
        </template>
        <template v-slot:link>
          <nut-price :price="props.ledgerRecord.prevWalletAmount / 100" size="normal" :need-symbol="false"
                     :thousands="true"/>
        </template>
      </nut-cell>
      <nut-cell :is-link="true" @click="showBalanceDetail">
        <template v-slot:title>
          <div class="flex-inline">
            <div>结算后</div>
            <nut-icon name="ask"></nut-icon>
          </div>
        </template>
        <template v-slot:link>
          <nut-price :price="props.ledgerRecord.afterWalletAmount / 100" size="normal" :need-symbol="false"
                     :thousands="true"/>
        </template>
      </nut-cell>
      <nut-cell>
        <template v-slot:title>
          <div class="flex-inline">
            <div>结算时间</div>
          </div>
        </template>
        <template v-slot:link>
          {{ dateFormat('YYYY-mm-dd HH:MM:SS', new Date(props.ledgerRecord.createTime)) }}
        </template>
      </nut-cell>
      <nut-cell title="成员钱包" :center="true">
        <template v-slot:link>
          <nut-avatar-group size="small" :max-count="8">
            <nut-avatar v-for="user in ledger.members" :key="user.id" :icon="user.avatarUrl"
                        @active-avatar="openUserLedgerWalletRecord(user)">
            </nut-avatar>
          </nut-avatar-group>
        </template>
      </nut-cell>
    </nut-cell-group>

    <!-- 成员钱包弹出框 -->
    <nut-popup position="bottom" :style="{ height: '50%' }" v-model:visible="showUserLedgerWalletRecord">
      <div class="user-ledger-detail-wrapper">
        <h3 class="user-ledger-detail-title">{{ activeUser.nickname }}的钱包变化</h3>
      </div>
      <nut-cell-group>
        <nut-cell title="支出金额">
          <template v-slot:link>
            <nut-price :price="activeLedgerMemberWalletRecordDetail.amount / 100" size="normal" :need-symbol="false"
                       :thousands="true"/>
          </template>
        </nut-cell>
        <nut-cell title="结算前">
          <template v-slot:link>
            <nut-price :price="activeLedgerMemberWalletRecordDetail.prevWalletAmount / 100" size="normal"
                       :need-symbol="false"
                       :thousands="true"/>
          </template>
        </nut-cell>
        <nut-cell title="结算后">
          <template v-slot:link>
            <nut-price :price="activeLedgerMemberWalletRecordDetail.afterWalletAmount / 100" size="normal"
                       :need-symbol="false"
                       :thousands="true"/>
          </template>
        </nut-cell>
        <nut-cell>
          <template v-slot:title>
            <div class="flex-inline">
              <div>结算时间</div>
            </div>
          </template>
          <template v-slot:link>
            {{ dateFormat('YYYY-mm-dd HH:MM:SS', new Date(activeLedgerMemberWalletRecordDetail.createTime)) }}
          </template>
        </nut-cell>
      </nut-cell-group>
    </nut-popup>
  </nut-popup>

</template>

<script setup>
import {showToast} from '@tarojs/taro'
import {defineComponent, defineProps, defineEmits, ref} from 'vue';
import axios_plus from "../../config/axios_plus";
import {dateFormat} from "../../util/DateUtil";

defineComponent({
  name: 'LedgerRecord'
})

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  ledgerRecord: {
    type: Object,
    required: true
  },
  ledger: {
    type: Object,
    required: true
  }
})
// visible处理
const emit = defineEmits(['update:visible'])

function close() {
  emit('update:visible', false)
}

const showUserLedgerWalletRecord = ref(false)

const computeModeTagTypeList = [
  {id: 0, name: '个人支出', tagType: 'primary'},
  {id: 0, name: '共同支出', tagType: 'success'},
]
const activeUser = ref({})
const activeLedgerMemberWalletRecordDetail = ref({})

function loadLedgerMemberWalletRecordDetail() {
  activeLedgerMemberWalletRecordDetail.value
  console.log(props.ledgerRecord)
  axios_plus.get(
      `/ledger/member/wallet/record/one?ledgerMemberId=${activeUser.value.id}&ledgerRecordId=${props.ledgerRecord.id}`
  ).then(({data}) => {
    if (data.code === 'E0001') {
      activeLedgerMemberWalletRecordDetail.value = data.data
    }
  })
}

function openUserLedgerWalletRecord(user) {
  activeLedgerMemberWalletRecordDetail.value = {}
  activeUser.value = user
  loadLedgerMemberWalletRecordDetail()
  showUserLedgerWalletRecord.value = true
}

function showBalanceDetail() {
  showToast({
    title: '公共钱包余额',
    icon: 'none',
    duration: 2000
  })
}

</script>

<style lang="scss">
// .ledger-detail-wrapper {
// }

.ledger-collapse-item > .collapse-item {
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
