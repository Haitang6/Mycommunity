package com.haitang.mycommunity.exception;

import org.omg.PortableInterceptor.INACTIVE;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    NO_LOGIN(2001,"未登录"),
    QUESTION_NOT_FOUND(2002,"你查找的问题不存在，看看其他问题吧"),
    COMMENT_OR_REPLAY_NOT_SELECT(2003,"未选中评论或者回复"),
    SYSTEM_ERROR(2004,"服务器不开心，稍后再来"),
    TYPE_OF_COMMENT_WRONG(2005,"评论类型错误"),
    COMMENT_NOT_FOND(2006,"评论不存在"),
    CONTEXT_IS_NULL(2007,"评论内容为空"),
    READ_NITIFICATION_FAIL(2008,"兄弟这是别人的通知"),
    NOTIFICATION_NOT_FOND(2009,"通知不翼而飞了");
    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCoed() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
