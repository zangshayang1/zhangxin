package com.zhangxin.javacore.concurrent.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private static volatile int count = 0;

    private static MyReentrantLockImpl lock = new MyReentrantLockImpl();

    private static void increase() {
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        int threads = 20;
        //倒计数锁存器   并发协同用
        CountDownLatch cdl = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        ReentrantLockDemo.increase();
                    }
                    cdl.countDown();
                }
            }).start();
        }

        //等待执行增的线程执行完增
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(ReentrantLockDemo.count);
    }
}

