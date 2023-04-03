const { defineConfig } = require('@vue/cli-service')
const path = require('path')
function resolve(dir) {
  return path.join(__dirname, dir)
}
module.exports = defineConfig({
  transpileDependencies: true,
  // 关闭eslint校验,但不起效,用另一种方法：用驼峰命名法
  lintOnSave: false,
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: 'interrogationclear',
    resolve: {
		//起别名
      alias: {
        '@': resolve('src')
      }
    }
  },
  devServer: {
	  proxy: {
			[process.env.VUE_APP_BASE_API]: {
			  target:"http://127.0.0.1:88",
			  changeOrigin: true,  //配置跨域
			  ws: true,
			  pathRewrite: {
				['^' + "http://127.0.0.1:88"]: ''
			  }
			},
		  },
	}
})
