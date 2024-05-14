package com.ving.usercenter.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserCenterTableInfo {

    /**
     * 学号
     */
    @ExcelProperty("学号")
    private String schoolCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String username;
}
