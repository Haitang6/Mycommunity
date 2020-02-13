package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.CommentDto;
import com.haitang.mycommunity.dto.CommentShowDto;
import com.haitang.mycommunity.dto.ResultDto;
import com.haitang.mycommunity.enums.CommentTypeEnum;
import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.mapper.CommentMapper;
import com.haitang.mycommunity.model.Comment;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private
    CommentService commentService;

    /*
    * 对问题进行评论
    * */
    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentDto commentDto, HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");

        if (user == null) {
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentDto.getContext() == null || StringUtils.isBlank(commentDto.getContext())){
            return ResultDto.errorOf(CustomizeErrorCode.CONTEXT_IS_NULL);
        }
        Comment comment=new Comment();
        comment.setContext(commentDto.getContext());
        comment.setParentId(commentDto.getParentId());
        comment.setType(commentDto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0);
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        return ResultDto.success();
    }

    /*
    * 对评论进行回复展示
    *
    * */

    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDto<List<CommentShowDto>> showDoubleComment(@PathVariable(name = "id") Integer id ){

        List <CommentShowDto> commentShowDtos = commentService.findByTargetId(id, CommentTypeEnum.COMMENT);


        return ResultDto.successWithData(commentShowDtos);
    }





}
