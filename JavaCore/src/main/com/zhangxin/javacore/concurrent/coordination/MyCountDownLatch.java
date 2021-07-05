package com.zhangxin.javacore.concurrent.coordination;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class MyCountDownLatch {

  private AtomicInteger count;
  private List<Thread> waiters;

  public MyCountDownLatch(int count) {
    this.count = new AtomicInteger(count);
    waiters = Collections.synchronizedList(new LinkedList<>());
  }

  public void countDown() {
    // when multiple threads are racing to update this.count
    // only one of them will succeed and get out of the loop except when count.get() <= 0
    // other threads will attempt again during the next loop
    while (true) {
      int expect = count.get();
      if (expect <= 0) {
        break;
      }
      int update = expect - 1;
      // Atomically sets the value to the given updated value if the current value the expected value.
      if (count.compareAndSet(expect, update)) {
        if (update == 0) {
          doRelease();
        }
        break;
      }
    }
  }

  private void doRelease() {
    waiters.forEach(LockSupport::unpark);
    waiters.clear(); // help gc
  }

  public void await() throws InterruptedException {
    // check before waiting
    int c = count.get();
    if (c <= 0) return ;

    waiters.add(Thread.currentThread());

    // infinite loop prevents psudo wakeup
    while (true) {
      // the only way to get out of the loop is that the following condition is met
      c = count.get();
      if (c <= 0) return ;

      LockSupport.park(this);
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
    }
  }
  
}
