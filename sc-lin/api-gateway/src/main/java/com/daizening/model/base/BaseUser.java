package com.daizening.model.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
public class BaseUser {
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String checkCode;

    // status： -1 -> 账号已冻结， 1 -> 账号正常
    private int status = 1;

    public BaseUser(int id) {
        this.id = id;
    }

    public BaseUser(int id, @NotNull String username, @NotNull String password, String checkCode, int status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.checkCode = checkCode;
        this.status = status;
    }

    public BaseUser() {
    }

    // 检测账号是否是冻结状态
    public boolean isAccountOk() {
        if (this.status == -1) {
           return false;
        }
        return true;
    }
}
