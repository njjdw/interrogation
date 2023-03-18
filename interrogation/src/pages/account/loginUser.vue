<template>
	<div class="out-frame">
		<el-form>
			<div class="login-panel">
				<div class="title">用户登录</div>
				<div class="username">	
					用户：<input type="text" v-model="userAccountDTO.userPhone" placeholder="用户名/手机号">
				</div>
				<div class="password">
				密码：<input type="password" v-model="userAccountDTO.password" placeholder="密码">
				</div>
				<div class="checkCode">
				 <el-form-item label="" prop="checkCode" >
					<el-input v-model="userAccountDTO.checkCode"  prefix-icon="lj-icon-yanzhengma"
				           autocomplete="off"
				           autocapitalize="off"
				           spellcheck="false"
				           @keyup.enter.native="submit"
				           style="float:left;width:130px; height: 40px;"
							placeholder="请填写验证码">
					</el-input>
				     <div style="width:90px;float:right;height:40px;">
				          <img alt="" :src="img" @click="getImgCode" style="width:90px;float:right;height:40px;">
				     </div>
				</el-form-item>
				</div>
				<div class="others">
					<span>
						<router-link to="#">忘记密码</router-link>
					</span>
					<span>
						<router-link to="/regist">注册</router-link>
					</span>
				</div>
				<div class="submitButton">
					 <el-button type="primary" round native-type="submit" style="width: 200px;" @click="login">登录</el-button>
				</div>
				</div>
		</el-form>
	</div>
</template>

<script>
import Vue from 'vue';
	import request from '@/utils/request.js'
	import websocket from '@/utils/socket'
	export default {
		data() {
			return {
				img:'',
				userAccountDTO: {
				  userPhone: "",
				  password: "",
				  checkCode:"",
				},
			}
		},
		mounted() {
			this.getImgCode();
		},
		methods: {
			 // 登录验证码
			getImgCode (){
			    request.get("/getCode/imgCode",{responseType: 'blob'}).then((res) => {
					const binaryData = [];
					binaryData.push(res.data);
					this.img = window.URL.createObjectURL(new Blob(binaryData,{type: 'application/jpg;chartset=UTF-8'}));
					// console.log(this.img)
			     });
			},
			login (){
				request.post("/user/login",this.userAccountDTO).then((response) =>{
					if (response){
						
						localStorage.setItem("token",response.data.data.userInfo.token);
						let userInfo = response.data.data.userInfo;
						delete userInfo.token;
						localStorage.setItem("userInfo",JSON.stringify(userInfo));
						this.$router.push("/");
						websocket.getSocket();
					}else{
						return
					}
				})
			}
		}
	}
</script>

<style >
	.out-frame{
		height: 100vh;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.login-panel{
		height: 60vh;
		width: 350px;
		display: flex;
		flex-direction: column;
		justify-content: space-evenly;
		align-items: center;
		border-radius: 5px;
		box-shadow: 1px 1px 2px lightgray;/*x轴偏移、y轴偏移、模糊*/
		margin: 10px;
		background-color: floralwhite;
		opacity: 0.9;
	}
	.title{
		width: 350px;
		height: 6vh;
		text-align: center;
		line-height: 6vh;
		font-size: 20px;
		font-family: 'Courier New', Courier, monospace
	}
	input{
		outline: medium;
		border-style: none;
		/*去除默认边框*/
		width: 200px;
		height: 30px;
		border-radius: 5px;/*长方形设边框圆弧使用px*/
	}
	input:focus{
		 border: 1px solid #65a0fa;/*获取焦点显示边框样式*/
	}
	input::-ms-input-placeholder{
	        text-align: center;
			color: lightgray;
	}
	input::-webkit-input-placeholder{
	        text-align: center;
			color: lightgray;
	}
	.others{
		width: 100%;
		height: 3vh;
		display: flex;
		flex-direction: row;
		justify-content: flex-end;
	}
	.others>span{
		margin-right: 10px;
	}
	.el-message-box{
		width: 50vw;
	}
</style>