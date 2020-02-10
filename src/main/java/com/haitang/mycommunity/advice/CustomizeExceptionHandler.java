package com.haitang.mycommunity.advice;

import com.alibaba.fastjson.JSON;
import com.haitang.mycommunity.dto.ResultDto;
import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex, Model model, HttpServletRequest request, HttpServletResponse response){

        String contentType = request.getContentType();
        if ("application/json".equals(contentType)){

            ResultDto resultDto=null;
            if (ex instanceof CustomizeException){
                resultDto = ResultDto.errorOf((CustomizeException) ex);
            }else {
                resultDto = ResultDto.errorOf(CustomizeErrorCode.SYSTEM_ERROR);

            }
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();

            }catch (IOException ioe){

            }
            return null;
        }else {
            if (ex instanceof CustomizeException){
                model.addAttribute("message",ex.getMessage());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR);
            }
            return new ModelAndView("error");
        }

    }


}
