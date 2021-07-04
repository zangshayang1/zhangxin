package com.zhangxin.javacore.concurrent.thread.interrupt;

public class InterruptStateDemo {

  private static volatile boolean flag = true;

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(() -> {

      while (flag);

      System.out.println("From internal - is interrupted(): " + Thread.interrupted());

      while (!flag);

      System.out.println("Note that the attempt to interrupt the thread hasn't been successful so far.");

      try {
        System.out.println("Thread state before calling sleep(): " + Thread.currentThread().getState());
        Thread.sleep(5000L);
      } catch (InterruptedException e) {
        System.out.println("Another attempt to interrupt the thread in WAITING state succeeded.");
      }

      System.out.println("t1 done. ");
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

    Thread.sleep(100L);
    System.out.println("Thread state after calling sleep(): " + t1.getState());
    t1.interrupt();
  }
}
