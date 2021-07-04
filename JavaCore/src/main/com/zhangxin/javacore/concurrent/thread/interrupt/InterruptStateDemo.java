package com.zhangxin.javacore.concurrent.thread.interrupt;

public class InterruptStateDemo {

  static volatile boolean flag = true;

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(()-> {
      while(flag) { }

      System.out.println("From internal - is interrupted(): " + Thread.interrupted());

      while(!flag) { }

    });

    t1.start();

    Thread.sleep(100L);
    // false - t1 is not interrupted
    System.out.println("Before interruption - is interrupted(): " + t1.isInterrupted());
    t1.interrupt();
    // true - t1 is interrupted
    System.out.println("After interruption - is interrupted(): " + t1.isInterrupted());

    System.out.println("Set flag = " + false);
    flag = false;

    System.out.println("Sleeping for 100ms...");
    Thread.sleep(100L);
    // After sleep, t1.isInterrupted() returns false WHY?
    // Because calling Thread.interrupted() from t1 internal resets the flag to false
    System.out.println("After sleep - is interrupted(): " + t1.isInterrupted());

    flag = true;
    System.out.println("t1 done. ");
  }
}
