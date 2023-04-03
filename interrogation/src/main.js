import Vue from 'vue'
import App from './App.vue'
//引用组件
import ElementUI from 'element-ui'
//引用组件样式
import 'element-ui/lib/theme-chalk/index.css'
//应用主键
Vue.use(ElementUI)
import router from './router/index.js'
Vue.config.productionTip = false
new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
/*全局前置路由守卫*/
router.beforeEach((to,from,next) => {
	if (to.meta.requiresAuth == true){
		if (localStorage.getItem("token") != null){
			//token存在就放行
			console.log("前置路由守卫"+localStorage.getItem("token"))
			next()
		}else {
			new Vue().$message.error("未登录用户")
			router.push("/login")
		}
	} else {
		next()
	}
})