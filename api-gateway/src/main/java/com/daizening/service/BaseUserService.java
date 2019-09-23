package com.daizening.service;

import com.daizening.dto.ResultData;
import com.daizening.model.base.BaseUser;

public interface BaseUserService {

    ResultData addUser(BaseUser baseUser) throws Exception;

    ResultData userLogin(BaseUser baseUser) throws Exception;
}
