package com.haitang.mycommunity.service;

import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public String updateorCreate(User user){

        String accountId=user.getAccountId();
        User user1 = userMapper.findByAcoountid(accountId);
        //插入
        if (user1 == null){
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
        //更新
            user1.setGmtModified(System.currentTimeMillis());
            user1.setName(user.getName());
            user1.setAvatarUrl(user.getAvatarUrl());
            user1.setToken(user.getToken());
            userMapper.update(user1);
        }
        return null;
    }


}
