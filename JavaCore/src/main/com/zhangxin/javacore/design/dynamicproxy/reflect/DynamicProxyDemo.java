package com.zhangxin.javacore.design.dynamicproxy.reflect;

public class DynamicProxyDemo {

  public static void main(String[] args) {
    Object executorA = new ExecutorImplA();
    Object executorB = new ExecutorImplB();
    Executor proxy = (Executor) DynamicProxyGenerator.getProxyFor(executorA);
    proxy.execute();
    proxy = (Executor) DynamicProxyGenerator.getProxyFor(executorB);
    proxy.execute();
  }
}
