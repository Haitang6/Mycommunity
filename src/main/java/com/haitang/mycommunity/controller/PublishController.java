package com.haitang.mycommunity.controller;
import com.haitang.mycommunity.mapper.QuestionMapper;
import com.haitang.mycommunity.model.Question;
import com.haitang.mycommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(HttpServletRequest request,Model model){
         User user=(User)request.getSession().getAttribute("user");
         if (user != null){
             return "redirect:/";
         }
        return "publish";
    }

    @PostMapping("/addquestion")
    public String doPublish(Question question, HttpServletRequest request,Model model){

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());

        if (question.getTitle()==null || question.getTitle()==""){
            model.addAttribute("error","请填写标题");
            return "publish";
        }
        if (question.getDescription()==null || question.getDescription()==""){
            model.addAttribute("error","请填写描述");
            return "publish";
        }
        if (question.getTag()==null || question.getTag()==""){
            model.addAttribute("error","请填写标签");
            return "publish";
        }


        User user =(User) request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error","用户未登陆");
            return "publish";
        }
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setViewCount(0);
        question.setLikeCount(0);
        question.setCommentCount(0);
        questionMapper.addQuestion(question);
        model.addAttribute("question",question);
        model.addAttribute("test","test");
        return "redirect:/";
    }
}
