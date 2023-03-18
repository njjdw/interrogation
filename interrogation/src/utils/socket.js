export default{
	
	getSocket(){
		let userInfo = localStorage.getItem("userInfo");
		userInfo = JSON.parse(userInfo);
		var websocket = new WebSocket("ws://127.0.0.1:88/interrogation/webSocket/"+userInfo.id);
		return websocket
	}
}