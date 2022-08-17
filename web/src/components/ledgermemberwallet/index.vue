<template>
  <nut-popup
      class="lmw-popup"
      position="right"
      :style="{ width: '100%', height: '100%' }"
      :visible="props.visible"
      @close="close">
    <div class="lmw-wrapper">
      <div class="lmw-overlay">
        <nut-navbar class="lmw-nav" @on-click-back="close" title="钱包"/>
        <div class="lmw-avatar-tabs">
          <div class="lmw-avatar-tabs-title">钱包分类</div>
          <div class="lmw-avatar-tabs-avatars">
            <nut-avatar @active-avatar="clickLedger">共</nut-avatar>
            <nut-avatar
                v-for="member in members"
                :key="member.id"
                :url="member.avatarUrl"
                @active-avatar="clickMember(member)"
            ></nut-avatar>
          </div>
        </div>
        <nut-cell-group class="lmw-wallet-wrapper">
          <nut-cell title="钱包">
            <template v-slot:link>
              {{ wallet.name }}
            </template>
          </nut-cell>
          <nut-cell title="余额">
            <template v-slot:link>
              <nut-price :price="wallet.amount / 100" size="normal" :need-symbol="false" :thousands="true"/>
            </template>
          </nut-cell>
        </nut-cell-group>
      </div>

      <div class="lmw-records-wrapper" :style="recordsStyle">
        <nut-infiniteloading
            containerId='lmw-records-wrapper'
            :use-window='false'
            :has-more="hasMoreRecords"
            @load-more="loadMoreRecords"
            pull-icon="loading"
            load-more-txt="哎呀，这里是底部了啦">
          <nut-cell-group title="变化记录">
            <nut-cell v-for="(record, index) in records" :key="index"
                      :center="true" :is-link="false">
              <template v-slot:icon>
                <nut-avatar size="small" :icon="record.creator.avatarUrl"></nut-avatar>
              </template>
              <template v-slot:title>
                <div class="ledger-desc">
                  <div class="ledger-desc-item">
                    <div>
                      {{ record.text }}
                      <span :class="{
                            'lmw-font-red': record.amount < 0,
                            'lmw-font-green': record.amount >=0
                            }"
                            style="display: inline-block"
                      >{{ record.amount > 0 ? '+' : '' }}{{ record.amount / 100 }}¥</span>
                    </div>
                    <div>
                      余额
                      <span :class="{
                            'lmw-font-red': record.amount < 0,
                            'lmw-font-green': record.amount >=0
                            }"
                            style="display: inline-block"
                      >{{ record.amount > 0 ? '+' : '' }}{{ record.afterWalletAmount / 100 }}¥</span>
                    </div>
                  </div>
                </div>
              </template>
              <template v-slot:link>
                <div>{{ record.time }}</div>
              </template>
            </nut-cell>
          </nut-cell-group>
        </nut-infiniteloading>
      </div>
    </div>
  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, defineEmits, watch} from 'vue'
import axios_plus from '../../config/axios_plus'
import Taro from '@tarojs/taro'
import {LOCAL_STORAGE_KEYS} from '../../config/local_storage_keys'
import {dateFormat} from '../../util/DateUtil'

