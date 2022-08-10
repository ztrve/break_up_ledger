<template>
  <nut-popup position="right" :style="{ width: '100%', height: '100%' }" :visible="props.visible" @close="close">
    <nut-navbar @on-click-back="close" title="标签配置" />
    <div class="tag-table-title">
      <div title="tag">标签</div>
      <div title="default-tag">默认标签</div>
    </div>

    <nut-form class="user-ledger-tag-setting-form" :model-value="tags">
      <nut-form-item
          v-for="tag in tags"
          :label="tag.tag"
          body-align="right"
      >
        <nut-switch v-model="tag.isDefaultTag" active-text="开" />
      </nut-form-item>
    </nut-form>
    <nut-button>保存</nut-button>
  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, defineEmits} from 'vue';
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'UserLedgerTagTable'
})

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
    default: false
  }
})
// 控制弹出框开关
const emit = defineEmits(['update:visible'])

function close() {
  emit('update:visible', false)
}

const tags = ref([])

function loadMyLedgerTag() {
  tags.value = []
  axios_plus.get(
      '/user/ledger/tag/list'
  ).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      if (resp.data) {
        tags.value = resp.data
      }
    }
  })
}

loadMyLedgerTag()
</script>

<style lang="scss">
.user-ledger-tag-setting-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.user-ledger-tag-setting-title-wrapper {
  width: 100%;
  margin: 10px 0;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-end;
}

.user-ledger-tag-setting-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.user-ledger-tag-setting-tag-wrapper {
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.user-ledger-tag-setting-form {
  flex-grow: 1;
}

.user-ledger-tag-setting-tag {
  margin: 2px 2px;
}

.tag-table-title {
  display: flex;
  flex-direction: row;
  margin: 0 20px;
  justify-content: space-between;
  font-weight: bold;
}

.tag-table-title > .tag {
  text-align: left;
}

.tag-table-title > .default-tag {
  text-align: right;
}
</style>
