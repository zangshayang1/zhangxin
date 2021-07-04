package com.zhangxin.javacore.concurrent.coordination;

public class CasDemo {

  private static volatile int flag = 1;
  private static volatile int i = 0;
  private static int limit = 20;

  public static void main(String[] args) {
    new Thread(() -> {
      while (i < limit) {
        while (flag != 1);
        System.out.println("t1: " + (++i));
        flag = 2;
      }
    }).start();

    new Thread(() -> {
      while (i < limit) {
        while (flag != 2);
        System.out.println("t2: " + (++i));
        flag = 3;
      }
    }).start();

    new Thread(() -> {
      while (i < limit) {
        while (flag != 3);
        System.out.println("t3: " + (++i));
        flag = 1;
      }
    }).start();
  }

}
