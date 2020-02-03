package com.haitang.mycommunity.provide;

import com.alibaba.fastjson.JSON;
import com.haitang.mycommunity.dto.AccessTokenDto;
import com.haitang.mycommunity.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {

    public String getAccessToken(AccessTokenDto accessTokenDto){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
          try (Response response = client.newCall(request).execute()) {
              String string = response.body().string();
              String accesstoken=string.split("&")[0].split("=")[1];
              return accesstoken;
          } catch (Exception e) {
              e.printStackTrace();
        }
        return null;
    }


    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+accessToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String string=response.body().string();
                GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
                return githubUser;
            } catch (IOException e) {
                return null;
            }

    }
}
