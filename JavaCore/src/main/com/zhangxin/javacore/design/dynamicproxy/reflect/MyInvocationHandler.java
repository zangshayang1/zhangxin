package com.zhangxin.javacore.design.dynamicproxy.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

  private Object target;

  MyInvocationHandler(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    before();
    Object res = method.invoke(target, args);
    after();

    return res;
  }

  private void after() {
    System.out.println("Do something after invocation... done.");
  }

  private void before() {
    System.out.println("Do something before invocation... done.");
  }

}
