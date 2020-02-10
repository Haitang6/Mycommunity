package com.haitang.mycommunity.mapper;

import com.haitang.mycommunity.model.Question;


public interface QuestionExtMapper {
    int incView(Question question);
    int incComment(Question question);
}
