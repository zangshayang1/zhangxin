package com.zhangxin.framework.mybatis.mapper;

import com.zhangxin.framework.mybatis.pojo.User;

import java.util.List;

public interface UserMapper {

    // method declarations must match CRUD operation defined in resources/mapper/UserMapper.xml
    public List<User> selectUserList();
}
