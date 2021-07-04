package com.zhangxin.javacore.concurrent.thread.state;

import java.util.concurrent.locks.LockSupport;

public class ThreadStateDemo6_Park {

  public static void main(String[] args) throws Throwable {

    Thread t1 = new Thread(() -> {

      LockSupport.park();

      System.out.println("t1 state after unpack(): " + Thread.currentThread().getState());
    });

    t1.start();

    Thread.sleep(1000L);

    System.out.println("t1 state while being park(): " + t1.getState());

    LockSupport.unpark(t1);
  }
}
