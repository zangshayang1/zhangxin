package com.zhangxin.framework.mybatis;

import com.zhangxin.framework.mybatis.mapper.UserMapper;
import com.zhangxin.framework.mybatis.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration(locations = {"classpath:spring-config.xml"})
@RunWith(value = SpringJUnit4ClassRunner.class)
public class SpringIntegrationDemo {

    @Autowired
    private UserMapper mapper;

    /**
     * MyBatis 整合 Spring
     *   我们需要将 MyBatis中的核心对象交给Spring 容器管理
     *   SqlSessionFactory  SqlSession
     */
    @Test
    public void testQuery(){
        List<User> users = mapper.selectUserList();
        for (User user : users) {
            System.out.println(user);
        }
    }
}
