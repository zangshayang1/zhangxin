package com.zhangxin.javacore.concurrent.thread.state;

public class ThreadStateDemo2_Join {

  public static void main(String[] args) throws Throwable {

    Thread t1 = new Thread(() -> {
      try {
        Thread.sleep(10000L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Thread t2 = new Thread(() -> {
      try {
        System.out.println("t2 run t1.join(5000L)...");
        t1.join(5000L);
        System.out.println("t2 run t1.join()...");
        t1.join();
        System.out.println("t2 done.");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    t1.start();
    t2.start();

    Thread.sleep(1000L);

    System.out.println("t2 state: " + t2.getState());

    Thread.sleep(5000L);
    System.out.println("t2 state: " + t2.getState());

  }
}
