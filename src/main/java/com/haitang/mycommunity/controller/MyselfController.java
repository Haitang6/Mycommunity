package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.NotificationDto;
import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.service.NotificationService;
import com.haitang.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MyselfController {

    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    @GetMapping("/myself/{action}")
    public String myself(@PathVariable(name = "action") String action,
                         HttpServletRequest request,
                         Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("question".equals(action)) {
            List<QuestionDto> questionDtos = questionService.findAllByUserid(user.getId());
            model.addAttribute("questionDtos", questionDtos);
            model.addAttribute("title", "我的问题");
            model.addAttribute("titleName", "question");
        } else if ("replies".equals(action)) {
            List<NotificationDto> notificationDtos = notificationService.list(user.getId());
            model.addAttribute("title", "我的回复");
            model.addAttribute("titleName", "replies");
            model.addAttribute("notificationDtos", notificationDtos);
            return "myself";
        }
        return "myself";
    }
}
