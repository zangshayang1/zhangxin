package com.zhangxin.javacore.concurrent.thread.stop;

public class FlagControl {

  public volatile static boolean flag = true; // the use of volatile is the key here

  public static void main(String[] args) throws InterruptedException {
    new Thread(() -> {
      try {
        while (flag) {
          System.out.println("haha");
          Thread.sleep(1000L);
        }
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }).start();

    Thread.sleep(3000L);
    flag = false;
    System.out.println("xixi");
  }
}
