import Vue from 'vue'
import VueRouter from 'vue-router'
import Router from 'vue-router'
//应用插件
Vue.use(Router)

export default new VueRouter({
	routes: [
	{
		path: '/login',
		name: 'Login',
		meta: {title: '用户登录'},
		component: ()=>import("@/pages/account/loginUser"),
		hidden: true
	},
	{
		path: '/regist',
		name: 'Regist',
		meta: {title: '用户注册'},
		component: ()=>import("@/pages/account/registUser"),
		hidden: true
	},
	//主页路由，SPA结构
	{
		path: '/',//默认路由
		redirect: '/doctor_list',
		component: ()=>import("@/pages/main/myIndex"),
		hidden: true,
		children: [
			{
				path: '/doctor_list',
				name: 'Doctors',
				meta: {title: '择医问诊'},
				component: ()=>import("@/pages/main/doctorList")
			},
			{
				path: '/healthy_list',
				name: 'HealthyList',
				meta: {title: '健康常识'},
				component: ()=>import("@/pages/main/healthyList")
			},
			{
				path: '/self_info/:id',
				name: 'SelfInfo',
				redirect: '/chating_message/:id',
				meta: {title: '个人信息',requiresAuth: true},
				component: ()=>import("@/pages/main/selfInfo"),
				children: [
					{
						path: '/brower_records/:id',/*不需要重复引入上级组件路由*/
						name: 'BrowerRecords',
						meta: {title: '浏览记录'},
						component: ()=>import("@/components/selfInfo/browerRecords")
					},
					{
						path: '/interrogation_records/:id',
						name: 'InterrogationRecords',
						meta: {title: '问诊记录'},
						component: ()=>import("@/components/selfInfo/interrogationRecords")
					},
					{
						path: '/chating_message/:id',
						name: 'ChatingMessage',
						meta: {title: '聊天信息'},
						component: ()=>import("@/components/selfInfo/chatingMessage")
					},
				]
			},
		]
	},
	
	{
		path: '/doctor_info/:id',
		name: 'DoctorInfo',
		meta: {title: '医生信息'},
		component: ()=>import("@/pages/doctors/doctorInfo"),
		hidden: true
	},
	{
		path: '/hospital_info/:id',
		name: 'HospitalInfo',
		meta: {title: '医院信息'},
		component: ()=>import("@/pages/doctors/hospitalInfo"),
		hidden: true
	},
	{
		path: '/chating/:id',
		name: 'Chating',
		meta: {title: '问诊聊天室',requiresAuth: true},
		component: ()=>import("@/pages/interrogation/chatingRoom"),
		hidden: true
	},
	{
		path: '/healthy_info/:id',
		name: 'HealthyInfo',
		meta: {title: '健康知识'},
		component: ()=>import("@/pages/healthy/healthyInfo"),
		hidden: true
	},
	{
		path: '/authentication/:id',
		name: 'Authentication',
		meta: {title: '医生认证',requiresAuth: true},
		component: ()=>import("@/pages/doctors/authentication"),
		hidden: true
	},
	{
		path: '/comment',
		name: 'Comment',
		meta: {title: '评价',requiresAuth: true},
		component: ()=>import("@/pages/interrogation/comment"),
		hidden: true
	}
]
})