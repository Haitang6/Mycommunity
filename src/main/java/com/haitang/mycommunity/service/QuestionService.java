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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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
        List<Question> questions = questionMapper.selectByExample(new QuestionExample());

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
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        updateQuestion.setViewCount(question.getViewCount()+1);
//        QuestionExample questionExample=new QuestionExample();
//        questionExample.createCriteria()
//                .andIdEqualTo(id);
//        questionMapper.updateByExampleSelective(updateQuestion,questionExample);
        Question question=new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
