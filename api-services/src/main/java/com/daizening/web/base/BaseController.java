package com.daizening.web.base;

import com.daizening.exception.UnloginException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class BaseController {

    @Autowired
    private HttpServletRequest request;


    public String getUserId(){
        String userId = request.getHeader("userId");
        if (null == userId) {
            throw new UnloginException("未登录异常");
        }

        System.out.println("从request中获得的userId为：" + request.getHeader("userId"));
        return (String) request.getHeader("userId");
    }
}
