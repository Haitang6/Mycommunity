package com.haitang.mycommunity.dto;

import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.exception.CustomizeException;

public class ResultDto {
    private Integer code;
    private String message;

    public static ResultDto errorOf(Integer coed,String message){
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(coed);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeErrorCode noLogin) {

        return errorOf(noLogin.getCoed(),noLogin.getMessage());
    }

    public static ResultDto success(){
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
