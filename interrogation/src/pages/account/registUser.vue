<template>
	<div class="out-frame">
		<el-form action="" >
			<div class="regist-panel">
				<div class="title">用户注册</div>
				<div class="username">	
					用户：<input type="text" v-model="userAccountDTO.userPhone" placeholder=" 手机号" id = "number">
				</div>
				<div class="password1">
				密码：<input type="password" v-model="userAccountDTO.password" placeholder="密码">
				</div>
				<div class="password2">
				确认密码：<input type="password" v-model="passwords" placeholder="确认密码">
				</div>
				<div class="checkCode">
					<input type="text" style="width: 150px;" v-model="userAccountDTO.checkCode" placeholder="短信验证码"> 
					<el-button v-if="!sending" type="primary" size="small" @click="getCode">
						获取验证码
						</el-button>
					<el-button v-else type="info" size="small" disabled>{{restSecond}}秒后重发</el-button>
				</div>
				<div class="others">
					<span>
						<router-link to="#">忘记密码</router-link>
					</span>
					<span>
						<router-link to="/login">登录</router-link>
					</span>
				</div>
				<div class="submitButton">
					 <el-button type="primary" round native-type="submit" style="width: 200px;" @click="regist">注册</el-button>
				</div>
				</div>
		</el-form>
	</div>
</template>

<script>
	import request from '../../utils/request.js'
	export default {
		data() {
			return {
				sending: false,
				second: 15,
				restSecond: 0,
				passwords: "",
				userAccountDTO: {
				  userPhone: "",
				  password: "",
				  checkCode:"",
				},
			}
		},
		methods: {
			 // 获取验证码
			   /**
				*1、判断是否有输入手机号
				 2、获取验证码
				 3、sending改为true、触发setInterval
				 4、restSecond为0时，sending改为false 
				*/
			getCode(){
			   if (this.userAccountDTO.userPhone){
					request.get("/getCode/sms/"+this.userAccountDTO.userPhone).then(
					(response) =>{
						if (response){
						this.$message.success(response.data.message);
						document.getElementById("number").disabled = true;
						}
					}
					);
					this.sending = !this.sending;
					this.restSecond = this.second;
					let fadeTime = setInterval(()=>{
						if (this.restSecond <= 0){
							clearInterval(fadeTime);
					  		this.sending = !this.sending;
						}else{
							this.restSecond--;
						}
					},1000)//以毫秒为单位
			   }else{
				   this.$message.error("请输入手机号");
			   }
			},
			/**
			 * 1、判断密码是否为空，合法、一致
			 * 2、短信验证码是否为空
			 */
			regist(){
				let reg = /^[a-zA-Z0-9]{6,9}$/;
				if (!this.userAccountDTO.password){
					this.$message.error('请输入密码');
					return;
				}else if(!reg.test(this.userAccountDTO.password)){
					this.$message.error('密码应包含字母、数字共6-9位');
					return;
				}else if (this.userAccountDTO.password !== this.passwords){
					this.$message.error("请确定密码是否一致");
					return;
				}
				
				if (!this.userAccountDTO.checkCode){
					this.$message.error('验证码不能为空')
					return;
				}
			     request.post("/user/regist",
					 this.userAccountDTO
				 ).then(
					(response) =>{
						if (response){
						this.$message.success(response.data.message);
						}
					}
				 )
			}
		}
	}
</script>

<style>
	.out-frame{
		height: 100vh;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.regist-panel{
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
		width: inherit;
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
	.regist-panel>div:not(0){
		width: inherit;
		height: 8vh;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
	}
</style>