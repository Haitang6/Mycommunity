package com.haitang.mycommunity.mapper;

import com.haitang.mycommunity.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,view_count,comment_count,like_count,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{viewCount},#{commentCount},#{likeCount},#{tag})")
    void addQuestion(Question question);

    @Select("select * from question ")
    List<Question> findAll();

    @Select("select * from question where creator = #{userid}")
    List<Question> findAllByUserid(@Param("userid") Integer userid);

    @Select("select * from question where id = #{id}")
    Question findById(@Param("id") Integer id);

    @Update("update question set title = #{title},description = #{description},tag=#{tag},gmt_modified=#{gmtModified} where id=#{id} ")
    void updateQuestion(Question question);
}
