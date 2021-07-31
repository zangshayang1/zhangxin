package com.zhangxin.javacore.design.dynamicproxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class SuperclassProxyInterceptor implements MethodInterceptor {

  @Override public Object intercept(Object proxy, Method method, Object[] args,
      MethodProxy methodProxy) throws Throwable {

    before();
    Object res = methodProxy.invokeSuper(proxy, args);
    after();

    return res;
  }

  private void before() {
    System.out.println("Do something before... done.");
  }

  private void after() {
    System.out.println("Do something after... done.");
  }
}
