function getBaseUrl () {
    const env = process.env.NODE_ENV === 'development' ? 'development' : 'production'
    console.log('编译环境：',process.env.NODE_ENV)
    switch (env) {
        case 'development':
            return 'http://localhost:3000/api/bul'
        case 'production':
            return 'http://localhost:3000/api/bul'
    }
    return ''
}

export {getBaseUrl}
