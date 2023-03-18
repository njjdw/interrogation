<template>
	<div class="comment-panel">
		<div class="query-result"></div>
		<div class="comment-result">
			<textarea name="comment" id="" cols="30" rows="10"></textarea>
			<div class="block">
			  <span class="demonstration">默认不区分颜色</span>
			  <el-rate v-model="value1"></el-rate>
			</div>
		</div>
	</div>
</template>

<script>
	 export default {
	    data() {
	      return {
	        stars: null,
	        colors: ['#99A9BF', '#F7BA2A', '#FF9900']  // 等同于 { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }
	      }
	    },
		created() {
			
		},
		methods: {
			
		},
		
	  }
</script>

<style>
</style>