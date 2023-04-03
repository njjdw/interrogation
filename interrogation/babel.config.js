module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset',
	["@babel/preset-env",{"modules":false}]
  ],
  "plugins":[
	  [
  // 配置允许按需引入部分组件
		  "component",
		  {
			  "libraryName": "element-ui",
			  "styleLibraryName": "theme-chalk"
		  }
	  ]
  ]
}
