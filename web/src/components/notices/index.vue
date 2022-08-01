<template>
  <view class="notices-wrapper" id="notices-wrapper">
    <!-- 空 -->
    <nut-empty v-if="undefined === notices || notices.length === 0" image="error">
      <template v-slot:description>
        <p>您还没有通知</p>
      </template>
    </nut-empty>
    <!-- 通知列表 -->

    <nut-infiniteloading
        v-else
        containerId='notices-wrapper'
        :use-window='false'
        :has-more="loadNoticesPageDeal"
        @load-more="loadNoticesPage"
        pull-icon="loading"
        load-more-txt="哎呀，这里是底部了啦"
    >
      <nut-cell-group>
        <nut-cell v-for="(notice, index) in notices" :key="index" :is-link="true" :center="true"
                  @click="openNoticeDetail(notice)">
          <template v-slot:icon>
            <nut-avatar size="normal" :class="{
            'notice-user-avatar': true,
            'is-deal': notice.dealStatus !== 0
          }"
                        :icon="notice.initiator.avatarUrl">
            </nut-avatar>
          </template>

          <template v-slot:title>
            <div :class="{
            'notice-desc': true,
            'is-deal': notice.dealStatus !== 0
          }">
              <div class="notice-desc-item title">{{ notice.noticeName }}</div>
              <div class="notice-desc-item sub-title">{{ notice.noticeMsg }}</div>
            </div>
          </template>

          <template v-slot:link>
            <div class="notice-link">
              <div class="date">{{ notice.createTime.substring(5, 10) }}</div>
              <nut-icon name="right"></nut-icon>
              <img v-if="notice.dealStatus !== 0" class="deal-img" :src="require('../../../assets/deal-img.png')">
            </div>
          </template>
        </nut-cell>
      </nut-cell-group>
    </nut-infiniteloading>

    <notice-detail v-model:visible="noticeDetailFlag" :notice="activeNotice" @deal="updateNotice"></notice-detail>
  </view>
</template>

<script setup>
import {defineComponent, ref} from 'vue';
import NoticeDetail from '../noticedetail'
import axios_plus from "../../config/axios_plus";

defineComponent({
  name: 'Notices'
})

const notices = ref([])
const noticePage = {
  pages: 0,
  size: 10,
  current: 0
}
function initNotices () {
  notices.value = []
  noticePage.pages = 0
  noticePage.size = 10
  noticePage.current = 0
}

const loadNoticesPageDeal = ref(true)

const noticeDetailFlag = ref(false)

const activeNotice = ref(undefined)

function openNoticeDetail(notice) {
  noticeDetailFlag.value = true
  activeNotice.value = notice
}

function loadNoticesPage(done) {
  console.log('load')
  axios_plus.get(
      `/notice?pages=${noticePage.pages}&size=${noticePage.size}&current=${noticePage.current + 1}`
  ).then(resp => {
    if (resp.data.code === 'E0001') {
      console.log(resp.data)
      const data = resp.data.data
      if (undefined !== data && null !== data && data.length > 0) {
        notices.value = notices.value.concat(data)
      }
      noticePage.pages = resp.data.pages
      noticePage.size = resp.data.size
      noticePage.current = resp.data.current

      if (noticePage.current === noticePage.pages) {
        loadNoticesPageDeal.value = false
      }
    }
  }).catch(() => {
  }).finally(() => {
    done()
  })
}

function updateNotice() {
  console.log('Notice page catch deal')
  initNotices()
  loadNoticesPage()
}

loadNoticesPage()
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
