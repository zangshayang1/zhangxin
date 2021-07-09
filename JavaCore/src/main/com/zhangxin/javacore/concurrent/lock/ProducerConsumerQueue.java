package com.zhangxin.javacore.concurrent.lock;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * It demo the use of Lock and Condition
 */
public class ProducerConsumerQueue {

  static class Storage {

    private int cap;
    private List<Integer> lists = new LinkedList<>();

    private Lock lock = new ReentrantLock();
    private final Condition produceCondition = lock.newCondition();
    private final Condition consumeCondition = lock.newCondition();

    Storage(int cap) {
      this.cap = cap;
    }

    void produce(int item) {
      lock.lock();
      try {
        while (lists.size() == cap) {
          // block producer
          produceCondition.await();
        }
        lists.add(item);
        System.out.println(
            Thread.currentThread().getName() + " produce " + item);
        // wake up consumer
        this.consumeCondition.signal();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally { // avoid deadlock
        lock.unlock();
      }
    }

    void consume() {
      lock.lock();
      try {
        while (lists.size() == 0) {
          // block consumer
          consumeCondition.await();
        }

        Integer item = lists.remove(0);
        System.out.println(Thread.currentThread().getName() + " consume " + item);
        // wake up producer
        produceCondition.signal();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally { // avoid deadlock
        lock.unlock();
      }
    }
  }

  public static void main(String[] args) {
    Random random = new Random();
    Storage storehouse = new Storage(10);
    Runnable produceRunnable = () -> {
      while (true) {
        storehouse.produce(random.nextInt(1000));
        try {
          Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    Runnable consumeRunnable = () -> {
      while (true) {
        storehouse.consume();
        try {
          Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    new Thread(produceRunnable).start();
    new Thread(produceRunnable).start();
    new Thread(produceRunnable).start();

    new Thread(consumeRunnable).start();
    new Thread(consumeRunnable).start();
    new Thread(consumeRunnable).start();
  }
}
