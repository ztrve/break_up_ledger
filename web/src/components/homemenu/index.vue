<template>
  <nut-cell-group>
    <nut-cell title="账单列表">
    </nut-cell>
  </nut-cell-group>
</template>

<script setup>
import { defineComponent, defineProps, ref } from 'vue';

defineComponent({
  name: 'HomeMenu'
})

const props = defineProps({

})


</script>

<style lang="scss">
// .home-menu-wrapper {
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
