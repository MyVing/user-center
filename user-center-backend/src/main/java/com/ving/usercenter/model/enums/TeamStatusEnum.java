package com.ving.usercenter.model.enums;

import io.swagger.models.auth.In;

/**
 * @Author ving
 * @Date 2024/5/21 16:55
 */
public enum TeamStatusEnum {

    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    SECRET(2,"加密");

    private int value;

    private String text;

    public static TeamStatusEnum getEnumByValue(Integer value){
        if(value == null){
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if(teamStatusEnum.getvalue() == value){
                return teamStatusEnum;
            }
        }
        return null;
    }
    

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getvalue() {
        return value;
    }

    public void setvalue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
