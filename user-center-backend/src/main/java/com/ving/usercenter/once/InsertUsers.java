package com.ving.usercenter.once;
import java.util.Date;

import com.ving.usercenter.mapper.UserMapper;
import com.ving.usercenter.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @Author ving
 * @Date 2024/5/13 23:40
 */
@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    /**
     * 批量插入用户
     */
 //   @Scheduled(initialDelay =  5000,fixedRate = Long.MAX_VALUE )
    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final  int INSERT_NUM = 10000000;
        for(int i = 0 ; i < INSERT_NUM;i++){
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

            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    public static void main(String[] args) {
        new InsertUsers().doInsertUsers();
    }
}
