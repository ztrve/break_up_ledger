<template>
  <nut-popup class="notice-detail-wrapper" :style="{ width: '100%', height: '100%' }" position="right"
             v-model:visible="props.visible">
    <div>
      <nut-navbar @on-click-back="close" title="通知详情"/>
      <nut-cell-group>
        <nut-cell title="发起人" :center="true">
          <template v-slot:link>
            <nut-avatar size="small" :class="{
                            'notice-user-avatar': true,
                        }" :icon="props.notice.initiator.avatarUrl"/>
            {{ props.notice.initiator.nickname }}
          </template>
        </nut-cell>
        <nut-cell title="主题" :center="true">
          <template v-slot:link>
            {{ props.notice.noticeName }}
          </template>
        </nut-cell>
        <nut-cell :center="true">
          <template v-slot:title>
            <div class="notice-detail-initiate-content-title">
              详情
            </div>
          </template>
          <template v-slot:link>
            <div class="notice-detail-initiate-content-link">
              {{ props.notice.noticeMsg }}
            </div>
          </template>
        </nut-cell>
        <nut-cell title="发起日期" :center="true">
          <template v-slot:link>
            {{ defaultDateFormat(new Date(props.notice.createTime)) }}
          </template>
        </nut-cell>
        <nut-cell title="处理状态" :center="true">
          <template v-slot:link>
            <nut-tag v-if="props.notice.dealStatus === 0" color="#E9E9E9" textColor="#999999">待处理</nut-tag>
            <nut-tag v-else type="success">已处理</nut-tag>
          </template>
        </nut-cell>
        <nut-cell v-if="props.notice.dealStatus !== 0" title="处理结果" :center="true">
          <template v-slot:link>
            <nut-tag v-if="props.notice.dealResult === 0" type="danger">拒绝</nut-tag>
            <nut-tag v-else type="success">同意</nut-tag>
          </template>
        </nut-cell>
      </nut-cell-group>
      <div class="notice-detail-operate" v-if="props.notice.dealStatus === 0">
        <nut-button class="notice-detail-operate-item" shape="square" type="danger"
                    @click="reject" :loading="rejectLoading">
          拒绝
        </nut-button>
        <nut-button class="notice-detail-operate-item" shape="square" type="primary"
                    @click="allow" :loading="allowLoading">
          同意
        </nut-button>
      </div>
    </div>
  </nut-popup>
</template>

<script setup>
import Taro from '@tarojs/taro'
import {defineComponent, defineProps, defineEmits, ref} from 'vue';
import {defaultDateFormat} from "../../util/DateUtil";
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'NoticeDetail'
})

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
    default: false
  },
  notice: {
    type: Object,
    default: {
      "id": 0,
      "noticeType": "",
      "noticeName": "",
      "noticeMsg": "",
      "noticeData": "",
      "initiatorId": 0,
      "handlerId": 0,
      "dealStatus": 0,
      "updateTime": "",
      "createTime": "",
      "initiator": {
        "id": 0,
        "code": "",
        "nickname": "",
        "avatarUrl": "",
        "phone": null
      }
    }
  }
})

const emit = defineEmits(['update:visible', 'deal'])

function close() {
  emit('update:visible', false)
}

const rejectLoading = ref(false)
const allowLoading = ref(false)

/**
 * 处理结果
 *
 * @param dealResult 0拒绝 1同意
 * @param responseCallback 请求结果回调
 */
function dealNotice(dealResult, responseCallback) {
  axios_plus.post("/notice/deal", {
    dealResult: dealResult,
    noticeId: props.notice.id
  }).then(() => {
    Taro.showToast({
      title: '处理完成',
      icon: 'success',
    })
    emit('deal')
  }).catch(() => {
  }).finally(() => {
    responseCallback()
    close()
  })
}

function reject() {
  rejectLoading.value = true
  dealNotice(0, () => {
    rejectLoading.value = false
  })
}

function allow() {
  allowLoading.value = true
  dealNotice(1, () => {
    allowLoading.value = false
  })
}

</script>

<style lang="scss">
// .notice-detail-wrapper {
// }

.notice-detail-operate {
  position: absolute;
  bottom: 0;
  width: 100%;
  display: flex;
  flex-direction: row;
}

.notice-detail-operate > .notice-detail-operate-item {
  flex: 1;
}

.notice-detail-initiate-content-title {
  flex: 1;
}

.notice-detail-initiate-content-link {
  flex: 3;
  text-align: right;
}
</style>
