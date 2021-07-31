package com.zhangxin.javacore.design.dynamicproxy.reflect;

public class ExecutorImplA implements Executor {

  @Override public void execute() {
    System.out.println("Execute task A... done.");
  }
}
