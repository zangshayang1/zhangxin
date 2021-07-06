package com.zhangxin.javacore.concurrent.keyword;

import java.util.concurrent.TimeUnit;

public class VisibilityDemo {

  // case 1 - regular
  private static boolean running = true;
  // case 2 - synchronized
  private static boolean synchronizedRunning = true;
  // case 3 - volatile
  private static volatile boolean volatileRunning = true;

  public static void main(String[] args) {

    // case 1 - regular
    Thread t1 = new Thread(new Runnable() {

      @Override public void run() {
        int i = 0;
        while (VisibilityDemo.running) {
          i++;
        }
        System.out.println("Yes, t1 terminated with i = " + i);
      }
    });

    // case 2 - synchronized
    Thread t2 = new Thread(new Runnable() {
      @Override public void run() {
        int i = 0;
        while (VisibilityDemo.synchronizedRunning) {
          synchronized (this) {
            i++;
          }
        }
        System.out.println("Yes, t2 terminated with i = " + i);
      }
    });

    // case 3 - volatile
    Thread t3 = new Thread(new Runnable() {
      @Override public void run() {
        int i = 0;
        while (VisibilityDemo.volatileRunning) {
          i++;
        }
        System.out.println("Yes, t3 terminated with i = " + i);
      }
    });

    // case 4 - synchronized outside of while loop
    Thread t4 = new Thread(new Runnable() {
      @Override public void run() {
        int i = 0;
        // whenever this thread goes into synchronized block
        // it sync "synchronizedRunning" from main memory
        // that's why case 2 works but case 4 doesn't
        synchronized (this) {
          while (VisibilityDemo.synchronizedRunning) {
            i++;
          }
        }
        System.out.println("Yes, t2 terminated with i = " + i);
      }
    });

    t1.start();
    t2.start();
    t3.start();
    t4.start();

    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    VisibilityDemo.running = false;
    VisibilityDemo.synchronizedRunning = false;
    VisibilityDemo.volatileRunning = false;

    System.out.println("Changed flag to 'false'. Does all of them terminate? ");
  }
}
