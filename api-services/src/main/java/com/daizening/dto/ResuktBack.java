package com.daizening.dto;

import lombok.Data;

@Data
public class ResuktBack<T> {
    private int code;

    private String info;

    private T data;

    public ResuktBack(int code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }
}
