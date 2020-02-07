package com.haitang.mycommunity.service;

import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.mapper.QuestionMapper;
import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.Question;
import com.haitang.mycommunity.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;


    public List<QuestionDto> findAll(){
        List<QuestionDto> questionDtos=new ArrayList<QuestionDto>();
        List<Question> questions = questionMapper.findAll();
        for (Question question:questions){
            Integer creator = question.getCreator();
            User user = userMapper.findById(creator);
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        return questionDtos;

    }


    public List<QuestionDto> findAllByUserid(Integer userid){
        List<QuestionDto> questionDtos=new ArrayList<QuestionDto>();
        List<Question> questions = questionMapper.findAllByUserid(userid);
        for (Question question:questions){
            Integer creator = question.getCreator();
            User user = userMapper.findById(creator);
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            System.out.println(questionDto);
            questionDtos.add(questionDto);
        }
        return questionDtos;

    }
}
