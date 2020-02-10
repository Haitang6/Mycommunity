package com.haitang.mycommunity.service;

import com.haitang.mycommunity.enums.CommentTypeEnum;
import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.exception.CustomizeException;
import com.haitang.mycommunity.mapper.CommentMapper;
import com.haitang.mycommunity.mapper.QuestionExtMapper;
import com.haitang.mycommunity.mapper.QuestionMapper;
import com.haitang.mycommunity.model.Comment;
import com.haitang.mycommunity.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    public void insert(Comment comment) {
        if (comment.getId() ==0 ||comment.getId() == null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_OR_REPLAY_NOT_SELECT);
        }
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

        }else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
        }
    }




}
