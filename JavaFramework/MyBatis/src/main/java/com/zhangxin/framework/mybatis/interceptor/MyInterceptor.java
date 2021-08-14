package com.zhangxin.framework.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;
import java.util.Properties;

/**
 * 自定义的MyBatis的拦截器 插件
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 * ParameterHandler (getParameterObject, setParameters)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * StatementHandler (prepare, parameterize, batch, update, query)
 */
@Intercepts({
  @Signature(
          type = Executor.class
          ,method = "query"
          ,args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}
  ),@Signature(
          type = ResultSetHandler.class
          ,method = "handleResultSets"
          ,args = {Statement.class}
)
})
public class MyInterceptor implements Interceptor {

  private String testProp;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    System.out.println("Before invocation ...");
    Object obj = invocation.proceed();
    System.out.println("After invocation ...");
    return obj;
  }

  @Override
  public Object plugin(Object target) {
      return Plugin.wrap(target,this);
  }

  @Override
  public void setProperties(Properties properties) {
    this.testProp = (String) properties.get("testProp");
    System.out.println(this.testProp);
  }
}
