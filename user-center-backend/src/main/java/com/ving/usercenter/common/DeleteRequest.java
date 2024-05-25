package com.ving.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求
 * @Author ving
 * @Date 2024/5/24 11:45
 */
@Data
public class DeleteRequest implements Serializable {


    private static final long serialVersionUID = -324486029170186371L;

    private long id;
}
