<template>
  <div class="home-wrapper" ref="homeWrapperRef">
    <div class="empty-wrapper" v-if="ledgers.length === 0">
      <nut-empty image="network" description="你不会还没有账本吧?">
        <template v-slot:default>
          <div style="margin-top: 10px">
            <nut-button icon="order" type="primary" @click="openCreateLedgerSettingPopup">新建账本</nut-button>
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
                    @click="clickLedgerRecordDetail(ledgerRecord)">
            <template v-slot:icon>
              <nut-avatar size="small"
                          :icon="ledgerRecord.creator.avatarUrl">
              </nut-avatar>
            </template>
            <template v-slot:title>
              <div class="ledger-desc">
                <div class="ledger-desc-item">
                  <div>{{ ledgerRecord.text }}</div>
                  <div style="font-size: 12px">{{ ledgerRecord.amount / 100 }} 软妹币</div>
                </div>
                <nut-tag v-if="false" class="ledger-desc-item" type="primary">单</nut-tag>
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
      <div style="position: absolute; bottom: 0; right: 6px">
        <nut-button type="primary" icon="uploader" shape="square" size="mini" @click="showLedgerRecordSetting = true">
          记一笔
        </nut-button>
      </div>
    </div>
    <!-- 账单详情 -->
    <ledger-record v-model:visible="showLedgerRecordDetail" :ledger-record="activeLedgerRecord" :ledger="activeLedger"
    ></ledger-record>

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

    <!--  -->
    <ledger-record-setting
        v-model:visible="showLedgerRecordSetting" :type="'create'"
        :ledger="activeLedger" @submit-success="addActiveLedgerRecord"
    ></ledger-record-setting>
  </div>
</template>

<script setup>
import {defineComponent, ref} from 'vue';
import LedgerRecord from '/src/components/ledgerrecord'
import HomeMenu from '/src/components/homemenu'
import LedgerRecordSetting from '/src/components/ledgerrecordsetting'
import axios_plus from '../../config/axios_plus'
import LedgerSetting from '/src/components/ledgersetting'
import {dateFormat} from '../../util/DateUtil'
import {LoginDialogStore} from '../../../store'
import Taro from '@tarojs/taro'
import {LOCAL_STORAGE_KEYS} from "../../config/local_storage_keys";

defineComponent({
  name: 'Home'
})

const showLedgerRecordDetail = ref(false)
const showHomeMenu = ref(false)
const showLedgerRecordSetting = ref(false)

const homeWrapperRef = ref(null)
const ledgers = ref([])
const activeLedger = ref({})
const activeLedgerRecord = ref({})
const ledgerSettingOptions = ref({
  show: false,
  type: 'create',
  ledger: {}
})

/**
 * 格式：
 * [{ text: '买菜', time: '2022-07-21' }]
 */
const activeLedgerRecords = ref([])

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

function loadLedgerDetail(ledger) {
  console.log(ledger.value)
  activeLedger.value = ledger.value
  axios_plus.get(
      `/ledger/${ledger.value.id}`
  ).then(response => {
    const resp = response.data
    if (resp.code === 'E0001' && undefined !== resp.data) {
      activeLedger.value = resp.data
    }
  })

}

function clickLedgerRecordDetail(ledgerRecord) {
  activeLedgerRecord.value = ledgerRecord
  showLedgerRecordDetail.value = true
}

function addActiveLedgerRecord(ledgerRecord) {
  translateToLedgerRecordView(ledgerRecord)
  activeLedgerRecords.value.unshift(ledgerRecord)
}

function openLedgerSettingPopup(ledger) {
  ledgerSettingOptions.value = {
    show: true,
    type: 'setting',
    ledger
  }
}

const loginDialogStore = LoginDialogStore()
function openCreateLedgerSettingPopup() {
  const userInfo = Taro.getStorageSync(LOCAL_STORAGE_KEYS.user)
  if (undefined === userInfo || '' === userInfo || JSON.stringify(userInfo) === '{}') {
    loginDialogStore.open()
    return
  }

  ledgerSettingOptions.value = {
    show: true,
    type: 'create',
    ledger: {}
  }
}

function loadLedgers() {
  axios_plus.get('/ledger/list')
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

function translateToLedgerRecordView(record) {
  record.text = record.tag
  record.time = dateFormat('mm-dd HH:MM', new Date(record.createTime))
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
          translateToLedgerRecordView(record)
        })
        activeLedgerRecords.value = activeLedgerRecords.value.concat(resp.data)
      }
    }
  })
}

function changeActiveLedger(ledger) {
  console.log('触发变更 active')
  loadLedgerDetail(ledger)
  loadActiveLedgerRecords(ledger.value)
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
  align-items: center;
}

.ledger-desc-item {
  margin: 0 5px;
}
</style>
