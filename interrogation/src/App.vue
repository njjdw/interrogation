<template>
  <div id="app">
  <router-view />
  </div>
</template>

<script>

export default {
  name: 'App',
}
</script>

<style>
	#app{
		width: 100%;
		height: 100%;
		background: url("assets/images/theme.jpg");
		background-size: cover;
	}
	@media screen and (max-width: 915px) {
		#app{
			background: none;
		}
	}
</style>
