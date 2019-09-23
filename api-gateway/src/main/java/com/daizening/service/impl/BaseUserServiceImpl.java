package com.daizening.service.impl;

import com.daizening.dao.BaseTokenRecordsDao;
import com.daizening.dao.BaseUserDao;
import com.daizening.dto.ResultData;
import com.daizening.enums.StatusEnums;
import com.daizening.model.base.BaseTokenRecords;
import com.daizening.model.base.BaseUser;
import com.daizening.security.config.TokenProperties;
import com.daizening.security.model.token.AccessToken;
import com.daizening.security.model.token.TokenFactory;
import com.daizening.service.BaseUserService;
import com.daizening.utils.AesEncryptUtils;
import com.daizening.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.crypto.dk.AesDkCrypto;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class BaseUserServiceImpl implements BaseUserService {

    @Resource
    private BaseUserDao baseUserDao;

    @Resource
    private BaseTokenRecordsDao baseTokenRecordsDao;

    @Autowired
    private TokenFactory tokenFactory;

    @Override
    public ResultData addUser(BaseUser baseUser) throws Exception {
        // 用户输入的信息： 1、用户名 2、密码
        // checkCode 的生成为根据用户名进行对称加密
        String checkCode = AesEncryptUtils.encrypt(AesEncryptUtils.encrypt(baseUser.getUsername() + "-" + baseUser.getPassword()));
        baseUser.setCheckCode(checkCode);
        int n = baseUserDao.add(baseUser);
        if (n > 0) {
            return new ResultData(StatusEnums.SUCCESS);
        }
        return new ResultData(StatusEnums.FAILED);
    }

    @Override
    public ResultData userLogin(BaseUser baseUser) throws Exception {
        BaseUser user = baseUserDao.findOne(baseUser.getUsername(), baseUser.getPassword());
        if (user == null) {
            return new ResultData(StatusEnums.FAILED, "账号或密码错误");
        }
        if (user.getStatus() == -1) {
            return new ResultData(StatusEnums.FAILED, "账号已被冻结，如需解封，请联系管理员");
        }
        String rightCheckCode = AesEncryptUtils.encrypt(AesEncryptUtils.encrypt(baseUser.getUsername() + "-" + baseUser.getPassword()));
        if (!user.getCheckCode().equals(rightCheckCode)) {
            return new ResultData(StatusEnums.FAILED, "用户密码已经遭到数据库层面的非法篡改");
        }
        AccessToken accessToken = tokenFactory.createAccessToken(baseUser);
        BaseTokenRecords baseTokenRecords = new BaseTokenRecords();
        baseTokenRecords.setUserId(user.getId());
        baseTokenRecords.setToken(accessToken.getToken());
        baseTokenRecords.setExpireTime(TimeUtils.date2Str(accessToken.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
        baseTokenRecords.setRefreshTime(TimeUtils.date2Str(accessToken.getRefreshTime(), "yyyy-MM-dd HH:mm:ss"));
        // 存
        int n = baseTokenRecordsDao.add(baseTokenRecords);
        if (n > 0) {
            return new ResultData(StatusEnums.SUCCESS, accessToken.getToken());
        }
        return new ResultData(StatusEnums.FAILED);
    }
}
