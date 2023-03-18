package com.interrogation.controller.api;

import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.result.ResponseResult;
import com.interrogation.service.HospitalInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/hospital")
public class HospitalInfoController {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private HospitalInfoService hospitalInfoService;

    @GetMapping("/getAllNames")
    public ResponseResult getAllNames(){
        List<DictDTO> o = (List<DictDTO>)redisTemplate.opsForValue().get("interrogation:hospital:names");
        log.info("redis中获取医院名");
        if (o == null){
            o = hospitalInfoService.getAllHospitalNames();
            redisTemplate.opsForValue().set("interrogation:hospital:names",o);
            log.info("将医院名称存入redis");
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("hospitalNames",o);
        return ResponseResult.success().setData(resultMap);
    }
}
