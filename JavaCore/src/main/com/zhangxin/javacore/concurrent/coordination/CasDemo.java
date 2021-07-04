package com.zhangxin.javacore.concurrent.coordination;

public class CasDemo {

	private static volatile int t = 1;
	private static volatile int i = 0;

	public static void main(String[] args) {
		new Thread(() -> {
			while (i < 10) {
				while (t != 1) {
				}
				System.out.println("t1: " + (++i));
				t = 2;
			}
		}).start();

		new Thread(() -> {
			while (i < 10) {
				while (t != 2) {
				}
				System.out.println(" t2: " + (++i));
				t = 1;
			}
		}).start();
	}

}
