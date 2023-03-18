package com.interrogation.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.interrogation.config.properties.SendCodeProperty;
import com.interrogation.exception.Assert;
import com.interrogation.exception.InterrogationException;
import com.interrogation.result.ResponseEnum;
import com.interrogation.service.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SMSServiceImpl implements SMSService {

    @Override
    public void sendCode(String phone, Map<String,Object> paramMap) {
        //建立短信服务连接
        DefaultProfile profile = DefaultProfile.getProfile(
                SendCodeProperty.REGION_Id,
                SendCodeProperty.KEY_ID,
                SendCodeProperty.KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        //发送请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", SendCodeProperty.REGION_Id);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SendCodeProperty.SIGN_NAME);
        request.putQueryParameter("TemplateCode", SendCodeProperty.TEMPLATE_CODE);
        Gson gson = new Gson();
        String s = gson.toJson(paramMap);
        request.putQueryParameter("TemplateParam",s);
        try {
            CommonResponse commonResponse = client.getCommonResponse(request);
            boolean isSuccess = commonResponse.getHttpResponse().isSuccess();
            //响应失败则抛异常
            Assert.isTrue(isSuccess, ResponseEnum.ALIYUN_SMS_ERROR);
            //响应成功，则获取响应结果的数据
            String data = commonResponse.getData();
            //字符串结果转化为键值对
            HashMap<String,String> hashMap = gson.fromJson(data, HashMap.class);
            log.info("阿里云短信服务响应结果");
            String code = hashMap.get("Code");
            String message = hashMap.get("Message");
            log.info("Code："+code);
            log.info("Message："+message);
            //短信发送频繁
            Assert.notEquals("isv.BUSINESS_LIMIT_CONTROL",code,ResponseEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);
            //code状态码不为OK,抛出其他异常
            Assert.equals("OK",code,ResponseEnum.ALIYUN_SMS_ERROR);

        } catch (ClientException e) {
            log.error("短信发送SDK调用失败：");
            log.error("ErrorCode=" + e.getErrCode());
            log.error("ErrorMessage=" + e.getErrMsg());
            throw new InterrogationException(ResponseEnum.ALIYUN_SMS_ERROR , e);
        }
    }
}
