function getBaseUrl () {
    const env = process.env.NODE_ENV === 'development' ? 'development' : 'production'
    console.log('编译环境：',process.env.NODE_ENV)
    switch (env) {
        case 'development':
            return 'http://10.0.2.62:3000/api/bul'
        case 'production':
            return 'https://bul.diswares.cn/api/bul'
    }
    return ''
}

export {getBaseUrl}
