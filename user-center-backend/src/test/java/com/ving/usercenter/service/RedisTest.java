package com.ving.usercenter.service;

import com.ving.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @Author ving
 * @Date 2024/5/15 22:22
 */

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //增
        valueOperations.set("vingString","ving");
        valueOperations.set("vingInteger",1);
        valueOperations.set("vingDouble",2.0);

        User user = new User();
        user.setId(1);
        valueOperations.set("vingUser",user);

        //查
        Object ving = valueOperations.get("vingString");
        Assertions.assertTrue("ving".equals((String)ving));
        ving =  valueOperations.get("vingInteger");
        Assertions.assertTrue(1 == ((Integer)ving));
        ving = valueOperations.get("vingDouble");
        Assertions.assertTrue(2.0 == ((Double)ving));
         valueOperations.get("vingUser");
        System.out.println(valueOperations.get("vingUser"));

        redisTemplate.delete("vingString");

    }

}
