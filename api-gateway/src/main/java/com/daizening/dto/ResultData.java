package com.daizening.dto;

import com.daizening.enums.StatusEnums;
import lombok.Data;

@Data
public class ResultData<T> {

    private int code;

    private String info;

    private T data;


    public ResultData(StatusEnums statusEnums, T data) {
        this.code = statusEnums.getCode();
        this.info = statusEnums.getInfo();
        this.data = data;
    }

    public ResultData(StatusEnums statusEnums) {
        this.code = statusEnums.getCode();
        this.info = statusEnums.getInfo();
    }

    public ResultData(int code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }
}
