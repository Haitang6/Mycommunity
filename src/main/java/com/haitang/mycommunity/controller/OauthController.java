package com.haitang.mycommunity.controller;
import com.haitang.mycommunity.dto.AccessTokenDto;
import com.haitang.mycommunity.dto.GithubUser;
import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.provide.GitHubProvider;
import com.haitang.mycommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class OauthController {

    @Autowired
    GitHubProvider gitHubProvider;
    @Autowired
    private UserService userService;
    @Value("${github.client.id}")
    private String clientid;

    @Value("${github.client.secret}")
    private String clientsecret;

    @Value("${github.redirect.uri}")
    private String redirecturl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientid);
        accessTokenDto.setClient_secret(clientsecret);
        accessTokenDto.setStatus(state);
        accessTokenDto.setRedirect_url(redirecturl);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        if (githubUser!=null){
            User user = new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.updateorCreate(user);
            request.getSession().setAttribute("user",githubUser);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout (HttpServletRequest request,HttpServletResponse response){

        request.getSession().removeAttribute("user");

        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
