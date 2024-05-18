package com.ving.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ving.usercenter.mapper.UserMapper;
import com.ving.usercenter.model.domain.User;
import com.ving.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 *
 * @Author ving
 * @Date 2024/5/16 0:28
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;


    //重点用户
    List<Long> mainUserList = Arrays.asList(1l);

    //每天执行，预热推荐用户
    @Scheduled(cron = "0 4 0 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("partner:precachejob:docache:lock");
        try {
            //只有线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock"+Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format("partner:user:recommend:%s", userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

                    //没有缓存，需要查到后写缓存
                    try {
                        redisTemplate.opsForValue().set(redisKey, userPage, 30000, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }

            }
        } catch (InterruptedException e) {
           log.error("doCacheRecommendUser error",e);
        }finally {
            //只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock"+Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
