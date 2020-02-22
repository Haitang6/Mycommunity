package com.haitang.mycommunity.controller;
import com.haitang.mycommunity.dto.QuestionDto;
import com.haitang.mycommunity.schedule.HotTagCache;
import com.haitang.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;
    @Autowired
    HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(HttpServletRequest request,Model model) {

        List<QuestionDto> questionDtos =questionService.findAll();
        model.addAttribute("questionDtos",questionDtos);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("tags",tags);

        return "index";
    }

    @GetMapping("/test")
    public String test(Model model){
        return "test";
    }

}
