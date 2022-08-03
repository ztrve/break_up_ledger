<template>
  <!-- 配置 账单记录 -->
<!--  TODO 页面修改-->
  <nut-popup class="ledger-record-setting-wrapper"
             position="bottom"
             :style="{ width: '100%', height: '60%' }"
             :visible="props.visible"
             @close="close"
  >
    <div class="ledger-record-setting-wrapper">
      <div class="ledger-record-setting-title-wrapper">
        <!-- 标题 -->
        <div class="ledger-record-setting-title">{{ createLedgerPopupTitle() }}</div>
        <nut-icon v-if="props.type !== 'create'" @click="canFormEdit = !canFormEdit" name="edit" size="15px"
                  :class="{
              'create-ledger-popup-edit': true,
              'can-edit': canFormEdit,
              'cant-edit': !canFormEdit
            }"
        ></nut-icon>
      </div>
      <!-- 账本表单 -->
      <nut-form class="ledger-record-setting-form" :model-value="form">
        <nut-form-item label="账本名称">
          <input class="nut-input-text" placeholder="请输入账本名称" type="text" v-model="props.ledger.name"
                 :disabled="true"/>
        </nut-form-item>
        <nut-form-item
            label="账单标签" :required="true"
            :rules="[
              { required: true, message: '请选择账单标签, 统计账单时以此为依据' },
              { regex: /^\S+$/, message: '请选择账单标签, 统计账单时以此为依据' }
            ]"
        >
          <input class="nut-input-text" placeholder="请输入账本名称" type="text" v-model="form.tag" :disabled="true"/>
        </nut-form-item>
        <nut-form-item
            label="默认标签"
        >
          <div class="ledger-record-setting-form-tag-wrapper">
            <nut-tag class="ledger-record-setting-form-tag" type="primary" :plain="true"
                     v-for="tag in defaultTags" :key="tag.id"
                     @click="form.tag = tag.tag"
            >
              {{ tag.tag }}
            </nut-tag>
            <nut-tag class="ledger-record-setting-form-tag" type="primary"
                     @click="showTagPicker = true">
              ...
            </nut-tag>
            <nut-tag class="ledger-record-setting-form-tag" type="primary" :round="true"
                     @click="">
              +
            </nut-tag>
            <nut-picker
                v-model:visible="showTagPicker"
                :columns="tagPickerColumns"
                title="账单标签"
                @confirm="confirmTagPicker"
            >
              <template #default>
                <nut-button style="width: 100%" @click="" shape="square" type="primary">没有想要的? 新建一个吧</nut-button>
              </template>
            </nut-picker>
          </div>
        </nut-form-item>

        <nut-form-item label="账本名称">
          <input class="nut-input-text" placeholder="请输入备注" type="text" v-model="form.extra"/>
        </nut-form-item>
      </nut-form>
    </div>
  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, defineEmits} from 'vue';
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'UserLedgerSetting'
})

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
    default: false
  },
  // 页面类型: create新建 setting配置
  type: {
    type: String,
    default: 'create',
    required: true,
    validator(value) {
      return 'create' === value || 'setting' === value
    }
  }
})
// 控制弹出框开关
const emit = defineEmits(['update:visible'])

function close() {
  emit('update:visible', false)
}

const canFormEdit = ref(true)
const showTagPicker = ref(false)

const form = ref({})
const defaultTags = ref([])
const notDefaultTags = ref([])
const tagPickerColumns = ref([])

function confirmTagPicker (tag) {
  form.value.tag = tag
}

function initForm() {
  form.value = {
    ledgerId: 0,
    amount: 0,
    tag: '',
    extra: '',
  }
}

function createLedgerPopupTitle() {
  if ('create' === props.type) {
    return '记一笔'
  } else if ('setting' === props.type) {
    if (canFormEdit.value) {
      return '修改账单'
    } else {
      return '账单详情'
    }
  }
  return 'error'
}

function loadMyLedgerTag() {
  defaultTags.value = []
  notDefaultTags.value = []
  axios_plus.get(
      '/user/ledger/tag/list'
  ).then(response => {
    const resp = response.data
    if ('E0001' === resp.code) {
      if (resp.data) {
        resp.data.forEach(tag => {
          if (tag.isDefaultTag) {
            defaultTags.value.push(tag)
          } else {
            notDefaultTags.value.push(tag)
          }
          tagPickerColumns.value.push({
            text: tag.tag,
            value: tag.tag
          })
        })
        defaultTags.value = resp.data
      }
    }
  })
}

initForm()
loadMyLedgerTag()
</script>

<style lang="scss">
.ledger-record-setting-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.ledger-record-setting-title-wrapper {
  width: 100%;
  margin: 10px 0;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-end;
}

.ledger-record-setting-title {
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.ledger-record-setting-form-tag-wrapper {
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.ledger-record-setting-form-tag {
  margin: 2px 2px;
}

</style>
