package com.ving.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍请求体
 *
 * @Author ving
 * @Date 2024/1/27 20:10
 */
@Data
public class TeamQuitRequest implements Serializable {


    private static final long serialVersionUID = 5692554240649196869L;
    /**
     * id
     */
    private Long teamId;


}
