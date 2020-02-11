package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.CommentDto;
import com.haitang.mycommunity.dto.ResultDto;
import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.mapper.CommentMapper;
import com.haitang.mycommunity.model.Comment;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private
    CommentService commentService;

    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentDto commentDto, HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");

        if (user == null) {
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment=new Comment();
        comment.setContext(commentDto.getContext());
        comment.setParentId(commentDto.getParentId());
        comment.setType(commentDto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0);
        comment.setCommentator(289);
        commentService.insert(comment);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        return ResultDto.success();
    }
}
