<template>
			<div class="infinite-list-wrapper">
				<div class="search">
					<div style="width: 4vh; height: 4vh;"></div>
					<input type="text" name="" placeholder="医院/医生名称">
					<svg t="1674988042833" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2680"style="width: 4vh; height: 4vh;">
						<path d="M413.679717 606.580099c-109.2451 0-196.641589-87.396489-196.641589-196.642612s87.396489-196.641589 196.641589-196.641589 196.641589 87.396489 196.641589 196.641589S522.924817 606.580099 413.679717 606.580099zM675.869185 606.580099l-34.958186 0-13.109576-13.109576c43.698245-48.067762 69.917396-113.615641 69.917396-183.533037 0-157.312862-126.725216-284.038078-284.038078-284.038078-157.313885 0-284.038078 126.724193-284.038078 284.038078 0 157.313885 126.724193 284.038078 284.038078 284.038078 69.917396 0 135.464251-26.219151 183.532013-69.917396l13.109576 13.109576 0 34.958186 218.491223 218.491223 65.546855-65.546855L675.869185 606.580099z" fill="#CCCCCC" p-id="2681">
						</path>
					</svg>
				</div>
			    <ul
			      class="list"
			      v-infinite-scroll="load"
			      infinite-scroll-disabled="disabled">
			      <!-- <li v-for="i in count" class="list-item">{{ i }}</li> -->
					<div v-for="doctor in list" class='list-item doctor'>
							<div class="base-info">
								<img :src="defaultImg" alt="">
								<div>
									<div>
										<span class="doctor-name">{{doctor.doctorName?doctor.doctorName:'医生名'}}</span>
									</div>
									<span>{{doctor.hospital?doctor.hospital:'挂靠医院'}}</span>
									<span>{{doctor.department?doctor.department:'一科室'}}</span>
									<span>{{doctor.level?doctor.level:'职称'}}</span>
								</div>
							</div>
						<div><button @click="skipChating(doctor.userId)">立即咨询</button></div>
					</div>
					<p v-if="loading">加载中...</p>
					<p v-if="noMore">没有更多了</p>
			    </ul>
			  </div>
</template>

<script>
	import request from "@/utils/request"
	 export default {
	    data () {
	      return {
	        loading: false,
			defaultImg: '/default.jpg',
			list:[],
			hasNextPage: true,
			pageNum: 0,
			// doctors: {},//包含list和分页信息
	      }
	    },
	    computed: {
	      noMore () {
	        return !this.hasNextPage
	      },
	      disabled () {
	        return this.loading || this.noMore
	      }
	    },
		mounted() {
			this.list = [];
			this.pageNum = 0;
			request.get("/doctor/doctorList/"+this.pageNum).then((response) => {
				this.list = response.data.data.doctors.list;
				this.hasNextPage = response.data.data.doctors.hasNextPage;
				this.pageNum = response.data.data.doctors.pageNum;
			})
		},
	    methods: {
	      load () {
	        this.loading = true;
	        request.get("/doctor/doctorList/"+this.pageNum).then((response) => {
	        	this.list.push(...response.data.data.doctors.list);
				this.hasNextPage = response.data.data.doctors.hasNextPage;
				this.pageNum = response.data.data.doctors.pageNum
	        })
			this.loading = false
	      },
		  skipChating(userId){
			  request.get("/doctor/queryNumber/"+userId).then((response) => {
				  if (response.data.code != 0){
					  return
				  }
			  })
			  this.$router.push("/chating/"+userId)
		  }
	    }
	  }
</script>

<style scoped>
	.infinite-list-wrapper{
		width: inherit;
		height: 89vh;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		align-items: center;

	}
	.list{
		height: auto;
	}
	.list-item{
		box-shadow: 1px 1px 1px lightgray;
	}
	.list,.infinite-list-wrapper{
		overflow: scroll;
	}
	.infinite-list-wrapper::-webkit-scrollbar,.list::-webkit-scrollbar{
		display: none;
	}
	/*宽度小于等于915px样式生效*/
	@media only screen and (max-width: 400px) {
	}
		.doctor{
			width: 70vw;
			height: 10vh;
			display: flex;
			flex-direction: column;
			justify-content: space-evenly;
			align-items: center;
			border-radius: 1.5vh;
			/* overflow: hidden; */
			margin-bottom: 2px;
			background: green;
		}
		.doctor>.base-info{
			width: 70vw;
			height: 5vh;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: flex-start;
			font-size: 5px;
		}
		button{
			height: 5vh;
			width: 70vw;
			background-color: royalblue;
			border: none;
			border-radius: 1.5vh;
		}
		.base-info>img{
			width: 5vh;
			height: 5vh;
			border-radius: 50%;
		}
		.base-info>div{
			width: 56vw;
		}
		.base-info span:not(.doctor-name){
			display: inline-block;/*使行内元素设置宽高*/
			font-size: 3px;
			width: auto;
			margin-right: 5px;
			background-color: lightgreen;
			border-radius: 2px;
			padding: 1px;
		}
		.search{
			width: 70vw;
			height: 5vh;
			border-radius: 2.5vh;
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			box-shadow: 1px 1px 10px #e2e2e2;
			background-color: white;
			overflow: hidden;
			/* margin-bottom: 10px; */
		}
	
		.search>input{
			width: 55vw;
		}
	
	button:hover{
		background-color: #5b96e2;
	}
	
	input{
		outline: medium;
		border-style: none;
		/*去除默认边框*/
		width: 150px;
		height: 4vh;
		border-radius: 5px;/*长方形设边框圆弧使用px*/
	}
	input::-ms-input-placeholder{
	        text-align: center;
			color: lightgray;
	}
	input::-webkit-input-placeholder{
	        text-align: center;
			color: lightgray;
	}
	p{
		text-align: center;
		font-size: 10px;
	}
	/*915px页面宽度及以上时，样式生效 */
	@media only screen and (min-width: 915px) {
		.doctor>.base-info,button,.doctor{
			width: 60vw !important;
		}
		.doctor{
			height: 20vh !important;
		}
	}
</style>
