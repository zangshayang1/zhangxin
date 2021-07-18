package com.zhangxin.javacore.concurrent.distributed;

import java.util.concurrent.CountDownLatch;

/**
 * It demo orderId generation service in high-concurrency scenario.
 *
 * Check a few things before running the below:
 */
public class HighConcurrencyScenario {

  private static final int CONCURRENCY = 60;

  private static final CountDownLatch CDL = new CountDownLatch(CONCURRENCY);

  public static void main(String[] args) {

    for (int i = 0; i < CONCURRENCY; i++) {
      new Thread(new Runnable() {
        @Override public void run() {
          // use concurrent threads to mimic distributed system hosting OrderService
          OrderService orderService = new OrderServiceImpl();

          CDL.countDown();
          // make certain number of threads fire at the same time to mimic concurrent scenario
          try {
            CDL.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          String orderId = orderService.createOrder();
          System.out.println(Thread.currentThread().getName() + " ========> " + orderId);
        }
      }).start();
    }
  }
}
