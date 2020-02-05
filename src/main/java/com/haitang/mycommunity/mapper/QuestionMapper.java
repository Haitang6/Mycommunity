package com.haitang.mycommunity.mapper;

import com.haitang.mycommunity.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,view_count,comment_count,like_count,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{viewCount},#{commentCount},#{likeCount},#{tag})")
    void addQuestion(Question question);
}
