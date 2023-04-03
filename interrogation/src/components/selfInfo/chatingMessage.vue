<template>
	<div class="out-frame">
		<div v-for="message in messageList" class='list-item doctor'>
				<div class="base-info"  @click="skipChating(message.senderId)">
					<img :src="defaultImg" alt="">
					<div>
						<div>
							<span class="doctor-name">{{message.senderName}}</span>
						</div>
						<span>{{message.content}}</span>
					</div>
				</div>
		</div>
	</div>
	
</template>

<script>
	export default{
		data() {
			return {
				webSocketObj: this.$webSocket,
				messageList:[]
			}
		},
		created() {
			getMessageList()
		},
		watch(){
			for (let msg of messageList) {
				
			}
		},
		methods: {
			getMessageList(){
				
			},
			socketOnMessage(e){
				/**
				 * message: {
				 *  senderName:string
				 * sender:int
				 * recipant:int
				 * content:string
				 * sendTime:date
				 * 
				 }
				 */
				let message = JSON.parse(e.data);
				for (let msg of messageList) {
					if (msg.senderId === message.senderId){
						msg.content = message.content
					}
				}
			},
		}
	}
</script>

<style>
	.doctor>.base-info{
		width: 70vw;
		height: 5vh;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: flex-start;
		font-size: 5px;
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
</style>