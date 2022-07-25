<template>
    <nut-popup class="notice-detail-wrapper" :style="{ width: '100%', height: '100%' }" position="right"
        v-model:visible="props.visible">
        <div>
            <nut-navbar @on-click-back="close" title="通知详情" />
            <nut-cell-group>
                <nut-cell title="发起人" :center="true">
                    <template v-slot:link>
                        <nut-avatar size="small" :class="{
                            'notice-user-avatar': true,
                        }" :icon="props.notice.initiatorAvatarUrl" />
                        {{ props.notice.initiatorName }}
                    </template>
                </nut-cell>
                <nut-cell title="所属账单" :center="true">
                    <template v-slot:link>
                        {{ props.notice.ledgerName }}
                    </template>
                </nut-cell>
                <nut-cell :center="true">
                    <template v-slot:title>
                        <div class="notice-detail-initiate-content-title">
                            发起内容
                        </div>
                    </template>
                    <template v-slot:link>
                        <div class="notice-detail-initiate-content-link">
                            {{ props.notice.noticeTitle }}
                        </div>
                    </template>
                </nut-cell>
                <nut-cell title="发起日期" :center="true">
                    <template v-slot:link>
                        {{ props.notice.date }}
                    </template>
                </nut-cell>
                <nut-cell title="处理状态" :center="true">
                    <template v-slot:link>
                        <nut-tag v-if="props.notice.isDeal" type="success">已处理</nut-tag>
                        <nut-tag v-else color="#E9E9E9" textColor="#999999">待处理</nut-tag>
                    </template>
                </nut-cell>
            </nut-cell-group>
            <div class="notice-detail-operate">
                <nut-button class="notice-detail-operate-item" shape="square" type="danger" @click="reject"
                    :loading="rejectLoading">
                    拒绝
                </nut-button>
                <nut-button class="notice-detail-operate-item" shape="square" type="primary" @click="allow"
                    :loading="allowLoading">
                    同意
                </nut-button>
            </div>
        </div>
    </nut-popup>
</template>

<script setup>
import Taro from '@tarojs/taro'
import { defineComponent, defineProps, defineEmits, ref } from 'vue';

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
            ledgerId: 1,
            ledgerName: '账单1',
            noticeId: 1,
            noticeTitle: '配置AAA从开启修改到关闭',
            initiatorUserId: 1,
            initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png',
            initiatorName: '张三',
            date: '07-21',
            isDeal: true
        }
    }
})

const emit = defineEmits(['update:visible'])
function close() {
    emit('update:visible', false)
}
const rejectLoading = ref(false)
const allowLoading = ref(false)

function reject() {
    rejectLoading.value = true
    setTimeout(() => {
        rejectLoading.value = false
        Taro.showToast({
            title: 'success',
            icon: 'success',
        })
        close()
    }, 2000);
}

function allow() {
    allowLoading.value = true
    setTimeout(() => {
        allowLoading.value = false
        Taro.showToast({
            title: 'success',
            icon: 'success',
        })
        close()
    }, 2000);
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

.notice-detail-operate>.notice-detail-operate-item {
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
