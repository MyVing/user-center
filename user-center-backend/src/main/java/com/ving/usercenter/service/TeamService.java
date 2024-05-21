package com.ving.usercenter.service;

import com.ving.usercenter.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ving.usercenter.model.domain.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
* @author huawei
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-05-19 11:40:53
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     *
     * @param team
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    long addTeam(@RequestBody Team team, User loginUser);
}
