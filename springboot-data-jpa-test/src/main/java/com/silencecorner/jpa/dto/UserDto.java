package com.silencecorner.jpa.dto;

import lombok.Value;

/**
 * @author hai
 * @description
 * @email hilin2333@gmail.com
 * @date 30/12/2017 10:53 PM
 */
// 类中不能有static属性
@Value
public class UserDto {
    public UserDto(String uid, Integer status, String username) {
        this.uid = uid;
        this.enable = status;
        this.username = username;
    }

    String uid;

    Integer enable;

    String username;

}
