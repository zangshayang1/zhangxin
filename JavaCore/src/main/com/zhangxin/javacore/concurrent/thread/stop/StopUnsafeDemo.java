package com.zhangxin.javacore.concurrent.thread.stop;

public class StopUnsafeDemo {

  /**
   * What does the startTogether do?
   *
   * Start a thread to read the static object "user" 300 times, printing mismatched id1 and id2 if any
   *
   * Start 10 threads to modify the static object "user", stop
   *   Each thread attempts to enter the synchronized block 3 times
   *   Each attempt sets the id1 and id2 to the same value with 200ms sleep in between
   *   Each attempt will be stopped while sleeping because the outer loop calls t.stop() only after 100ms.
   *
   */
  public static void main(String[] args) throws InterruptedException {

    new ReadObjectThread().start();

    int numOfThreads = 10;
    for (int i = 0; i < numOfThreads; i++) {
      Thread t = new changeObjectThread();
      t.start();
      Thread.sleep(100);
      t.stop(); // released all the locks held by t and left user's data unsafe
    }
  }

  static User user = new User();

  static class User {

    private int id1;
    private int id2;

    User() {
      id1 = 0;
      id2 = 0;
    }

    int getId1() {
      return id1;
    }

    void setId1(int id1) {
      this.id1 = id1;
    }

    int getId2() {
      return id2;
    }

    void setId2(int id2) {
      this.id2 = id2;
    }

    public String toString() {
      return "User [id1=" + id1 + ",id2=" + id2 + "]";
    }
  }

  static class changeObjectThread extends Thread {

    public void run() {
      int attempt = 3;
      while (attempt > 0) {
        attempt--;
        synchronized (user) {

          int v = (int) (System.currentTimeMillis() / 1000);

          user.setId1(v);

          // it's supposed to hold the lock while it's sleeping
          // but it's stopped and released all the locks
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          user.setId2(v);
        }
        Thread.yield();
      }
    }
  }

  static class ReadObjectThread extends Thread {

    public void run() {
      int attempt = 300;
      while (attempt > 0) {
        attempt--;
        synchronized (user) {
          if (user.getId1() != user.getId2()) {
            System.out.println(user.toString());
          }
        }
        Thread.yield();
      }
    }
  }
}
