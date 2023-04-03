package com.interrogation.controller.api;

import com.interrogation.exception.InterrogationException;
import com.interrogation.result.ResponseEnum;
import com.interrogation.result.ResponseResult;
import com.interrogation.service.SMSService;
import com.interrogation.service.UserInfoService;
import com.interrogation.utils.RandomImgCodeUtils;
import com.interrogation.utils.RandomUtils;
import com.interrogation.utils.RegexValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/getCode")
public class SendCodeController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private SMSService smsService;
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 1、判断手机号码是否合法
     * 2、手机号码是否已注册
     * 3、生成短信验证码
     * 4、调用sms发送验证短信
     * 5、将验证码限时存入redis缓存
     * @param phone 手机号码
     */
    // "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$"
    /**
     * ^：表示以什么开头，在[]内使用时表示非
     * |：或，表示有多种可匹配字符串
     * [0-9]：0到9的任意一个数字
     * (13[0-9])：选项中的一组可匹配的字符串
     * \\d{8}：任意8各数字，双斜杠是为了防止java将\d解析成特定字符，\\d才能保留原由意义
     * $：结束符，要结合^开始符号使用，否则不能确定字符串长度
     */
    @GetMapping("/sms/{phone}")
    public ResponseResult smsCode(@PathVariable("phone") String phone){

        if (!RegexValidateUtils.checkCellphone(phone)){
            throw new InterrogationException(ResponseEnum.MOBILE_ERROR);
        }
        if(userInfoService.isExistNumber(phone)){
            throw new InterrogationException(ResponseEnum.MOBILE_EXIST_ERROR);
        }
        String fourBit = RandomUtils.getDigitBits(4);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("code",fourBit);
        smsService.sendCode(phone,paramMap);
        redisTemplate.opsForValue().set("interrogation:sms:code:"+phone,fourBit, Duration.ofMinutes(5));
        log.info("验证码{}存入redis",fourBit);
        return ResponseResult.success().setMessage("验证码发送成功");
    }

    /**
     * 图片以数据流的方式传回
     * @return
     */
    @GetMapping("/imgCode")
    private ResponseResult imgCode(HttpServletResponse response,HttpServletRequest request) {
            String codes = RandomImgCodeUtils.getRandomImgCode(response,request);
            String ip = request.getRemoteAddr();
            log.info("图片验证码{}存入redis,ip:{}",codes,ip);
            redisTemplate.opsForValue().set("interrogation:img:code:"+ip,codes, Duration.ofMinutes(5));
        return ResponseResult.success();
    }
}
