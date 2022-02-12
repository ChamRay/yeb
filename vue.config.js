//处理跨域 ip：port不同时的前后端访问问题

let proxyObj = {}

proxyObj['/']={
    // websorcket
    ws:false,
    /* 代理的目标地址 */
    target:'http://localhost:8081',
    // 发送请求头host会被设置target
    changeOrigin:true,
    //不重写请求地址
    pathReWrite:{
        '^/':'/'
    }
},

module.exports = {
    devServer:{
        /* 本项目地址和端口 */
        host:'localhost',
        port:8080,
        //代理对象
        proxy:proxyObj
    }
}
