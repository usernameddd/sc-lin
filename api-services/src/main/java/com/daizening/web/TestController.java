package com.daizening.web;

import com.daizening.web.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController  extends BaseController {

    @RequestMapping("/hello")
    public String hello() {
        return "say hello to every one!" + getUserId();
    }
}
