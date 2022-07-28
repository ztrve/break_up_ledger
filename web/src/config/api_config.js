export default function () {
    const env = process.env.NODE_ENV === 'development' ? 'development' : 'production'
    console.log('编译环境：',process.env.NODE_ENV)
    switch (env) {
        case 'development':
            process.env.BASE_URL = 'http://localhost:3000/api/bul'
            break
        case 'production':
            process.env.BASE_URL = 'http://localhost:3000/api/bul'
            break
    }
}
