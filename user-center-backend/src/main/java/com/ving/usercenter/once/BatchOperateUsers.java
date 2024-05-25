package com.ving.usercenter.once;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import com.ving.usercenter.mapper.UserMapper;
import com.ving.usercenter.model.domain.User;
import com.ving.usercenter.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @Author ving
 * @Date 2024/5/13 23:40
 */
@Component
public class BatchOperateUsers {

    @Resource
    private UserService userService;

    //CPU密集型：分配的核心线程数 = CPU - 1
    //IO密集型： 分配的核心线程数可以大于 CPU 核数
    private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10000));


    /**
     * 批量插入用户
     */
   // @Scheduled(initialDelay =  5000,fixedRate = Long.MAX_VALUE )
    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        int batchSize = 5000;
        //分十组
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();

                user.setUsername("假Ving");
                user.setUserAccount("fakeVing");
                user.setAvatarUrl("https://tse4-mm.cn.bing.net/th/id/OIP-C.ladu3x_gSCPrngRnxLYLaQAAAA?w=220&h=220&c=7&r=0&o=5&dpr=1.3&pid=1.7");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("1234");
                user.setEmail("134@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setSchoolCode("3122004611");
                user.setTags("[]");
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName:"+Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            },executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 批量删除用户
     */
    //@Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doDeleteUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int DELETE_NUM = 100000;
        int batchSize = 5000;
        //分十组
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<Long> userIdList = new ArrayList<>();
            while (true) {
                j++;
                userIdList.add((long) j);
                if (j % batchSize == 0) {
                    break;
                }
            }
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName:" + Thread.currentThread().getName());
                userService.removeByIds(userIdList);
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }



}
