package com.interrogation.controller.api;

import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.pojo.entity.Dict;
import com.interrogation.result.ResponseResult;
import com.interrogation.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 1、从redis中查找是否有相应的数据字典
     * 2、如果没有则从数据库获取并存入redis
     * @param parentId
     * @return
     */
    @GetMapping("/dictList/{parentId}")
    public ResponseResult getDictList(@PathVariable("parentId") Integer parentId){
        List<DictDTO> o = (List<DictDTO>)redisTemplate.opsForValue().get("interrogation:dict:" + parentId);
        log.info("redis中获取dict{}",parentId);
        if (o == null){
            o = dictService.getDict(parentId);
            redisTemplate.opsForValue().set("interrogation:dict:"+parentId,o);
            log.info("将数据字典存入redis");
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list",o);
        return ResponseResult.success().setData(resultMap);
    }

}
