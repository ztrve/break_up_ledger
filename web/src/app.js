import { createApp } from 'vue'
import {
  Button, Toast, Tabbar, TabbarItem, Icon, Avatar, AvatarGroup, Empty,
  Cell, CellGroup, Menu, MenuItem, OverLay, Popup, Tag, Navbar, Tabs, TabPane,
  Price, Collapse, CollapseItem, Divider, Ellipsis
} from '@nutui/nutui-taro';
import { createPinia } from 'pinia'
import Taro from '@tarojs/taro'

import './app.styl'

const App = createApp({
  mounted() {

    console.log('app.js mounted');
  },
  // 对应 onLaunch
  onLaunch() {
    console.log('app.js onLaunch');
  },
  // 对应 onShow
  onShow(options) {
    console.log('app.js onShow');
    Taro.checkSession({
      success: resp => {
        console.log(resp)
        Taro.login({
          success: function (resp) {
            if (resp.code) {
              //发起网络请求
              console.log('发起网络请求');
              console.log(resp);
              Taro.getUserProfile({
                desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
                success: (res) => {
                  // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
                  console.log(res);
                  // this.setState({
                  //   userInfo: res.userInfo,
                  //   hasUserInfo: true
                  // })
                }
              })
              // 向后端发起token请求
              // Taro.request({
              //   url: 'https://test.com/onLogin',
              //   data: {
              //     code: res.code
              //   }
              // })
            } else {
              console.log('登录失败！' + resp.errMsg)
            }
          }
        })
      },
      fail: resp => {
        console.log(error)
        // Taro.login({
        //   success: resp => {
        //     console.log('login success');
        //     console.log(resp);
        //   },
        //   fail: resp => {
        //     console.log('login fail');
        //     console.log(resp);
        //   }
        // })
      }
    })
  },
  // 对应 onHide
  onHide() {
    console.log('app.js onHide');
  },
})

App
  .use(createPinia())
  .use(Button).use(Toast).use(Tabbar).use(TabbarItem).use(Icon).use(Avatar).use(AvatarGroup).use(Empty)
  .use(Cell).use(CellGroup).use(Menu).use(MenuItem).use(OverLay).use(Popup).use(Tag).use(Navbar).use(Tabs).use(TabPane)
  .use(Price).use(Collapse).use(CollapseItem).use(Divider).use(Ellipsis)
export default App
