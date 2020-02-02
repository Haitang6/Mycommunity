package com.haitang.mycommunity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(@RequestParam(name = "age") int age , Model model) {

        model.addAttribute("myage",age);
        return "index";
    }
}
