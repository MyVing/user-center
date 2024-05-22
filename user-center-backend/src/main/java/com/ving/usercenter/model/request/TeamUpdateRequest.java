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
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 2559771582689471959L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;


    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0-公开 , 1-私有,2-加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

}
