package com.zhangxin.javacore.concurrent.thread.state;

public class ThreadStateDemo3_Synchronized {

	public static void main(String[] args) throws Throwable {

		Thread t1 = new Thread(() -> {
			synchronized (ThreadStateDemo3_Synchronized.class) {
				System.out.println("t1 entered synchronized block.");
				System.out.println("t1 state: " + Thread.currentThread().getState());
			}
		});

		Thread t2 = new Thread(() -> {
			synchronized (ThreadStateDemo3_Synchronized.class) {
				t1.start();
				System.out.println("t2 entered synchronized block.");
				System.out.println("t1 state: " + t1.getState());
			}
		});

		t2.start();
	}
}
