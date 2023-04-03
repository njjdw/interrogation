<template>
	<!-- template模板下必须要并只能有一个根结点 -->
	<div class="main">
		<div class="header">{{title}}</div>
		<div class="panel">
			<router-view></router-view>
		</div>
		<div class="nav">
			<router-link to="/doctor_list" class="link">
				医生
			</router-link>
			<router-link to="/healthy_list" class="link">
				健康常识
			</router-link>
			<router-link to="/self_info/1" class="link">
				我的
			</router-link>
		</div>
	</div>
</template>

<script>
import Cookies from 'js-cookie';
import request from "@/utils/request"
	export default {
		data() {
			return {
				// title: "页面主题"
			}
		},
		computed:{
			title(){
				return this.$route.meta.title;
			}
		},
		  mounted() {
		    //先在服务器端校验token
		        request({
		            url: '/user/checkToken',
		            method: 'get',
		        }).then((response) => {
		            console.log('校验成功')
		        })
		  },
		
	}
</script>

<style scoped>
	.main{
		width: 100vw;
		display: flex;
		flex-direction: column;
		height: 100vh;
		/*以上样式自适应撑满高度*/
		align-items: center;
	}
	.header{
		width: 100vw;
		height: 6vh;
		text-align: center;
		line-height: 6vh;
		background-color: white;
	}
	.panel{
		overflow: scroll;
	}
	.panel::-webkit-scrollbar{
		display: none;
	}
	@media only screen and (max-width: 915px) {
		.panel{
			height: 89vh;
			width: 100vw;
			margin: 0;
			border-radius: 0;
			opacity: 0.9;
			background-color: transparent;
		}
	}
	
	/* 表示当屏幕宽度大于等于915px时，该样式生效。 */
	/*媒体查询的样式写在后方，否则会被相同选择器覆盖*/
	@media only screen and (min-width: 915px) {
		.panel{
			height: 89vh;
			width: 60vw;
			margin: 0;
			border-radius: 0;
			background-color: transparent;
		}
	}
	.nav{
		height: 5vh;
		width: 100vw;
		background-color: white;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
	}
	.link{
		text-decoration: none;
		color: black;
		text-align: center;
		/* line-height: 5vh; */
		outline: none;
	}
	.link:hover {
		text-decoration: none;
	}
	.link:visited{
		text-decoration: none;
	}
</style>