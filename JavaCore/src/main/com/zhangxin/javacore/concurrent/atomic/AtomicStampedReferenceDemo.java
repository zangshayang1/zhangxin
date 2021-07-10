package com.zhangxin.javacore.concurrent.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * [ABA Problem](https://en.wikipedia.org/wiki/ABA_problem)
 *
 * This class demo'ed that:
 * 1. AtomicInteger fails the ABA problem
 * 2. AtomicStampedReference/AtomicMarkableReference doesn't fail.
 */
public class AtomicStampedReferenceDemo {

  private static AtomicInteger atomicInt = new AtomicInteger(100);
  private static AtomicStampedReference<Integer> atomicStampedRef = new AtomicStampedReference<>(100, 0);

  public static void main(String[] args) throws InterruptedException {

    Thread t1 = new Thread(new Runnable() {
      @Override public void run() {
        atomicInt.compareAndSet(100, 101);
        atomicInt.compareAndSet(101, 100);
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override public void run() {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
        boolean r = atomicInt.compareAndSet(100, 101);
        System.out.println("Execution succeeded? " + r); // true
      }
    });

    t1.start();
    t2.start();

    Thread refT1 = new Thread(new Runnable() {
      @Override public void run() {
        try {
          TimeUnit.SECONDS.sleep(1); // wait for refT2 to get old stamp first
        } catch (InterruptedException ignored) {
        }
        // update and incremet stamp
        atomicStampedRef.compareAndSet(100, 101,
            atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
        atomicStampedRef.compareAndSet(101, 100,
            atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
      }
    });

    Thread refT2 = new Thread(new Runnable() {
      @Override public void run() {
        int stamp = atomicStampedRef.getStamp(); // get old stamp
        try {
          TimeUnit.SECONDS.sleep(2); // then wait for refT1 to be done
        } catch (InterruptedException ignored) {
        }
        boolean r = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
        System.out.println("Execution succeeded? " + r); // false
      }
    });

    refT1.start();
    refT2.start();
  }

}
