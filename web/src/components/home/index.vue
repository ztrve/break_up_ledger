<template>
  <div class="home-wrapper">
    <div class="empty-wrapper" v-if="ledgers.length === 0">
      <nut-empty image="network" description="你不会还没有账本吧?">
        <template v-slot:default>
          <div style="margin-top: 10px">
            <nut-button icon="add" type="primary" @click="showCreateLedgerPopup = true">新建账本</nut-button>
          </div>
        </template>
      </nut-empty>
    </div>
    <div class="ledger-wrapper" v-else>
      <nut-navbar :left-show="false">
        <template #content>
          <div class="home-title">
            {{ activeLedger }}
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
      <home-menu></home-menu>
    </nut-popup>

    <!-- 创建账本弹出 -->
    <nut-popup class="home-menu-wrapper"
               position="bottom"
               :style="{ width: '100%', height: '50%' }"
               v-model:visible="showCreateLedgerPopup">
      <div class="create-ledger-popup-wrapper">
        <div class="create-ledger-popup-title">新建账本</div>
        <nut-form :model-value="createLedgerFormData">
          <nut-form-item label="账本名称">
            <input class="nut-input-text" placeholder="请输入账本名称" type="text" v-model="createLedgerFormData.name"/>
          </nut-form-item>
          <nut-form-item label="所有成员可以提交账单" body-align="right">
            <nut-switch v-model="createLedgerFormData.canMemberCommit"></nut-switch>
          </nut-form-item>
          <!--    TODO 请输入表单    -->
        </nut-form>

      </div>
    </nut-popup>
  </div>
</template>

<script setup>
import {defineComponent, onMounted, ref} from 'vue';
import LedgerDetail from '../ledgerdetail'
import HomeMenu from '/src/components/homemenu'

defineComponent({
  name: 'Home'
})
const ledgers = ref([
  // {value: 0, text: '账单1'},
  // {value: 1, text: '账单2'},
])
const activeLedger = ref("ledgers.value[0].text")
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

const createLedgerFormData = ref({
  name: '',
  canMemberCommit: true
})

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

.create-ledger-popup-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  width: 100%;
  text-align: center;
  margin: 10px 0;
}
</style>
