package com.silencecorner.jpa.dto;

import lombok.Data;
import lombok.Getter;

/**
 * @author hai
 * @description
 * @email hilin2333@gmail.com
 * @date 30/12/2017 10:53 PM
 */
// 类中不能有static属性
    @Data
@Getter
public class UserDto {
    public UserDto(String uid, Integer status, String username) {
        this.uid = uid;
        this.enable = status;
        this.username = username;
    }

    private final String uid;

    private final Integer enable;

    private final String username;

}
