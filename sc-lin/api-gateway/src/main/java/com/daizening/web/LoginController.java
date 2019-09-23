package com.daizening.web;

import com.daizening.constant.Const;
import com.daizening.dto.ResultData;
import com.daizening.enums.StatusEnums;
import com.daizening.model.ParamsError;
import com.daizening.model.base.BaseTokenRecords;
import com.daizening.model.base.BaseUser;
import com.daizening.security.model.UserContext;
import com.daizening.security.model.token.AccessToken;
import com.daizening.security.model.token.TokenFactory;
import com.daizening.service.BaseTokenRecordsService;
import com.daizening.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.netty.http.server.HttpServerRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private TokenFactory tokenFactory;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private BaseTokenRecordsService baseTokenRecordsService;

    @PostMapping(value = "add")
    public ResultData addUser(@RequestBody BaseUser baseUser) throws Exception {
        return baseUserService.addUser(baseUser);
    }

    @PostMapping(value = "login")
    public ResultData login (@RequestBody @Valid  BaseUser baseUser) throws Exception {
        return baseUserService.userLogin(baseUser);
    }

    @GetMapping(value = "refreshToken")
    public ResultData refreshToken (ServerWebExchange exchange) {
        // 从header中取出token信息，校验是否是系统颁发的，生成新的token
        // 策略1： 生成token的时候，将用户id和token、以及有效期时间（2t）存储在mysql数据库中
        // 当用户携带已过期的jwt来访问资源时，去查数据库，根据用户id，token，去查时间有没有超过有效期
        // 若没超过，返回新的jwt给用户，若超过，返回jwt失效，用户重新登录
//        String token = request.getHeaders().getFirst(Const.TOKEN_HEADER_PARAM);
        String token = exchange.getRequest().getHeaders().getFirst(Const.TOKEN_HEADER_PARAM);
        if (token == null || token.equals("")) {
            return new ResultData(StatusEnums.FAILED);
        }
        return baseTokenRecordsService.refreshToken(token);
    }
}
