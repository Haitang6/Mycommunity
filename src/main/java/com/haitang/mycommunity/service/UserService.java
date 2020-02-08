package com.haitang.mycommunity.service;

import com.haitang.mycommunity.mapper.UserMapper;
import com.haitang.mycommunity.model.User;
import com.haitang.mycommunity.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public String updateorCreate(User user){

        String accountId=user.getAccountId();
        UserExample userExample=new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        //插入
        if (users.size() == 0){
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
        //更新
            User user1 = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setName(user.getName());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());

            UserExample userExample1=new UserExample();
            userExample1.createCriteria()
                    .andIdEqualTo(user1.getId());
            userMapper.updateByExampleSelective(updateUser,userExample1);
//            userMapper.update(user1);
        }
        return null;
    }


}
