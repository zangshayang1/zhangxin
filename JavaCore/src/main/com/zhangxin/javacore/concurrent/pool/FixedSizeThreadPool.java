package com.zhangxin.javacore.concurrent.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedSizeThreadPool {

  private BlockingQueue<Runnable> taskQueue;

  private List<Worker> workers;

  private volatile boolean workingFlag = true; // violatile -> no dirty read

  public FixedSizeThreadPool(int workerPoolSize, int taskQueueSize) {
    if (workerPoolSize <= 0 || taskQueueSize <= 0) {
      throw new IllegalArgumentException("Illegal Argument.");
    }

    this.taskQueue = new LinkedBlockingQueue<>(taskQueueSize);
    this.workers = Collections.synchronizedList(new ArrayList<>(workerPoolSize));

    for (int i = 0; i < workerPoolSize; i++) {
      this.workers.add(new Worker());
    }
    // change thread state: NEW -> RUNNABLE, ready to run task.
    this.workers.forEach(Worker::start);
  }

  public void shutdown() {
    this.workingFlag = false;

    for (Thread w : this.workers) {
      if (w.getState().equals(Thread.State.BLOCKED) || w.getState().equals(Thread.State.WAITING)
          || w.getState().equals(Thread.State.TIMED_WAITING)) {
        // What if some blocking threads are in the middle of executing some task?
        // Does this function shutdown the whole thing before completing all the tasks?
        // Note that take() throws InterruptedException when interrupted. Actually all blocking methods do.
        // Common practice is to have it in a try-catch block
        // so that when it is interrupted, you can catch the exception and check if all the tasks are completed.
        w.interrupt();
      }
    }
  }

  public boolean submit(Runnable task) {
    if (this.workingFlag) {
      return this.taskQueue.offer(task); // offer returns false when the queue is full
    }
    return false;
  }

  private class Worker extends Thread {

    public Worker() {
    }

    public void run() {
      int taskCount = 0;

      // take() task out of the queue when workingFlag is true
      // otherwise, only poll() from queue when there are tasks remaining in the queue
      while (workingFlag || taskQueue.size() > 0) {
        Runnable task = null;
        try {
          if (workingFlag) {
            task = taskQueue.take(); // take() is blocking/waiting when the queue is empty
          }
          // WHY using poll() instead of take() to get remaining tasks from the queue?
          // because more workers than remaining tasks can come to this block
          // in which case, some worker will wait forever and block the process from terminating
          // BUT what if shutdown() is invoked after a worker starts waiting on an empty queue?
          // that's why we need to interrupt() each worker in the above shutdown().
          else {
            task = taskQueue.poll(); // poll() returns null when the queue is empty
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        if (task != null) {
          try {
            task.run();
            System.out.println(
                Thread.currentThread().getName() + " done with" + (++taskCount) + " tasks.");
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      System.out.println(Thread.currentThread().getName() + " end.");
    }
  }

  public static void main(String[] args) {
    FixedSizeThreadPool pool = new FixedSizeThreadPool(3, 6);

    for (int i = 0; i < 6; i++) {
      pool.submit(() -> {   // Runnable
        System.out.println("Task execution starts...");
        try {
          Thread.sleep(200L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }

    try {
      Thread.sleep(2000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    pool.shutdown();
  }
}
