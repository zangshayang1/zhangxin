package com.zhangxin.javacore.concurrent.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicFieldUpdaterDemo {

  static class DemoBean {

    volatile int num;

    DemoBean() {
      this.num = 0;
    }

    int getNum() {
      return num;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int threads = 20;
    final CountDownLatch cdl = new CountDownLatch(threads);

    final DemoBean demoBean = new DemoBean();
    final AtomicIntegerFieldUpdater<DemoBean> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater
        .newUpdater(DemoBean.class, "num");

    for (int i = 0; i < threads; i++) {
      new Thread(new Runnable() {

        @Override public void run() {
          for (int j = 0; j < 10000; j++) {
            // use the same updater for the same object
            atomicIntegerFieldUpdater.incrementAndGet(demoBean);
          }
          cdl.countDown();
        }
      }).start();
    }

    try {
      cdl.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(demoBean.getNum());
  }
}


