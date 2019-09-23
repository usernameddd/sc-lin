package com.daizening.exceptionHandler;

import com.daizening.dto.ResuktBack;
import com.daizening.exception.UnloginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler(value = UnloginException.class)
    @ResponseBody
    public ResponseEntity<ResuktBack> unlogin(UnloginException e) {
        return new ResponseEntity(new ResuktBack(-1,"未登录", null), HttpStatus.UNAUTHORIZED);
    }
}
