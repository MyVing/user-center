package com.ving.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登陆请求体
 *
 * @Author ving
 * @Date 2024/1/27 20:10
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 2559771582689471959L;

    /**
     * id
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;

}
