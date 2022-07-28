<template>
  <view class="notices-wrapper">
    <nut-cell-group>
      <nut-cell v-for="(notice, index) in notices" :key="index" :is-link="true" :center="true"
        @click="openNoticeDetail(notice)">
        <template v-slot:icon>
          <nut-avatar size="normal" :class="{
            'notice-user-avatar': true,
            'is-deal': notice.isDeal
          }"
            icon="https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png">
          </nut-avatar>
        </template>

        <template v-slot:title>
          <div :class="{
            'notice-desc': true,
            'is-deal': notice.isDeal
          }">
            <div class="notice-desc-item title">{{ notice.ledgerName }}</div>
            <div class="notice-desc-item sub-title">{{ notice.noticeTitle }}</div>
          </div>
        </template>

        <template v-slot:link>
          <div class="notice-link">
            <div class="date">{{ notice.date }}</div>
            <nut-icon name="right"></nut-icon>
            <img v-if="notice.isDeal" class="deal-img" :src="require('../../../assets/deal-img.png')">
          </div>
        </template>
      </nut-cell>
    </nut-cell-group>

    <notice-detail v-model:visible="noticeDetailFlag" :notice="activeNotice"></notice-detail>
  </view>
</template>

<script setup>
import { defineComponent, ref } from 'vue';
import NoticeDetail from '../noticedetail'

defineComponent({
  name: 'Notices'
})

const notices = ref([
  { ledgerId: 1, ledgerName: '账单1', noticeId: 1, noticeTitle: '配置AAA从开启修改到关闭修改到关闭修改到关闭修改到关闭dddddddd', date: '07-21', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: true },
  { ledgerId: 1, ledgerName: '账单3', noticeId: 1, noticeTitle: '配置BBB从开启', date: '07-20', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: false },
  { ledgerId: 1, ledgerName: '账单2', noticeId: 1, noticeTitle: '配置CCC从开启修改到关闭dddddd', date: '07-19', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: true },
  { ledgerId: 1, ledgerName: '账单2', noticeId: 1, noticeTitle: '配置BBB从开启修改到关闭dddddd', date: '07-18', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: false },
  { ledgerId: 1, ledgerName: '账单2', noticeId: 1, noticeTitle: '配置AAA从开启修改到关闭dddddd', date: '07-18', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: true },
  { ledgerId: 1, ledgerName: '账单3', noticeId: 1, noticeTitle: '配置DDD从开启修改到关闭dddddd', date: '07-18', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: false },
  { ledgerId: 1, ledgerName: '账单4', noticeId: 1, noticeTitle: '配置AAA从开启修改到关闭dddddd', date: '07-17', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: false },
  { ledgerId: 1, ledgerName: '账单4', noticeId: 1, noticeTitle: '配置BBB从开启修改到关闭dddddd', date: '07-17', initiatorUserId: 1, initiatorAvatarUrl: 'https://img12.360buyimg.com/imagetools/jfs/t1/143702/31/16654/116794/5fc6f541Edebf8a57/4138097748889987.png', initiatorName: '张三', isDeal: false },
])
const noticeDetailFlag = ref(false)

const activeNotice = ref(undefined)

function openNoticeDetail(notice) {
  noticeDetailFlag.value = true
  activeNotice.value = notice
}
</script>

<style lang="scss">
.notices-wrapper {
  height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}

.notice-user-avatar {
  margin-right: 10px;
}

.notice-desc {
  display: flex;
  flex-direction: column;
}

.notice-desc-item {
  margin: 2px;
}

.notice-desc-item.title {
  font-weight: bold;
}

.notice-desc-item.sub-title {
  width: 190px;
  font-size: 16px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.notice-link {
  color: rgba(0, 0, 0, 0.25);
  display: flex;
  flex-direction: row;
}

.deal-img {
  position: absolute;
  bottom: 0;
  right: 0;
  width: auto;
  height: 100%;
  aspect-ratio: 1/1;
}

.is-deal {
  opacity: .5;
}
</style>
