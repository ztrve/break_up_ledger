<template>
  <!-- 配置 账单记录 -->
  <nut-popup class="ledger-record-setting-wrapper"
             position="bottom"
             :style="{ width: '100%', height: 'auto' }"
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
          <input class="nut-input-text" placeholder="账本名称" type="text" v-model="props.ledger.name"
                 :disabled="true"/>
        </nut-form-item>
        <nut-form-item
            label="账单标签" :required="true"
            :rules="[
              { required: true, message: '请选择账单标签, 统计账单时以此为依据' },
              { regex: /^\S+$/, message: '请选择账单标签, 统计账单时以此为依据' }
            ]"
        >
          <input class="nut-input-text" placeholder="请选择账本标签" type="text" v-model="form.tag" :disabled="true"/>
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
            <nut-picker
                v-model:value="tagPickerValue"
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
        <nut-form-item label="账单备注">
          <input class="nut-input-text" placeholder="请输入账本备注" type="text" v-model="props.ledger.extra"/>
        </nut-form-item>
      </nut-form>
      <nut-button style="width: 100%;" shape="square" type="primary"
                  :loading="commitLoading"
                  @click="commit">
        提交
      </nut-button>
    </div>
  </nut-popup>
</template>

<script setup>
import {defineComponent, defineProps, ref, defineEmits} from 'vue';
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'LedgerRecordSetting'
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
  },
  ledger: {
    type: Object,
    required: true,
    default: {
      id: 0,
      name: ''
    },
  },
})
// 控制弹出框开关
const emit = defineEmits(['update:visible', 'submit-success'])

function close() {
  initForm()
  emit('update:visible', false)
}

const canFormEdit = ref(true)
const showTagPicker = ref(false)
const commitLoading = ref(false)

const form = ref({})
const defaultTags = ref([])
const notDefaultTags = ref([])
const tagPickerColumns = ref([])
const tagPickerValue = ref('')

function initForm() {
  form.value = {
    amount: 0,
    tag: '',
    extra: '',
  }
}

function commit () {
  commitLoading.value = true
  console.log(props.ledger.id)
  axios_plus.post("/ledger/record", {
    ledgerId: props.ledger.id,
    amount: form.value.amount,
    tag: form.value.tag,
    extra: form.value.extra,
  }).then(response => {
    const resp = response.data
    if (resp.code === 'E0001') {
      emit('submit-success', resp.data)
      close()
    }
  }).finally(() => {
    commitLoading.value = false
  })
}

function confirmTagPicker (val) {
  if (undefined === val.selectedValue[0]) {
    form.value.tag = tagPickerColumns.value[0].value
  } else {
    form.value.tag = val.selectedValue[0]
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
        resp.data.forEach((tag, index) => {
          if (tag.isDefaultTag) {
            defaultTags.value.push(tag)
          } else {
            notDefaultTags.value.push(tag)
          }
          if (index === 0) {
            tagPickerValue.value = tag.tag
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
