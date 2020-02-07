package com.haitang.mycommunity.controller;
import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.Question;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MyselfController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/myself/{action}")
    public String myself(@PathVariable(name = "action") String action,
                         HttpServletRequest request,
                         Model model){
        User user=(User)request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        if ("question".equals(action)){

            List<QuestionDto> questionDtos = questionService.findAllByUserid(user.getId());
            model.addAttribute("questionDtos",questionDtos);
            model.addAttribute("title","我的问题");
            model.addAttribute("titleName","question");
        }else if ("replies".equals(action)){

            model.addAttribute("title","我的回复");
            model.addAttribute("titleName","replies");
        }
        return "myself";
    }
}
