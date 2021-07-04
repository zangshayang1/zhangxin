package com.zhangxin.javacore.concurrent.coordination;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {

  public static void main(String[] args) throws InterruptedException {
    startTogether();
    //    finishTogether();
  }

  private static void startTogether() {

    int concurrency = 100;

    final CountDownLatch countDownLatch = new CountDownLatch(concurrency);

    final Random rd = new Random();

    for (int i = 0; i < concurrency; i++) {
      new Thread(new Runnable() {

        @Override public void run() {

          // prepare itself
          try {
            Thread.sleep(rd.nextInt(10000));
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }

          System.out.println(Thread.currentThread().getName() + " Prepare...done.");

          // signal to everyone else that I am ready
          countDownLatch.countDown();

          // wait for everyone else to start together
          try {
            countDownLatch.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }

          System.out.println(Thread.currentThread().getName() + " started concurrently...");
          System.out.println(Thread.currentThread().getName() + " done.");
        }
      }).start();
    }
  }

  private static class Job implements Runnable {

    private int taskNo;
    private int[] arr;
    private int startIndex, endIndex;
    private CountDownLatch cdl;

    Job(int taskNo, int[] arr, int startIndex, int endIndex, CountDownLatch cdl) {
      super();
      this.taskNo = taskNo;
      this.arr = arr;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
      this.cdl = cdl;
    }

    @Override public void run() {
      Random rd = new Random();
      for (int i = startIndex; i <= endIndex; i++) {
        arr[i] = rd.nextInt();
      }
      System.out.println("task-" + taskNo + " done.");
      cdl.countDown();
    }
  }

  private static void finishTogether() throws InterruptedException {

    int targetSize = 10_000_000;
    final int[] targetArray = new int[targetSize];

    int segmentSize = 100_000;

    int jobNum = targetSize / segmentSize;

    ExecutorService threadPool = Executors.newFixedThreadPool(6);

    CountDownLatch cdl = new CountDownLatch(jobNum);

    for (int i = 0; i < jobNum; i++) {
      threadPool.execute(new Job(i, targetArray, i * segmentSize, (i + 1) * segmentSize - 1, cdl));
    }

    System.out.println("Tasks assigned to multiple threads.");

    long s1 = System.currentTimeMillis();

    cdl.await();
    // once CountDownLatch is down to 0, this object can not be reset or re-used.

    System.out.println("All tasks done. Time took: " + (System.currentTimeMillis() - s1));

    threadPool.shutdown();

    System.out.println("------------------------");

    s1 = System.currentTimeMillis();

    Random rd = new Random();
    for (int i = 0; i < targetSize; i++) {
      targetArray[i] = rd.nextInt();
    }
    System.out.println("Without concurrency, it took: " + (System.currentTimeMillis() - s1));
  }

}
