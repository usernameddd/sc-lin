package com.daizening.exceptionHandler;

import com.daizening.dto.ResultData;
import com.daizening.enums.StatusEnums;
import com.daizening.model.ParamsError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ResultData> tokenError (Exception e) {
        System.out.println(e.getMessage());
        if (e instanceof UnsupportedJwtException || e instanceof  MalformedJwtException || e instanceof IllegalArgumentException || e instanceof SignatureException) {
            return new ResponseEntity(new ResultData(StatusEnums.TOKEN_ERROR, "无效/错误的token"), HttpStatus.UNAUTHORIZED);
        } else if (e instanceof ExpiredJwtException) {
            return new ResponseEntity(new ResultData(StatusEnums.TOKEN_EXPIRED, "token已过期/失效"), HttpStatus.UNAUTHORIZED);
        } else if (e instanceof WebExchangeBindException) {
            BindingResult result = ((WebExchangeBindException)e).getBindingResult();
            List<ParamsError> list = new ArrayList();
            for (FieldError error: result.getFieldErrors()) {
                ParamsError paramsError = new ParamsError();
                paramsError.setField(error.getField());
                paramsError.setMessage(error.getDefaultMessage());
                list.add(paramsError);
            }
            return new ResponseEntity(new ResultData(StatusEnums.PARAMS_ERROR, list), HttpStatus.OK);
        }
        return new ResponseEntity(new ResultData(StatusEnums.FAILED, e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
