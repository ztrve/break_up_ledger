<template>
  <div class="home-wrapper" ref="homeWrapperRef">
    <div class="empty-wrapper" v-if="ledgers.length === 0">
      <nut-empty image="network" description="你不会还没有账本吧?">
        <template v-slot:default>
          <div style="margin-top: 10px">
            <nut-button icon="order" type="primary" @click="openLedgerSettingPopup">新建账本</nut-button>
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

      <!-- 账本多功能按钮 -->
      <nut-drag :style="{ position: 'absolute', bottom: '100px', right: '50px' }" direction="y">
        <nut-button type="primary" icon="uploader" shape="square" size="mini">记一笔</nut-button>
      </nut-drag>
    </div>
    <!-- 账单详情 -->
    <ledger-detail v-model="showLedgerRecordDetail"></ledger-detail>

    <!-- 首页菜单弹出 -->
    <home-menu v-model:visible="showHomeMenu"
               :ledgers="ledgers"
               @click-change="changeActiveLedger"
               @click-create-ledger="openCreateLedgerSettingPopup"
               @click-setting="openLedgerSettingPopup"
    ></home-menu>

    <!-- 创建账单 -->
    <ledger-setting :type="ledgerSettingOptions.type" v-model:visible="ledgerSettingOptions.show"
                    :ledger="ledgerSettingOptions.ledger" @change-ledger="changeLedgerSetting"
                    @remove-ledger="removeLedger"
    ></ledger-setting>
  </div>
</template>

<script setup>
import {defineComponent, ref} from 'vue';
import LedgerDetail from '../ledgerdetail'
import HomeMenu from '/src/components/homemenu'
import axios_plus from "../../config/axios_plus"
import LedgerSetting from '/src/components/ledgersetting'
import {dateFormat} from "../../util/DateUtil";

defineComponent({
  name: 'Home'
})

const homeWrapperRef = ref(null)
const ledgers = ref([])
const activeLedger = ref({})
/**
 * 格式：
 * [{ text: '买菜', time: '2022-07-21' }]
 */
const activeLedgerRecords = ref([
  // {id: 10, text: '买菜', time: '2022-07-21'},
])
const activeLedgerRecordsPage = {
  pages: 0,
  size: 10,
  current: 0
}

function initActiveLedgerRecords() {
  activeLedgerRecords.value = []
  activeLedgerRecordsPage.pages = 0
  activeLedgerRecordsPage.size = 10
  activeLedgerRecordsPage.current = 0
}

const showLedgerRecordDetail = ref(false)
const showHomeMenu = ref(false)
const ledgerSettingOptions = ref({
  show: false,
  type: 'create',
  ledger: {}
})

function openLedgerSettingPopup(ledger) {
  ledgerSettingOptions.value = {
    show: true,
    type: 'setting',
    ledger
  }
}

function openCreateLedgerSettingPopup() {
  ledgerSettingOptions.value = {
    show: true,
    type: 'create',
    ledger: {}
  }
}

function loadLedgers() {
  axios_plus.get("/ledger/list")
      .then(response => {
        const resp = response.data
        if ('E0001' === resp.code && resp.data) {
          console.log('ledgers loaded')
          ledgers.value = resp.data
        }
      })
      .catch(e => {
        console.error(e)
      })
}

function loadActiveLedgerRecords(ledger) {
  axios_plus.get(
      `/ledger/record?ledgerId=${ledger.id}`
  ).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      if (resp.data === undefined || resp.data === null || resp.data.length === 0) {
        activeLedgerRecords.value = []
      } else {
        resp.data.forEach(record => {
          record.text = record.tag
          record.time = dateFormat(record.createTime)
        })
        activeLedgerRecords.value = activeLedgerRecords.value.concat(resp.data)
        console.log(activeLedgerRecords.value)
      }
    }
  })

}

function changeActiveLedger(active) {
  console.log('触发变更 active')
  loadActiveLedgerRecords(active.value)
  activeLedger.value = active.value
  showHomeMenu.value = false
}

function changeLedgerSetting(ledger) {
  let sameLedgerIndex = -1
  ledgers.value.forEach((ledgerItem, index) => {
    if (ledgerItem.id === ledger.id) {
      sameLedgerIndex = index
    }
  })
  console.log('sameLedgerIndex is ' + sameLedgerIndex)
  if (sameLedgerIndex >= 0) {
    ledgers.value[sameLedgerIndex] = ledger
    if (activeLedger.value.id === ledger.id) {
      activeLedger.value = ledger
    }
  } else {
    ledgers.value.push(ledger)
  }
}

function removeLedger(ledger) {
  ledgers.value = ledgers.value.filter(ledgerItem => ledgerItem.id !== ledger.id)
}

loadLedgers()

</script>

<style lang="scss">
.home-wrapper {
  height: 100%;
  position: relative;
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
</style>
