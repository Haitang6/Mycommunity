package com.haitang.mycommunity.service;
import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.exception.CustomizeErrorCode;
import com.haitang.mycommunity.exception.CustomizeException;
import com.haitang.mycommunity.mapper.QuestionExtMapper;
import com.haitang.mycommunity.mapper.QuestionMapper;
import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.Question;
import com.haitang.mycommunity.model.QuestionExample;
import com.haitang.mycommunity.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;

    public void createOrUpdatequestion(Question question) {
        Question question1 = questionMapper.selectByPrimaryKey(question.getId());
        if (question1 == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());
            QuestionExample questionExample=new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (update !=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public List<QuestionDto> findAll(){
        List<QuestionDto> questionDtos=new ArrayList<QuestionDto>();

        QuestionExample questionExample= new QuestionExample();
        questionExample.setOrderByClause("gmt_Create desc");
        List<Question> questions = questionMapper.selectByExample(questionExample);
        for (Question question:questions){
            Integer creator = question.getCreator();
            User user = userMapper.selectByPrimaryKey(creator);
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        return questionDtos;

    }

    public List<QuestionDto> findAllByUserid(Integer userid){
        List<QuestionDto> questionDtos=new ArrayList<QuestionDto>();

        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userid);
        List<Question> questions = questionMapper.selectByExample(questionExample);
        for (Question question:questions){
            Integer creator = question.getCreator();
            User user = userMapper.selectByPrimaryKey(creator);
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        return questionDtos;

    }

    public QuestionDto findById(Integer id) {
        QuestionDto questionDto=new QuestionDto();
        Question question=questionMapper.selectByPrimaryKey(id);

        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        BeanUtils.copyProperties(question,questionDto);
        questionDto.setUser(user);
        return questionDto;
    }

    public void incView(Integer id) {
        Question question=new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDto> selectLikeTag(QuestionDto questionDto) {
        if (StringUtils.isBlank(questionDto.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDto.getTag(), ',');
        String regextTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(questionDto.getId());
        question.setTag(regextTag);
        List<Question> questions = questionExtMapper.selectLikeTag(question);
        List<QuestionDto> questionDtos = questions.stream().map(q -> {
            QuestionDto questionDto1=new QuestionDto();
            BeanUtils.copyProperties(q,questionDto1);
            return questionDto1;
        }).collect(Collectors.toList());
        return questionDtos;
    }
}
