package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.CommentShowDto;
import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.enums.CommentTypeEnum;
import com.haitang.mycommunity.model.Question;
import com.haitang.mycommunity.service.CommentService;
import com.haitang.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;

    @GetMapping("/questiondetail/{id}")
    public String questiondetail (@PathVariable("id") Integer id, Model model){
        QuestionDto questionDto=questionService.findById(id);
//       累加阅读数
        questionService.incView(id);
//        查询相关问题
        List<QuestionDto> relateQuestions=questionService.selectLikeTag(questionDto);

        List <CommentShowDto> commentShowDtos = commentService.findByTargetId(id, CommentTypeEnum.QUESTION);

        model.addAttribute("questionDto",questionDto);
        model.addAttribute("commentShowDto",commentShowDtos);
        model.addAttribute("relateQuestions",relateQuestions);
        return "questiondetail";
    }
    @GetMapping("/changequestion/{id}")
    public String changequestion (@PathVariable("id") Integer id,Model model){
        QuestionDto questionDto = questionService.findById(id);
        model.addAttribute("title",questionDto.getTitle());
        model.addAttribute("description",questionDto.getDescription());
        model.addAttribute("tag",questionDto.getTag());
        return "publish";
    }
}
