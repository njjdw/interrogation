<template>
	<div class="out-frame">
		<div class="info">
			<div class="base">
				<img :src="selfInfo.headImg?selfInfo.headImg:defaultImg" alt="">
				<span class="nick-name">{{selfInfo.nickName?selfInfo.nickName:'昵称'}}</span>
			</div>
			<div class="change-info">
				<template  v-if="selfInfo.id">
					<button class="edit">编辑信息</button>
					<button class="certify" v-if="selfInfo.role !== 1" @click="skipAuthentication">医生认证</button>
				</template>
				<template v-else>
					<button class="edit" @click="$router.push('/login');">登录</button>
				</template>
			</div>
		</div>
		<div>
			<div class="title">
				<!-- 省略当前组件父路由 -->
				<router-link to="/chating_message/1">消息</router-link>
				<router-link to="/interrogation_records/1">问诊记录</router-link>
				<router-link to="/brower_records/1" >浏览记录</router-link>
			</div>
			<div id="main">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>

<script>
import Cookies from 'js-cookie'
	export default {
		data() {
			return {
				defaultImg: "",
				selfInfo: {
					id: null,
					nickName: "",
					headImg: "",
					role: 0,
					userPhone: ""
				},
			}
		},
		mounted() {
			if (localStorage.getItem("userInfo")){
				let userInfo = localStorage.getItem("userInfo");
				userInfo = JSON.parse(userInfo);
				this.selfInfo = userInfo;
			}
		},
		methods: {
			skipAuthentication(){
				if (localStorage.getItem("token")){
					this.$router.push('/authentication/'+this.selfInfo.id)
				}
			},
		}
	}
</script>

<style scoped>
	.out-frame{
		width: inherit;
		height: inherit;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
	}
	a{
		text-decoration: none;
		color: black;
		text-align: center;
		/* line-height: 5vh; */
		outline: none;
	}
	a:hover {
		text-decoration: none;
	}
	a:visited{
		text-decoration: none;
	}
	.title{
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		font-size: 10px;
	}
	
	.info{
		width: inherit;
		height: 10vh;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
	}
	.base,.change-info{
		display: flex;
		flex-direction: row;
		width: 50vw;
		height: 6vh;
		
	}
	.base{
		padding-left: 15px;
		justify-items: flex-start;
	}
	.change-info{
		padding-right: 15px;
		justify-content: flex-end;
		align-items: center;
	}
	.base>img{
		width: 6vh;
		height: 6vh;
		border-radius: 50%;
	}
	.nick-name{
		width: 20vw;
		height: 6vh;
		line-height: 6vh;
		text-align: center;
	}
	button{
		height: 3vh;
		width: 20vw;
		background-color: royalblue;
		border: none;
		border-radius: 1.5vh;
		margin-right: 1vw;
	}
</style>