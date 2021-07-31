package com.zhangxin.javacore.design.dynamicproxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class InterfaceProxyInterceptor implements MethodInterceptor {

  private Object target;

  InterfaceProxyInterceptor(Object target) {
    this.target = target;
  }

  @Override public Object intercept(Object proxy, Method method, Object[] args,
      MethodProxy methodProxy) throws Throwable {

    before();
    Object res = method.invoke(target, args);
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
