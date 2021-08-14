package com.zhangxin.framework.mybatis;

import com.zhangxin.framework.mybatis.mapper.UserMapper;
import com.zhangxin.framework.mybatis.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class MapperDemo {

  /*
   * 1.加载驱动
   * 2.获取连接对象 Connection
   * 3.构建SQL
   * 4.Statement对象执行SQL语句
   * 5.返回/结果集处理
   * 6.关闭资源
   */

  @Test public void dynamicMapperDemo() throws Exception {
    InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactory build = new SqlSessionFactoryBuilder().build(in);
    SqlSession sqlSession = build.openSession();

    // get dynamic proxy of UserMapper interface from sqlSession using java.lang.reflect.Proxy
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

    // The following would work requiring that:
    //  1. namespace defined in resources/mapper/UserMapper.xml must point to the interface UserMapper
    //  2. interface methods declared in UserMapper must match CRUD operation defined in resources/mapper/UserMapper.xml
    List<User> users = userMapper.selectUserList();

    for (User user : users) {
      System.out.println(user);
    }
  }

}
