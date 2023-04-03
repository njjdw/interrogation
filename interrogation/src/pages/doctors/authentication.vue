<template>
	<div class="out-frame">
		<template>
			<el-steps style="width: 80vw; height: 10vh;" :active="active" finish-status="success">
			  <el-step title="提交"></el-step>
			  <el-step title="审核"></el-step>
			  <el-step title="通过" v-if="certify.authenticateStatus <= 2"></el-step>
			  <el-step title="不通过" v-else-if="certify.authenticateStatus == 3" status="error"></el-step>
			</el-steps>
		</template>
		<div class="certify-panel" v-if="this.active == 0">
		<el-form >
			<input type="hidden" name="userId" v-model="certify.userId">
			<el-form-item label="姓名">
				<el-input v-model="certify.userName" style="width: 200px;"/>
			</el-form-item>
			<el-form-item label="身份证号">
				<el-input v-model="certify.idCard" style="width: 200px;"/>
			</el-form-item>
			<el-form-item label="医院">
				<!-- 仅一次触发本地事件 -->
				<el-select @click.native.once="getHospital" v-model="certify.hospitalId" :filterable=true placeholder="医院">
					 <el-option
					      v-for="item in hospitalList"
					      :key="item.id"
						  :label="item.name"
					      :value="item.id">
					    </el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="科室">
				<el-select @click.native.once="getDict(2)" v-model="certify.department" placeholder="科室">
					 <el-option
					      v-for="item in departmentList"
					      :key="item.id"
						  :label="item.name"
					      :value="item.id">
					    </el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="医生职称">
				<el-select @click.native.once="getDict(3)" v-model="certify.level" placeholder="职称">
					 <el-option
					      v-for="item in levelList"
					      :key="item.id"
					      :value="item.id"
						  :label="item.name">
					    </el-option>
				</el-select>
			</el-form-item>
			<div><el-button type="primary" round native-type="submit" style="width: 200px;" @click="submit">提交</el-button></div>
		</el-form>
		<el-form>
			<el-form-item label="执业资格证">
				<el-upload
				  :action="uploadUrl"
				  class="upload-demo"
				  :on-preview="handlePreview"
				  :on-remove="handleRemove"
				  :on-success="onUploadSuccess"
				  :limit="1"
				  list-type="picture">
				  <el-button size="small" type="primary">点击上传</el-button>
				  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
				</el-upload>
			</el-form-item>
		</el-form>
		</div>
		<div class="certify-panel" style="height: 80vh;" v-else-if="this.active >= 1">
			<div>姓名：{{certify.userName}}</div>
			<div>身份号：{{certify.idCard}}</div>
			<div>医院：{{certify.hospital}}</div>
			<div>科室：{{certify.department}}</div>
			<div>医生职称：{{certify.level}}</div>
			<div>执业资格证： <img style="width: 20vh; height: 20vh;" :src="baseUrl+certify.qualification"/></div>
			<div v-if="certify.authenticateStatus <= 1"><el-button type="primary" round native-type="submit" style="width: 200px;" @click="">修改</el-button></div>
		</div>
	</div>
</template>

<script>
	import request from '@/utils/request'
	export default{
		data() {
			return {
				baseUrl: "http://localhost:88/interrogation/qualification/",
				certify: { 
					userId: 0,
					userName: '',
					idCard: '',
					hospitalId: '',
					department: '',
					level: '',
					qualification: '',
					authenticateStatus: 0,
				},
				uploadUrl: 'http://localhost:88/interrogation/api/doctor/uploadQualification',
				hospitalList: [],
				levelList: [],
				departmentList: [],
				dialogImageUrl: '',
				dialogVisible: false,
				fileList: [],
				active: 0,
			}
		},
		created() {
			this.certify.userId = this.$route.params.id;
			request.get("/doctor/getAuthenticateInfo/"+this.certify.userId).then((response) =>{
				if (!response.data.data.authenticateInfo){
					this.active = 0;
				}else {
					this.certify = response.data.data.authenticateInfo;
					this.certify.userName = response.data.data.authenticateInfo.doctorName
					let status = response.data.data.authenticateInfo.authenticateStatus;
					/**
					 * 首先判断认证到哪一步；由active表示
					 * 在判断认证状态，由authenticateStatus
					 */
					if (status == 0||status == 1){//认证中
						this.active = 1
					}else if (status == 2 || status == 3){
						this.active = 3
					}
				}
			})
		},
		methods: {
			handleRemove(file, fileList) {
				let qualification = this.certify.qualification;
					request.post("/doctor/deleteQualification/"+qualification)
			      },
			handlePreview(file) {
			        this.dialogImageUrl = file.url;
			        this.dialogVisible = true;
			    },
			getHospital(){
				request.get("/hospital/getAllNames").then((response) =>{
					this.hospitalList = response.data.data.hospitalNames;
				})
			},
			getDict(parentId){
				request.get("/dict/dictList/"+parentId).then((response) =>{
					let list = response.data.data.list;
					parentId == 2? this.departmentList = list : this.levelList = list;
				})
			},
			onUploadSuccess(response, file,fileList) {
			  // debugger
			  if (response.code !== 0) {
			    this.$message.error(response.message)
			    return
			  }
			  // 填充上传文件路径
			 this.certify.qualification = response.data.qualification
			 console.log("图片路径"+this.certify.qualification)
			},
			submit(){
				if (!this.certify.qualification){
					this.$message.error('请先上传执业资格证')
					return;
				}
				for (let key of Object.keys(this.certify)) {
					if (key === 'authenticateStatus') continue;
					if (!this.certify[key]){
						this.$message.error("请填写完整所以认证信息")
						return
					}
				}
				
				console.log(this.certify)
				let authenticationInfo = this.certify;
				request.post("/doctor/authenticate",this.certify).then((response) => {
					this.active = 1
					window.location.reload();
				})
			}
		}
	}
</script>

<style>
	.out-frame{
		width: 100vw;
		height: 100vh;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		align-items: center;
		padding-top: 10px;
	}
	.certify-panel{
		display: flex;
		flex-direction: column;
		justify-content: space-evenly;
		align-items: center;
		border-radius: 5px;
		/* margin: 10px; */
	}
	.el-upload>input{
		display: none;
	}
	.el-form-item{
		height: 10vh;
	}
	.upload-demo{
		height: auto;
	}
	@media screen and (max-width: 400px) {
		.el-form{
			width: 80vw;
		}
	}
</style>