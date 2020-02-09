package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/questiondetail/{id}")
    public String questiondetail (@PathVariable("id") Integer id, Model model){
        QuestionDto questionDto=questionService.findById(id);

//       累加阅读数
        questionService.incView(id);
        model.addAttribute("questionDto",questionDto);
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
