package com.zhangxin.javacore.design.dynamicproxy.reflect;

import java.lang.reflect.Proxy;

class DynamicProxyGenerator {

  static Object getProxyFor(Object target) {

    return Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        new MyInvocationHandler(target));
  }
}
