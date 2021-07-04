package com.zhangxin.javacore.concurrent.thread.state;

public class ThreadStateDemo4_Wait {

  public static void main(String[] args) throws Throwable {
    Object obj = new Object();
    Thread t1 = new Thread(() -> {
      synchronized (obj) {
        try {

          System.out.println("t1 starts to wait(3000L)");
          // wait() release the lock on this object
          obj.wait(3000L);

          System.out.println("t1 starts to wait()");
          // release the lock on this object again
          obj.wait();

          System.out.println("t1 done.");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    // t1 start hit obj.wait() for 3s, released lock on obj
    t1.start();

    // this thread sleep for 1s
    Thread.sleep(1000L);

    // this thread acquires the lock on obj and enter the following block
    synchronized (obj) {

      // now t1 state -> // TIMED_WAITING
      System.out.println("t1 state: " + t1.getState());

      // notify() wakes up one of the threads that is waiting on this object
      // but it doesn't release the lock this thread is currently holding
      obj.notify();

      // this thread sleep for another 1s
      Thread.sleep(1000L);

      // t1 was awaken but blocked on object's lock
      System.out.println("t1 state: " + t1.getState()); // BLOCKED
    }
    // this thread released the lock on obj and start to sleep for 3s.
    Thread.sleep(3000L);

    // t1 goes to the second wait()
    System.out.println("t1 state: " + t1.getState());

    Thread.sleep(1000L);
    // this thread acquired the lock and wake up t1 from indefinite waiting
    synchronized (obj) {
      obj.notify();
    }
    // this thread released lock and t1 acquired the lock
    // this thread went to sleep and t1 is done now.
    Thread.sleep(1000L);
    System.out.println("t1 state: " + t1.getState());
  }
}
