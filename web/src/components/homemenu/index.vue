<template>
  <nut-popup class="home-menu-wrapper"
             position="right"
             :style="{ width: '60%', height: '100%' }"
             :visible="props.visible"
             @close="close"
  >
    <nut-cell-group>
      <template #title>
          <div class="nut-cell-group__title my-ledgers-group-title-wrapper">
            <div>我的账本</div>
            <div class="home-menu-create-new-ledger-title" @click="clickCreateNewLedger">
              <nut-icon name="plus"></nut-icon>
              新建账本
            </div>

          </div>
      </template>

      <nut-cell v-for="(ledger, index) in ledgers" :title="ledger.name"
                :class="{
                'home-menu-active-ledger-title': ledger.id === activeLedger.id
              }"
                @click="clickLedger(index)"
      ></nut-cell>
<!--      <nut-cell class="home-menu-create-new-ledger-title" icon="plus" title="新建账本"-->
<!--                @click="clickCreateNewLedger"></nut-cell>-->
    </nut-cell-group>
    <nut-cell-group title="当前账本">
      <nut-cell :icon="require('../../../assets/wallet.png')" title="账本钱包" is-link :center="true" @click="clickSetting"></nut-cell>
      <nut-cell icon="setting" title="账本配置" is-link :center="true" @click="clickSetting"></nut-cell>
    </nut-cell-group>
  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, watch, defineEmits, onMounted} from 'vue';

defineComponent({
  name: 'HomeMenu'
})

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  ledgers: {
    type: Array
  }
})
const emit = defineEmits(['update:visible', 'click-change', 'click-create-ledger', 'click-setting'])

function close() {
  emit('update:visible', false)
}


const activeLedger = ref({})
const waitingOperateLedger = ref({})

function clickLedger(ledgerIndex) {
  waitingOperateLedger.value = props.ledgers[ledgerIndex]
  changeActiveLedger()
}

function changeActiveLedger() {
  activeLedger.value = waitingOperateLedger.value
  emit('click-change', activeLedger)
}

function clickCreateNewLedger() {
  emit('click-create-ledger')
}

function clickSetting() {
  emit('click-setting', waitingOperateLedger)
}

function initHomeMenu() {
  if (props.ledgers.length > 0) {
    waitingOperateLedger.value = props.ledgers[0]
    changeActiveLedger()
  }
}

// 统计初始化页面时，函数执行的次数
let initCount = 0
watch(props, (newVal, oldValue) => {
  if (initCount === 0) {
    initCount++
    initHomeMenu()
  }
})

initHomeMenu()
</script>

<style lang="scss">
// .home-menu-wrapper {
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

.home-menu-active-ledger-title {
  color: #4d90fe;
  font-weight: bold;
}

.home-menu-create-new-ledger-title {
  color: #07c160;
  display: flex;
  justify-content: center;
}

.ledger-options-wrapper {
  display: flex;
  flex-direction: column;
}

.ledger-options-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  display: flex;
  justify-content: center;
  padding: 10px 0;
}

.home-wrapper .nut-popup .nutui-popup__content-wrapper {
  height: auto;
}

.my-ledgers-group-title-wrapper {
  display: flex;
  justify-content: space-between;
}
</style>
