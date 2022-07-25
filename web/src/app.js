import { createApp } from 'vue'
import {
  Button, Toast, Tabbar, TabbarItem, Icon, Avatar, AvatarGroup, Empty,
  Cell, CellGroup, Menu, MenuItem, OverLay, Popup, Tag, Navbar, Tabs, TabPane,
  Price, Collapse, CollapseItem, Divider, Ellipsis
} from '@nutui/nutui-taro';
import { createPinia } from 'pinia'

import './app.styl'

const App = createApp({
  mounted() { },
  // 对应 onLaunch
  onLaunch() { },
  // 对应 onShow
  onShow(options) { },
  // 对应 onHide
  onHide() { },
})

App
  .use(createPinia())
  .use(Button).use(Toast).use(Tabbar).use(TabbarItem).use(Icon).use(Avatar).use(AvatarGroup).use(Empty)
  .use(Cell).use(CellGroup).use(Menu).use(MenuItem).use(OverLay).use(Popup).use(Tag).use(Navbar).use(Tabs).use(TabPane)
  .use(Price).use(Collapse).use(CollapseItem).use(Divider).use(Ellipsis)
export default App
