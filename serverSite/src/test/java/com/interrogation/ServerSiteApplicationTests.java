package com.interrogation;

import com.interrogation.utils.RandomImgCodeUtils;
import com.interrogation.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServerSiteApplicationTests {

    @Resource
    public RedisTemplate redisTemplate;
    @Resource
    private RandomImgCodeUtils randomImgCodeUtils;
    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("dd","123456");
    }

    @Test
    void testFourBit(){
        String fourBit = RandomUtils.getDigitBits(4);
        System.out.println(fourBit);
    }
}
