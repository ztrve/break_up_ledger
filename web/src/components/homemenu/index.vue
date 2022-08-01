<template>
  <nut-cell-group title="账单列表" style="position: relative">
    <nut-cell v-for="(ledger, index) in ledgers" :title="ledger.name"
              :class="{
                'home-menu-active-ledger-title': ledger.id === activeLedger.id
              }"
              @click="changeActiveLedger(index)"
    ></nut-cell>
    <nut-cell class="home-menu-create-new-ledger-title" icon="plus" title="新建账单" @click="createNewLedger"></nut-cell>
  </nut-cell-group>
</template>

<script setup>
import {defineComponent, defineProps, ref, watch, defineEmits} from 'vue';

defineComponent({
  name: 'HomeMenu'
})

const props = defineProps({
  ledgers: {
    type: Array
  }
})
const emit = defineEmits(['change', 'create-new-ledger'])

const activeLedger = ref({})

function changeActiveLedger(ledgerIndex) {
  activeLedger.value = props.ledgers[ledgerIndex]
  emit('change', activeLedger)
}

function createNewLedger() {
  emit('create-new-ledger')
}

watch(props, () => {
  changeActiveLedger(0)
})

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
}
</style>
