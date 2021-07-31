package com.zhangxin.javacore.design.dynamicproxy.reflect;

public class ExecutorImplB implements Executor {

  @Override public void execute() {
    System.out.println("Execute task B... done.");
  }
}