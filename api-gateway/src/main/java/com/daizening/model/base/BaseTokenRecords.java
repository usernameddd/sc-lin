package com.daizening.model.base;

import lombok.Data;


@Data
public class BaseTokenRecords {
    private int id;

    private int userId;

    private String token;

    private String expireTime;

    private String refreshTime;
}
