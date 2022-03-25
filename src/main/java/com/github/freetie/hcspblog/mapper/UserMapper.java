package com.github.freetie.hcspblog.mapper;

import com.github.freetie.hcspblog.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into `user`(`username`, `password`, `created_at`, `updated_at`) values (#{username}, #{password}, now(), now())")
    void createUser(@Param("username") String username, @Param("password") String password);

    @Select("select * from `user` where username = #{username}")
    User findByUsername(@Param("username") String username);
}