package com.zhangxin.javacore.concurrent.coordination;

public class WaitNotifyDemo {

	private static int i = 0;
	private final static Object OBJECT = new Object();

	private static int limit = 20;

	public static void main(String[] args) throws Throwable {

		new Thread(() -> {
			synchronized (OBJECT) {
				while (i < limit) {
					if (i % 3 == 0) {
						System.out.println("t1: " + (++i));
					}
					OBJECT.notifyAll();
					try {
						OBJECT.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				OBJECT.notifyAll();
			}
		}).start();

		new Thread(() -> {
			synchronized (OBJECT) {
				while (i < limit) {
					if (i % 3 == 1) {
						System.out.println(" t2: " + (++i));
					}
					OBJECT.notifyAll();
					try {
						OBJECT.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				OBJECT.notifyAll();
			}
		}).start();

		new Thread(() -> {
			synchronized (OBJECT) {
				while (i < limit) {
					if (i % 3 == 2) {
						System.out.println("  t3: " + (++i));
					}
					OBJECT.notifyAll();
					try {
						OBJECT.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				OBJECT.notifyAll();
			}
		}).start();
	}
}
