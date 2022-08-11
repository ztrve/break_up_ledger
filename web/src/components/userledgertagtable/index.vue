<template>
  <nut-popup position="right" :style="{ width: '100%', height: '100%' }" :visible="props.visible" @close="close">
    <div class="user-ledger-tag-table-wrapper">
      <nut-navbar class="" @on-click-back="close" title="标签配置"/>
      <div class="tag-table-title">
        <div title="tag">标签</div>
        <div title="default-tag">默认标签</div>
      </div>
      <nut-form class="user-ledger-tag-table" :model-value="tags"
                :style="{
                  height: tableHeight
                }"
      >
        <nut-form-item
            v-for="tag in tags"
            :label="tag.tag"
            body-align="right"
        >
          <nut-switch v-model="tag.isDefaultTag" active-text="开" @change="changeIsDefaultTagSwitch(tag)"/>
        </nut-form-item>
      </nut-form>

      <nut-button
          class="user-ledger-tag-table-submit" type="primary" shape="square"
          :loading="submitLoading" @click="submit"
      >
        保存
      </nut-button>
    </div>

  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, defineEmits, onMounted, watch} from 'vue';
import axios_plus from "../../config/axios_plus";
import Taro from "@tarojs/taro";

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

const submitLoading = ref(false)
const tags = ref([])
const tableHeight = ref('0')

function submit() {
  submitLoading.value = true
  // 校验
  const defaultTags = tags.value.filter(item => item.isDefaultTag)
  if (defaultTags.length > 5) {
    Taro.showToast({title: '最多五个默认标签', icon: 'none'})
    submitLoading.value = false
    return
  }

  // 发送请求
  axios_plus.put(
      '/user/ledger/tag', defaultTags
  ).then(({ data }) => {
  }).finally(() => {
    submitLoading.value = false
  })
}

function changeIsDefaultTagSwitch(tag) {
  const defaultTags = tags.value.filter(item => item.isDefaultTag)
  if (defaultTags.length > 5) {
    Taro.showToast({title: '最多五个默认标签', icon: 'none'})
    tag.isDefaultTag = false
  }
}

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

function computeTableHeight() {
  Taro.nextTick(() => {
    Taro.createSelectorQuery().select('.user-ledger-tag-table-wrapper')
        .boundingClientRect()
        .exec(wrappers => {
          const wrapperHeight = wrappers[0].height
          Taro.createSelectorQuery().select('.user-ledger-tag-table-wrapper > .tag-table-title')
              .boundingClientRect()
              .exec(titles => {
                const titleHeight = titles[0].height
                Taro.createSelectorQuery().select('.user-ledger-tag-table-wrapper > .user-ledger-tag-table-submit')
                    .boundingClientRect()
                    .exec(submits => {
                      const submitHeight = submits[0].height
                      tableHeight.value = wrapperHeight - titleHeight - submitHeight + 'px'
                    })
              })

        })
  })
}

loadMyLedgerTag()

watch(props, (newVal, oldVal) => {
  if (newVal.visible && tableHeight.value === '0') {
    computeTableHeight()
  }
})
</script>

<style lang="scss">
.user-ledger-tag-table-wrapper {
  display: flex;
  overflow: hidden;
  flex-direction: column;
  height: 100%;
}


.user-ledger-tag-table {
  overflow-y: auto;
  overflow-x: hidden;
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