defineComponent({
  name: 'LedgerMemberWallet'
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
const emit = defineEmits(['update:visible'])

function close() {
  emit('update:visible', false)
}

const publicWalletName = '共享钱包'
const members = ref([])
const wallet = ref({
  name: publicWalletName,
  amount: 0
})
const records = ref([
  {
    creator: {
      avatarUrl: ''
    },
    text: '',
    amount: 10,
    afterWalletAmount: 10000,
    time: 10000
  }
])
const recordsPage = {
  pages: 0,
  size: 10,
  current: 0
}
const recordsStyle = ref({
  width: '0',
  height: '0'
})

const hasMoreRecords = ref(true)

function initRecords() {
  wallet.value.name = ''
  wallet.value.amount = 0
  hasMoreRecords.value = true
  records.value = []
  recordsPage.pages = 0
  recordsPage.size = 10
  recordsPage.current = 0
}

function loadMoreRecords(done) {
  loadLedgerRecords(done)
}

function clickMember(member, done) {
  initRecords()
  loadMemberWallet(member)
  loadMemberWalletRecords(member, done)
}

function loadMemberWalletRecords(member, done) {
  const page = recordsPage

  axios_plus.get(
      `/ledger/member/wallet/record?ledgerId=${props.ledger.id}&ledgerMemberId=${member.id}&current=${page.current + 1}&size=${page.size}&pages=${page.pages}`
  ).then(({data}) => {
    if (data.code === 'E0001') {
      data.data.forEach(item => {
        item.time = dateFormat('mm-dd HH:MM', new Date(item.createTime))
        item.text = item.tag
      })
      records.value = records.value.concat(data.data)
    }

    recordsPage.pages = data.pages
    recordsPage.size = data.size
    recordsPage.current = data.current
    if (recordsPage.current === recordsPage.pages) {
      hasMoreRecords.value = false
    }
  }).finally(() => {
    if (done) {
      done()
    }
  })
}

function loadMemberWallet(member) {
  axios_plus.get(
      `/ledger/member/wallet/one?ledgerId=${props.ledger.id}&memberId=${member.id}`
  ).then(({data}) => {
    if (data.code === 'E0001') {
      wallet.value.name = member.nickname + ' 的钱包'
      wallet.value.amount = data.data.amount
    }
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

function clickLedger() {
  initRecords()
  loadLedgerDetail()
  loadLedgerRecords()
}

function loadLedgerDetail() {
  axios_plus.get(
      `/ledger/${props.ledger.id}`
  ).then(({data}) => {
    wallet.value.name = publicWalletName
    wallet.value.amount = data.data.amount
  })
}

function loadLedgerRecords(done) {
  const page = recordsPage
  axios_plus.get(
      `/ledger/record?ledgerId=${props.ledger.id}&current=${page.current + 1}&size=${page.size}&pages=${page.pages}`
  ).then(({data}) => {
    if ('E0001' === data.code) {
      if (data.data === undefined || data.data === null || data.data.length === 0) {
        records.value = []
      } else {
        data.data.forEach(item => {
          item.time = dateFormat('mm-dd HH:MM', new Date(item.createTime))
          item.text = item.tag
        })
        records.value = records.value.concat(data.data)
      }

      recordsPage.pages = data.pages
      recordsPage.size = data.size
      recordsPage.current = data.current
      if (recordsPage.current === recordsPage.pages) {
        hasMoreRecords.value = false
      }
    }
  }).finally(() => {
    if (done) {
      done()
    }
  })
}

let getDomSuccess = false

function computeRecordsStyle() {
  const systemInfo = Taro.getSystemInfoSync()
  const windowWidth = systemInfo.windowWidth
  const windowHeight = systemInfo.windowHeight

  Taro.createSelectorQuery().select('.lmw-wrapper .lmw-overlay')
      .boundingClientRect()
      .exec(overlays => {
        const overlayHeight = overlays[0].height
        recordsStyle.value.width = windowWidth + 'px'
        recordsStyle.value.height = windowHeight - overlayHeight + 'px'
      })
}

watch(props, (newVal, oldVal) => {
  initRecords()
  clickLedger()
  loadMembers()
  // 计算dom
  const intervalId = setInterval(() => {
    let getDomCount = 0
    do {
      try {
        computeRecordsStyle();

        getDomSuccess = true
      } catch (ignore) {
      }
    } while (!getDomSuccess && getDomCount++ < 255)
    clearInterval(intervalId)
  }, 50)
})
</script>

<style lang="scss">
.lmw-popup {
}

.lmw-popup .nut-navbar__title .title {
  min-width: 0;
}

.lmw-wrapper {
  display: flex;
  flex-direction: column;
}

.lmw-avatar-tabs {
  display: flex;
  align-items: center;
  margin: 0 10px;
}

.lmw-avatar-tabs .lmw-avatar-tabs-title {
  font-weight: bold;
  font-size: 18px;
}

.lmw-avatar-tabs .lmw-avatar-tabs-avatars {
  display: flex;
  flex-grow: 1;
  justify-content: space-around;
}

.lmw-font-red {
  color: #ff2121
}

.lmw-font-green {
  color: #00bc12
}

</style>
