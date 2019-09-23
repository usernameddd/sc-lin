package com.daizening.dao;

import com.daizening.model.base.BaseUser;
import org.apache.ibatis.annotations.Param;

public interface BaseUserDao {
    int add(@Param("baseUser") BaseUser baseUser);

    int del(int id);

    BaseUser findOne(@Param("username") String username, @Param("password") String password);

}
