package com.zhangxin.javacore.concurrent.coordination;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

	private static int i = 0;
	private static Thread t1, t2;

	public static void main(String[] args) throws Throwable {

		t1 = new Thread(() -> {
			while (i < 10) {
				System.out.println("t1: " + (++i));
				// park() and unpark() differs from wait()/notify() as in
        // it can specify which thread it takes effect on
				LockSupport.unpark(t2);
				LockSupport.park();
			}
		});

		t2 = new Thread(() -> {
			while (i < 10) {
				// what if t1 runs unpark(t2) before t2 runs this line? deadlock?
        // unpark(t2) produce a semaphore when there is none; otherwise, it does nothing.
        // park(t2) consumes a semaphore when there is one; otherwise, it halts the thread.
				LockSupport.park();
				System.out.println(" t2: " + (++i));
				LockSupport.unpark(t1);
			}
		});

		t1.start();
		t2.start();
	}
}
