<template>
  <div class="home-wrapper">
    <div class="ledger-wrapper">
      <!-- 账单选择 -->
      <nut-menu>
        <nut-menu-item v-model="activeLedger" :options="ledgers" />
      </nut-menu>
      <!-- 账单列表 -->
      <div class="ledgers">
        <nut-cell-group>
          <nut-cell v-for="(ledgerRecord, index) in activeLedgerRecords" :key="index" :is-link="true" :center="true"
            @click="ledgerRecordDetailFlag = true">
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
    <ledger-detail v-model="ledgerRecordDetailFlag"></ledger-detail>
  </div>
</template>

<script setup>
import { defineComponent, onMounted, ref } from 'vue';
import LedgerDetail from '../ledgerdetail'

defineComponent({
  name: 'Home'
})
const ledgers = ref([
  { value: 0, text: '账单1' },
  { value: 1, text: '账单2' },
])
const activeLedger = ref(ledgers.value[0].value)
/**
 * 格式：
 * [{ text: '买菜', time: '2022-07-21' }]
 */
const activeLedgerRecords = ref([
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买菜', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
  { id: 10, text: '买豆腐', time: '2022-07-21' },
])
const ledgerRecordDetailFlag = ref(false)

onMounted(() => {

})

</script>

<style lang="scss">
.home-wrapper {
  height: 100%;
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
</style>
