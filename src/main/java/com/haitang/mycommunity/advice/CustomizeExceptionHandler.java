package com.haitang.mycommunity.advice;

import com.haitang.mycommunity.exception.CustomizeException;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handel( Throwable ex, Model model){
        if (ex instanceof CustomizeException){
            model.addAttribute("message",ex.getMessage());
        }else {
            model.addAttribute("message","服务器冒烟了，请稍后再试");
        }
        return new ModelAndView("error");
    }

}
