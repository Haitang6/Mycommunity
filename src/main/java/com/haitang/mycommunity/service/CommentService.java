package com.haitang.mycommunity.service;

import com.haitang.mycommunity.dto.CommentShowDto;
import com.haitang.mycommunity.enums.CommentTypeEnum;
import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.exception.CustomizeException;
import com.haitang.mycommunity.mapper.*;
import com.haitang.mycommunity.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentExtMapper commentExtMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        //要评论或回复的内容不存在了
        if (comment.getParentId() ==0 ||comment.getParentId() == null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_OR_REPLAY_NOT_SELECT);
        }
        //评论或回复存在的情况
        if (comment.getType() == null || !CommentTypeEnum.isExits(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_OF_COMMENT_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
//            回复评论
            Comment comment1 = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (comment1 == null){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOND);
            }
            commentMapper.insert(comment);

//          添加评论的评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incComment(parentComment);

        }else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
        }
    }

    public List<CommentShowDto> findByTargetId(Integer id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
        .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_Create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
//        查找出一个问题下面的所有评论者,去除重复者
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        List <Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);

        List<User> users = userMapper.selectByExample(userExample);

//        把评论和用户对应起来
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        List<CommentShowDto> commentShowDtos = comments.stream().map(comment -> {
            CommentShowDto commentShowDto = new CommentShowDto();
            BeanUtils.copyProperties(comment,commentShowDto);
            commentShowDto.setUser(userMap.get(comment.getCommentator()));
            return commentShowDto;
        }).collect(Collectors.toList());

        return commentShowDtos;

    }
}
