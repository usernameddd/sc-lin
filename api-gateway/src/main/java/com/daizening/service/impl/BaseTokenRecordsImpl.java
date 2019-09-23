package com.daizening.service.impl;

import com.daizening.dao.BaseTokenRecordsDao;
import com.daizening.dto.ResultData;
import com.daizening.enums.StatusEnums;
import com.daizening.model.base.BaseTokenRecords;
import com.daizening.model.base.BaseUser;
import com.daizening.security.model.token.AccessToken;
import com.daizening.security.model.token.TokenFactory;
import com.daizening.service.BaseTokenRecordsService;
import com.daizening.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BaseTokenRecordsImpl implements BaseTokenRecordsService {

    @Autowired
    private TokenFactory tokenFactory;

    @Resource
    private BaseTokenRecordsDao baseTokenRecordsDao;

    @Override
    public ResultData addRecord(BaseTokenRecords baseTokenRecords) {
        int n = baseTokenRecordsDao.add(baseTokenRecords);
        if (n > 0) {
            return new ResultData(StatusEnums.SUCCESS);
        }
        return new ResultData(StatusEnums.FAILED);
    }

    @Override
    public ResultData refreshToken(String token) {
        BaseTokenRecords baseTokenRecords = baseTokenRecordsDao.findOne(token);
        Date now = new Date();
        if (null == baseTokenRecords) {
            return new ResultData(StatusEnums.FAILED, "token有误");
        } else if (!(TimeUtils.beforeEquals(baseTokenRecords.getExpireTime(), now) && TimeUtils.afterEquals(baseTokenRecords.getRefreshTime(),now))) {
            return new ResultData(StatusEnums.FAILED, "token未过期，无需刷新token！");
        }
        AccessToken accessToken = tokenFactory.createAccessToken(new BaseUser(baseTokenRecords.getUserId()));
        BaseTokenRecords newBaseTokenRecords = new BaseTokenRecords();
        newBaseTokenRecords.setUserId(baseTokenRecords.getUserId());
        newBaseTokenRecords.setToken(accessToken.getToken());
        newBaseTokenRecords.setExpireTime(TimeUtils.date2Str(accessToken.getExpireTime(), "yyyy-MM-dd HH:mm:ss"));
        newBaseTokenRecords.setRefreshTime(TimeUtils.date2Str(accessToken.getRefreshTime(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getExpireTime());
        // 存
        int n = baseTokenRecordsDao.add(baseTokenRecords);
        if (n > 0) {
            return new ResultData(StatusEnums.SUCCESS, accessToken.getToken());
        }
        return new ResultData(StatusEnums.FAILED);
    }

}
