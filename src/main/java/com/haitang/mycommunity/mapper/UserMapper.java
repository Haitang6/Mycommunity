package com.haitang.mycommunity.mapper;
import com.haitang.mycommunity.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user (account_id,name,token,gmt_modified,gmt_create,avatar_url) " +
            "values(#{accountId},#{name},#{token},#{gmtModified},#{gmtCreate},#{avatarUrl})")
    public void insert (User user);

    @Select("select * from user where token = #{token}")
    User  findBytoken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer creator);

    @Select("select * from user where account_id = #{accountId}")
    User findByAcoountid (@Param("accountId") String accountId);

    @Update("update user set name=#{name},token=#{token},gmt_create=#{gmtCreate},avatar_url=#{avatarUrl} where account_id=#{accountId}")
    void update(User user);
}
