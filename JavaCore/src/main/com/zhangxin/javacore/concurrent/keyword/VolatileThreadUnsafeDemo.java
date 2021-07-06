package com.zhangxin.javacore.concurrent.keyword;

import java.util.concurrent.CountDownLatch;

public class VolatileThreadUnsafeDemo {

  private static volatile int count = 0;

  private static void increase() {
    count++;
  }

  public static void main(String[] args) {
    int threads = 20;
    CountDownLatch cdl = new CountDownLatch(threads);

    for (int i = 0; i < threads; i++) {

      new Thread(new Runnable() {
        @Override public void run() {
          for (int i = 0; i < 1000; i++) {
            VolatileThreadUnsafeDemo.increase();
          }
          cdl.countDown();
        }
      }).start();
    }

    // wait all 20 threads complete
    try {
      cdl.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // expected print out 20000, but the result is off
    // because volatile keyword cannot guarantee thread safe.
    System.out.println(VolatileThreadUnsafeDemo.count);
  }
}
