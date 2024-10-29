package com.dorm.author;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dorm.controller.vo.JsonResult;

import io.jsonwebtoken.SignatureException;


@RestControllerAdvice
public class PermissionHandler {

	@ExceptionHandler(value = { SignatureException.class })
    @ResponseBody
    public JsonResult authorizationException(SignatureException e){
        return JsonResult.error("403", e.getMessage());
    }
}
