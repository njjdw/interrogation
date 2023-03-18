package com.interrogation.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.interrogation.config.MySpringConfigurator;
import com.interrogation.service.ChatingRecordsService;
import com.interrogation.service.InterviewRecordsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/webSocket/{userId}",configurator = MySpringConfigurator.class)
public class ChatingController {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    ChatingRecordsService chatingRecordsService;
    @Resource
    InterviewRecordsService interviewRecordsService;

    private ConcurrentHashMap<Integer,Session> onlineUser = new ConcurrentHashMap();
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId){
        System.out.println("连接websocket通信"+userId);
        onlineUser.put(userId,session);
        Set<JSONObject> msg = (Set<JSONObject>)redisTemplate.opsForSet().members("interrogation:recipant:" + userId);
        if (msg == null) return;
        Iterator<JSONObject> messageList = msg.iterator();
        while (messageList.hasNext()){
            JSONObject message = messageList.next();
            dealWithMessage(message,message.getInteger("recipant"),userId);
        }
        String message = JSONObject.toJSONString(msg);
        session.getAsyncRemote().sendText(message);
        redisTemplate.opsForSet().remove("interrogation:recipant:" + userId);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId){
        onlineUser.remove(userId,session);
    }
    @OnError
    public void onError(Throwable error,Session session, @PathParam("userId") Integer userId){

    }


    @OnMessage
    public void onMessage(Session session,String message,@PathParam("userId") Integer userId){

        JSONObject jsonObject = JSONObject.parseObject(message);
        /**一、发送患者信息
         * 1、获取医生session话，如果医生不在线则将信息保存在redis
         * 2、添加医生当前问诊人员成员
         * 3、当医生登录在线时，从redis中获取患者信息
         * 4、否则直接将消息直接发送给医生
         * 5、同时将信息保存入数据库
         */
        Integer recipantId = jsonObject.getInteger("recipant");
        Session recipant = onlineUser.get(recipantId);
        if (recipant == null) {//不在线
            //暂存信息
            redisTemplate.opsForSet().add("interrogation:recipant:" + recipant, jsonObject);
            return;
        }
        dealWithMessage(jsonObject,recipantId,userId);
        recipant.getAsyncRemote().sendText(message);
    }

    /**
     * 一、 患者基本信息处理
     * 二、对话处理
     *  三、医生治疗建议处理
     * @param message 消息
     * @param recipantId 接收者id
     * @param userId    发送者id
     */
    private void dealWithMessage(JSONObject message,Integer recipantId,Integer userId){
        if (message.containsKey("patientName")&&(!message.containsKey("suggestion"))){//问诊开始信息
            //添加医生的问诊人员
            redisTemplate.opsForSet().add(recipantId+":queryUser",userId);
            //暂存医生接诊的患者信息
            redisTemplate.opsForValue().set(userId+":query:" + recipantId, message);

        }else if(message.containsKey("suggestion")){//问诊结束信息
            //1、获取患者信息
            JSONObject patientInfo = (JSONObject)redisTemplate.opsForValue().getAndDelete(recipantId + ":query:" + userId);
            patientInfo.putAll(message);
            int recordId = interviewRecordsService.insertRecord(patientInfo);
            message.put("interviewRecordId",recordId);
        }else {//普通信息
            chatingRecordsService.inputChatRecords(message);
        }
    }

}
