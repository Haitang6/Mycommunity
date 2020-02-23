package com.haitang.mycommunity.mapper;

import com.haitang.mycommunity.model.Question;

import java.util.List;


public interface QuestionExtMapper {
    int incView(Question question);
    int incComment(Question question);
    List<Question> selectLikeTag (Question question);

    List<Question> selectLikeSearch(String search);
}
