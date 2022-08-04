<template>
  <nut-popup position="bottom" :style="{ width: '100%', height: '60%' }" :visible="props.visible" @close="close">
    <div class="user-ledger-tag-setting-wrapper">
      <div class="user-ledger-tag-setting-title-wrapper">
        <!-- 标题 -->
        <div class="user-ledger-tag-setting-title">{{ createLedgerPopupTitle() }}</div>
        <nut-icon v-if="props.type !== 'create'" @click="canFormEdit = !canFormEdit" name="edit" size="15px"
                  :class="{
              'create-ledger-popup-edit': true,
              'can-edit': canFormEdit,
              'cant-edit': !canFormEdit
            }"
        ></nut-icon>
      </div>
      <!-- 标签 -->
      <nut-form class="user-ledger-tag-setting-form" :model-value="form">
        <nut-form-item
            label="标签名称" :required="true"
            :rules="[
              { required: true, message: '请认真填写标签名称, 统计账单时以此为依据' },
              { regex: /^\S+$/, message: '请认真填写标签名称, 统计账单时以此为依据' }
            ]"
        >
          <input class="nut-input-text" placeholder="请输入标签名称" type="text" v-model="form.tag"
                 :disabled="!canFormEdit"/>
        </nut-form-item>

        <nut-form-item
            label="常用标签" :required="true" body-align="right"
            :rules="[
              { required: true, message: '常用标签能被用户快捷选中' },
            ]"
        >
          <nut-switch v-model="form.isDefaultTag" />
        </nut-form-item>
      </nut-form>
      <nut-button style="width: 100%" shape="square" type="primary"
                  @click="submit" :loading="submitLoading"
                  :disabled="!canFormEdit">
        提交
      </nut-button>
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
const emit = defineEmits(['update:visible', 'submit-success'])

function close() {
  initForm()
  emit('update:visible', false)
}

const canFormEdit = ref(true)
const submitLoading = ref(false)

const form = ref({})

function initForm() {
  form.value = {
    tag: '',
    isDefaultTag: false
  }
}

function submit () {
  submitLoading.value = true
  axios_plus.post(
      "/user/ledger/tag",
      form.value
  ).then(response => {
    const resp = response.data
    if (resp.code === 'E0001') {
      emit('submit-success', resp.data)
      close()
    }
  }).finally(() => {
    submitLoading.value = false
  })
}

function createLedgerPopupTitle() {
  if ('create' === props.type) {
    return '新的标签'
  } else if ('setting' === props.type) {
    if (canFormEdit.value) {
      return '修改标签'
    } else {
      return '标签详情'
    }
  }
  return 'error'
}

initForm()
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

</style>
