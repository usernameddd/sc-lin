package com.daizening.security.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;

@Data
public class UserContext {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private  String password;

//    public UserContext(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }

//    public static UserContext create(String username) {
//        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is black: " + username);
//        return new UserContext(username);
//    }

//    public String getUsername() {
//        return username;
//    }
//    public String getPassword() {
//        return password;
//    }
}
