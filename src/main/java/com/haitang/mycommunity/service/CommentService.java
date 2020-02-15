package com.haitang.mycommunity.service;

import com.haitang.mycommunity.dto.CommentShowDto;
import com.haitang.mycommunity.enums.CommentTypeEnum;
import com.haitang.mycommunity.enums.NotificationStatusEnum;
import com.haitang.mycommunity.enums.NotificationTypeEnum;
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
    @Autowired
    NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator) {
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
//            通知功能
            createNotify(comment,commentator.getName(),comment1.getContext(), NotificationTypeEnum.REPLY_COMMENT, comment1.getCommentator(), comment1.getParentId());


        }else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
//             添加回复数
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
//            通知功能
            createNotify(comment,commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION, question.getCreator(), question.getId());
        }
    }

    /*
    * comment 对应评论
    * commentor 父评论或者问题的发布者
    * */
    private void createNotify(Comment comment, String notifierName, String outerTitle, NotificationTypeEnum notificationTypeEnum, Integer commentator, Integer outerid) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setNotifier(comment.getCommentator());//这个评论的评论者
        notification.setOuterid(outerid);//回复的问题id
        notification.setReceiver(commentator);//这个评论的父级评论者或发布者
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
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


//        获取评论人转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
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
