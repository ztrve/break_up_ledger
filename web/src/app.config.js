export default defineAppConfig({
    pages: [
        'pages/index/index',
        'pages/test/index'
    ],
    subpackages: [
        {
            root: "packageA",
            pages: [
                "pages/login/index",
            ]
        }
    ],
    preloadRule: {
        "pages/index/index": {
            "network": "all",
            "packages": ["packageA"]
        }
    },
    window: {
        backgroundTextStyle: 'light',
        navigationBarBackgroundColor: '#fff',
        navigationBarTitleText: 'WeChat',
        navigationBarTextStyle: 'black'
    }
})
