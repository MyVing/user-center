package com.ving.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ving.usercenter.model.domain.Team;
import com.ving.usercenter.model.dto.TeamQuery;
import com.ving.usercenter.model.request.TeamJoinRequest;
import com.ving.usercenter.model.request.TeamQuitRequest;
import com.ving.usercenter.model.request.TeamUpdateRequest;
import com.ving.usercenter.model.vo.TeamUserVO;


import java.util.List;

/**
* @author 86157
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-01-11 14:52:00
*/
public interface TeamService extends IService<Team> {

    Long addTeam(Team team);

    List<TeamUserVO> listTeams(TeamQuery teamQuery);

    boolean updateTeam(TeamUpdateRequest teamUpdateRequest);

    boolean joinTeam(TeamJoinRequest teamJoinRequest);

    boolean quitTeam(TeamQuitRequest teamQuitRequest);

    boolean deleteTeam(long id);
}
