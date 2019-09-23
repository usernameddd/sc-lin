package com.daizening.dao;

import com.daizening.dto.ResultData;
import com.daizening.model.base.BaseTokenRecords;
import org.apache.ibatis.annotations.Param;

public interface BaseTokenRecordsDao {

    int add(@Param("baseTokenRecords") BaseTokenRecords baseTokenRecords);

    BaseTokenRecords findOne(@Param("token") String token);
}
