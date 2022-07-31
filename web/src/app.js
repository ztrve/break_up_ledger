import {createApp} from 'vue'
import {
    Button, Toast, Tabbar, TabbarItem, Icon, Avatar, AvatarGroup, Empty,
    Cell, CellGroup, Menu, MenuItem, OverLay, Popup, Tag, Navbar, Tabs, TabPane,
    Price, Collapse, CollapseItem, Divider, Ellipsis, Checkbox, CheckboxGroup,
    Input, InfiniteLoading
} from '@nutui/nutui-taro';
import {createPinia} from 'pinia'
import Taro from '@tarojs/taro'
import './app.styl'
import {LOCAL_STORAGE_KEYS} from "./config/local_storage_keys";
import {authLogin} from "./util/authUtil";

const App = createApp({
    mounted() {
        console.log('app.js mounted');
    },
    // 对应 onLaunch
    onLaunch() {
        console.log('app.js onLaunch');
    },
    // 对应 onShow
    onShow() {
        console.log('app.js onShow');
        const token = Taro.getStorageSync(LOCAL_STORAGE_KEYS.token)
        if (undefined === token || '' === token) {
            authLogin()
        }
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
    .use(Price).use(Collapse).use(CollapseItem).use(Divider).use(Ellipsis).use(Checkbox).use(CheckboxGroup)
    .use(Input).use(InfiniteLoading)

export default App
