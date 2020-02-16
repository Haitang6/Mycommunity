package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.NotificationDto;
import com.haitang.mycommunity.enums.NotificationTypeEnum;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notified(@PathVariable(name = "id") Integer id ,
                           HttpServletRequest request,
                           Model model){
        User user =(User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        NotificationDto notificationDto=notificationService.read(id,user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDto.getType() ||
        NotificationTypeEnum.REPLY_QUESTION.getType()== notificationDto.getType()){
            return "redirect:/questiondetail/"+notificationDto.getOuterid();
        }else {
            return "redirect:/";
        }
    }
}
