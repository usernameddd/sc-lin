package com.daizening.service;

import com.daizening.dto.ResultData;
import com.daizening.model.base.BaseTokenRecords;

public interface BaseTokenRecordsService {
    ResultData addRecord(BaseTokenRecords baseTokenRecords);

    ResultData refreshToken(String token);
}
