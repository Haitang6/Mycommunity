package com.haitang.mycommunity.controller;

import com.haitang.mycommunity.dto.AccessTokenDto;
import com.haitang.mycommunity.dto.GithubUser;
import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.provide.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class OauthController {

    @Autowired
    GitHubProvider gitHubProvider;

    @Autowired
    private UserMapper userMapper;
    @Value("${github.client.id}")
    private String clientid;

    @Value("${github.client.secret}")
    private String clientsecret;

    @Value("${github.redirect.uri}")
    private String redirecturl;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state, HttpServletRequest request){

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
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountid(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);

            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

}
