package com.zhangxin.javacore.concurrent.coordination;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

  private static final Random RANDOM = new Random();

  // maximum resource count: 10
  // fair? true -> first come first serve
  private static final Semaphore SEMAPHORE = new Semaphore(10, true);

  // 20 threads compete for 10 resources
  private static final int THREAD_NUM = 20;

  public static void main(String[] args) {

    for (int i = 0; i < THREAD_NUM; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(RANDOM.nextInt(5000));
            useCar();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

  private static void useCar() throws InterruptedException {
    System.out.println(Thread.currentThread().getName() + " want to use a shared car.");
    long s = System.currentTimeMillis();
    SEMAPHORE.acquire();
    System.out.println(Thread.currentThread().getName() + " got a shared car within (ms)" + (System.currentTimeMillis() - s));

    try {
      Thread.sleep(RANDOM.nextInt(10000));
    } catch (InterruptedException ignored) {
    } finally {
      System.out.println(Thread.currentThread().getName() + " returned a shared car.");
      SEMAPHORE.release();
    }
  }

}
