package com.haitang.mycommunity.mapper;
import com.haitang.mycommunity.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user (account_id,name,token,gmt_modified,gmt_create) " +
            "values(#{accountid},#{name},#{token},#{gmtModified},#{gmtCreate})")
    public void insert (User user);

    @Select("select * from user where token = #{token}")
    User  findBytoken(@Param("token") String token);
}
