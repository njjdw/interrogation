<template>
	<div class="out-frame">
		<div class="header">{{counterpart}}</div>
		<div class="chat-panel">
			<div class="message">
				<img src="http://localhost:8080/default.jpg" class="head-img">
				<pre class="message-content">dghjjgggsdfghjk</pre>
			</div>
		</div>
		<div class="send-frame">
			<div class="send">
			<button id="send-content">发送</button>
			<el-button type="text" @click="dialogVisible = true" id="finish">结束问诊</el-button>
			<button id="doc">
				<input type="file" id="file">
				<label for="file"><i class="el-icon-plus"></i></label>
			</button>
			</div>
			<textarea></textarea>
		</div>
		<el-dialog
		  title="填写患者信息"
		  :visible.sync="dialogVisible"
		  :before-close="handleClose"
		  custom-class="dialog-class">
		 <el-form>
			 <el-form-item>
			 	<el-input v-model="patientInfo.patientName" placeholder="姓名" />
			 </el-form-item>
			 <el-form-item >
			 	<el-select v-model="patientInfo.patientSex" placeholder="性别" >
			 		 <el-option label="男" value="男"></el-option>
						<el-option label="女" value="女"></el-option>
			 	</el-select>
			 </el-form-item>
			 <el-form-item >
			 	<el-input v-model="patientInfo.patientAge" placeholder="年龄" />
			 </el-form-item>
			 <el-form-item>
			 	<el-input v-model="patientInfo.description" placeholder="症状"/>
			 </el-form-item>
		 </el-form>
		  <span slot="footer" class="dialog-footer">
		    <el-button @click="dialogVisible = false">取 消</el-button>
		    <el-button type="primary" @click="submitPatientInfo()">提交</el-button>
		  </span>
		</el-dialog>
	</div>
</template>

<script>
	import request from '@/utils/request'
	import websocket from '@/utils/socket'
import Vue from 'vue';
	export default {
	    data() {
	      return {
	        dialogVisible: false,
			webSocketObj: null,
			/**
			 * message: {
			 *  senderName:string
			 * sender:int
			 * recipant:int
			 * content:string
			 * sendTime:date
			 * 
			 },
			 */
			patientInfo:{
				queryUser: '',
				patientAge: '',
				patientSex: '',
				patientName: '',
				recipant: 0,
				description: ''
			}
	      };
	    },
		computed: {
			counterpart() {
				request.get("/doctor/getDoctorName/"+this.$route.params.id).then((response) => {
					if (response.data.data.doctorName){
						return response.data.data.doctorName
					}
				})
			}
		},
		mounted() {
			this.dialogVisible = true
			this.webSocketObj = websocket.getSocket()
		},
		destroyed() {
		},
	    methods: {
	      handleClose(done) {
	        // this.$confirm('确认关闭？')
	        //   .then(_ => {
	        //     done();
	        //   })
	        //   .catch(_ => {});
	      },
		  submitPatientInfo(){
			  let userInfo = localStorage.getItem("userInfo");
			  userInfo = JSON.parse(userInfo);
			  this.patientInfo.queryUser = userInfo.id;
			  this.patientInfo.doctorId = this.$route.params.id;
			  for (let key of Object.keys(this.patientInfo)) {
			  	if (!this.patientInfo[key]){
			  		this.$message.error("请填写完整患者信息");
			  		return
			  	}
			  }
			  this.webSocketObj.send(JSON.stringify(this.patientInfo));
			  this.dialogVisible = false;
		  },
	    }
	  };
</script>

<style >
	.out-frame{
		height: 100vh;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.header{
		width: 60vw;
		height: 6vh;
		text-align: center;
		line-height: 6vh;
		background-color: white;
	}
	.chat-panel{
		height: 80vh;
		width: 60vw;
		margin: 0;
		border-radius: 0;
		background-color: whitesmoke;
	}
	.send-frame{
		height: 14vh;
		width: 60vw;
		background-color: white;
		display: flex;
		flex-direction: column;
		justify-content: space-evenly;
		align-items: center;
		color: #40b7fe;
	}
	@media screen and (max-width: 915px) {
		.chat-panel,.header,.send-frame{
			width: 100vw;
		}
		.el-dialog__header,.el-dialog__body,.el-dialog__footer,.el-message-box{
			width: 80vw;
		}
	}
	textarea{
		outline: none;
		border: none;
		width: inherit;
		height: 10vh;
	}
	#send-content{
		width: 40px;
		height: 20px;
		border: solid 1px #9ceaff;
		border-radius: 0.5px;
	}
	label{
		margin: 0;
		padding: 0;
	}
	label{
		width: 20px;
		height: 20px;
	}
	.send{
		display: flex;
		flex-direction: row;
		justify-content: flex-end;
		align-items: center;
		width: inherit;
		height: 4vh;
	}
	input[type='file']{
		width: 20px;
		height: inherit;
		display: none;
	}
	.message{
		width: inherit;
		display: flex;
		flex-direction: row;
	}
	.head-img{
		width: 5vh;
		height: 5vh;
		border-radius: 50%;
		overflow: hidden;
	}
	.message-content{
		width: 50%;
		border-radius: 5px;
		/* margin-top: 5px; */
	}
	pre{
		width: auto;
		background: #40b7fe;
		padding: 2px;
	}
	#finish{
		border: solid lightblue 1px;
		border-radius: 2px;
		padding: 0;
		height: 20px;
		line-height: 20px;
		margin-left: 1px;
		margin-right: 1px;
	}
	.dialog-class{
		 width: 80%;
		 font-size: 4px;
	}
</style>