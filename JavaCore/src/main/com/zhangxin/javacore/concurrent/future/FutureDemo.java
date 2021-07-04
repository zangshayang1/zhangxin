package com.zhangxin.javacore.concurrent.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureDemo {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    FutureDemo demo = new FutureDemo();

    demo.getFromFuture();
    System.out.println("---------------------");
    demo.cancelSleepingThreadWithoutInterruption();
    System.out.println("---------------------");
    demo.cancelWaitingThreadWithoutInterruption();
    System.out.println("---------------------");
    demo.cancelSleepingThreadWithInterruption();
    System.out.println("---------------------");
    demo.cancelWaitingThreadWithInterruption();
    System.out.println("---------------------");
    demo.cancelOrInterruptInfiniteLoopFailed();
    System.out.println("---------------------");
    System.out.println("Main exits.");

  }

  private void getFromFuture() throws InterruptedException {
    // Define anonymous callable lambda in FutureTask
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      System.out.println("GetFromFuture() - Callable execution - before sleep...");
      Thread.sleep(2000L);
      System.out.println("GetFromFuture() - Callable execution - after sleep...");
      return "ok";
    });

    new Thread(futureTask).start();

    try {
      System.out.println("GetFromFuture() - futureTask.get() invocation at " + System.currentTimeMillis());
      String res = futureTask.get(); // get() is blocking until the future returns
      System.out.println("GetFromFuture() - res = " + res);

    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    System.out.println("GetFromFuture() - futureTask.get() succeeded at " + System.currentTimeMillis());
    Thread.sleep(2000L); // Let the above future task finish before this function call finish itself
  }

  private void cancelSleepingThreadWithoutInterruption()
      throws InterruptedException, ExecutionException {
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      System.out.println("cancelSleepingThreadWithoutInterruption() - Callable execution - before sleep...");
      Thread.sleep(2000L);
      System.out.println("cancelSleepingThreadWithoutInterruption() - Callable execution - after sleep...");
      return "ok";
    });
    Thread t = new Thread(futureTask);
    t.start();

    Thread.sleep(100L);
    boolean isCancelled = futureTask.cancel(false);
    System.out.println("FutureTask cancelled: " + isCancelled);
    System.out.println("Thread state right after cancel: " + t.getState());
    Thread.sleep(2000L); // Let the above future task finish before this function call finish itself
    System.out.println("Thread final state: " + t.getState());
  }

  private void cancelWaitingThreadWithoutInterruption()
      throws InterruptedException, ExecutionException {
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      System.out.println("cancelWaitingThreadWithoutInterruption() - Callable execution - before sleep...");
      wait(2000L);
      System.out.println("cancelWaitingThreadWithoutInterruption() - Callable execution - after sleep...");
      return "ok";
    });
    Thread t = new Thread(futureTask);
    t.start();

    Thread.sleep(100L);
    boolean isCancelled = futureTask.cancel(false);
    System.out.println("Main - futureTask cancelled: " + isCancelled);
    System.out.println("Thread state right after cancel: " + t.getState());
    Thread.sleep(2000L); // Let the above future task finish before this function call finish itself
    System.out.println("Thread final state: " + t.getState());
  }

  private void cancelSleepingThreadWithInterruption()
      throws InterruptedException, ExecutionException {
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      System.out.println("cancelSleepingThreadWithInterruption() - Callable execution - before sleep...");
      Thread.sleep(2000L);
      System.out.println("cancelSleepingThreadWithInterruption() - Callable execution - after sleep...");
      return "ok";
    });
    Thread t = new Thread(futureTask);
    t.start();

    Thread.sleep(100L);
    boolean isCancelled = futureTask.cancel(true);
    System.out.println("Main - futureTask cancelled: " + isCancelled);
    System.out.println("Thread state right after cancel: " + t.getState());
    Thread.sleep(2000L); // Let the above future task finish before this function call finish itself
    System.out.println("Thread final state: " + t.getState());
  }

  private void cancelWaitingThreadWithInterruption() throws InterruptedException,
      ExecutionException {
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      System.out.println("cancelWaitingThreadWithInterruption() - Callable execution - before sleep...");
      wait(2000L);
      System.out.println("cancelWaitingThreadWithInterruption() - Callable execution - after sleep...");
      return "ok";
    });

    Thread t = new Thread(futureTask);
    t.start();

    Thread.sleep(100L);
    boolean isCancelled = futureTask.cancel(true);
    System.out.println("Main - futureTask cancelled: " + isCancelled);
    System.out.println("Thread state right after cancel: " + t.getState());
    Thread.sleep(2000L); // Let the above future task finish before this function call finish itself
    System.out.println("Thread final state: " + t.getState());
  }

  private void cancelOrInterruptInfiniteLoopFailed() throws InterruptedException, ExecutionException {
    FutureTask<String> futureTask = new FutureTask<>(() -> {
      while (true) {
        if (Thread.currentThread().getState() == Thread.State.TERMINATED) return null;
      }
    });
    Thread t = new Thread(futureTask);
    t.start();

    Thread.sleep(100L);
    boolean isCancelled = futureTask.cancel(true);
    System.out.println("Main - futureTask cancelled: " + isCancelled);
    System.out.println("Thread state: " + t.getState());
    System.out.println("Let it run for 5s before interrupting the thread...");
    Thread.sleep(5000L);
    t.interrupt();
    System.out.println("Wait for another 2s before checking thread state...");
    Thread.sleep(2000L);
    System.out.println("Thread state: " + t.getState());
  }
}
