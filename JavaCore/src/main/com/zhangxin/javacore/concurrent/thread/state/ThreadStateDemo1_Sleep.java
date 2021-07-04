package com.zhangxin.javacore.concurrent.thread.state;

public class ThreadStateDemo1_Sleep {

  static volatile boolean flag = true;

  public static void main(String[] args) throws Throwable {

    Thread t1 = new Thread(() -> {
      try {
        while (flag) {
        }
        Thread.sleep(10000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    System.out.println("flag: " + flag);
    System.out.println("t1 state before start(): " + t1.getState());

    t1.start();

    Thread.sleep(2000L);

    System.out.println("t1 state after start(): " + t1.getState());

    System.out.println("flag: " + flag);
    flag = false;

    Thread.sleep(2000L);
    System.out.println("t1 state while sleeping: " + t1.getState());

  }
}
