package com.haitang.mycommunity.exception;

public class CustomizeException extends RuntimeException {

    private Integer code;
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {

        this.message = errorCode.getMessage();
        this.code=errorCode.getCoed();
    }

    public CustomizeException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
