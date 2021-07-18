package com.zhangxin.javacore.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * On exclusive entity:
 * 1. UNSAFE can not be directly used at application level to implement CAS
 * 2. AtomicIntegerFieldUpdater can be used to implement CAS
 * 3. AtomicInteger is the simplest way to implement CAS
 */
public class MyReentrantLockImpl implements Lock {

  private static class Node {
    Thread thread;
    volatile Node pre;
    volatile Node next;

    Node(Thread thread) {
      this.thread = thread;
    }
  }

  // exclusive entity
  private volatile AtomicInteger state = new AtomicInteger();

  private Thread owner;

  private volatile Node head, tail;

  private boolean fair;

  private static final AtomicReferenceFieldUpdater<MyReentrantLockImpl, Node> HEAD_UPDATER =
      AtomicReferenceFieldUpdater.newUpdater(MyReentrantLockImpl.class, Node.class, "head");

  private static final AtomicReferenceFieldUpdater<MyReentrantLockImpl, Node> TAIL_UPDATER =
      AtomicReferenceFieldUpdater.newUpdater(MyReentrantLockImpl.class, Node.class, "tail");

  // unfair lock
  public MyReentrantLockImpl() {}

  // fair lock MyReentrantLockImpl(true)
  public MyReentrantLockImpl(boolean fair) {
    this.fair = fair;
  }

  @Override
  public void lock() {
    // implement reentrant property
    if (Thread.currentThread() == owner) {
      state.getAndIncrement();
      return ;
    }

    if (fair) {
      Node node = addWaiter();
      doPark(node);
    } else {
      if (!tryLock()) {
        Node node = addWaiter();
        doPark(node);
      }
    }
  }

  private void doPark(Node node) {
    while (true) {
      // if the node is the first one in the waiting linked list
      // tryLock() again before LockSupport.park()
      // if tryLock() fails, it means whichever thread successfully got the lock still holds it
      //  when that thread unlock() itself, it will unpark() the next one (current node) in the list
      // Otherwise, current thread should succeed in tryLock()
      // Without this logic being placed correctly, current thread might park() forever
      // when no other thread unpark() it
      if (node.pre == head) {
        if (tryLock()) {
          // only when the unparked thread from the waiting linked list successfully got the lock
          // the corresponding node will be removed from the list
          node.thread = null;
          node.pre = null;
          head = node;
          return ;
        }
      }

      // while tryLock() fails, blocking current thread on this MyReentrantLockImpl obj
      LockSupport.park(this);
      // reset interrupted state back to false in case this thread is interrupted from external
      // LockSupport.park() doesn't work when this thread's interrupted state is true
      Thread.interrupted();
    }
  }

  // append current thread
  private Node addWaiter() {
    Node node = new Node(Thread.currentThread());
    for (;;) {
      Node t = tail;
      if (t == null) {
        // purely init a dummy head
        // add waiting thread in next loop else block
        if (HEAD_UPDATER.compareAndSet(this, null, new Node(null))) {
          tail = head;
        }
      } else {
        // both "node.pre = t" and "t.next = node" are not CAS'ed here, which doesn't cause thread safety concern
        // because when t is not pointing at the same thing as tail, the "TAIL_UPDATER.compareAndSet()" will fail
        // and it will loop back to reset node.pre and t.next
        node.pre = t;
        // TAIL_UPDATER is always tracking "tail", which could be modified by other threads in concurrent scenario
        if (TAIL_UPDATER.compareAndSet(this, t, node)) {
          t.next = node;
          return node;
        }
      }
    }
  }

  @Override
  public boolean tryLock() {
    if (Thread.currentThread() == owner) {
      return true;
    }

    boolean locked = state.compareAndSet(0, 1);
    if (locked) {
      owner = Thread.currentThread();
    }
    return locked;
  }

  @Override
  public void unlock() {
    // only 1 thread is eligible to call unlock
    if (Thread.currentThread() != owner) {
      throw new RuntimeException("Only the lock owner can invoke unlock.");
    }

    // clear owner first and then set state to 0
    int s = state.get() - 1;
    if (s == 0) {
      owner = null;
    }
    state.set(s);

    // variable h is a local variable specific to the current thread,
    // the referenced object doesn't change once assigned even when head can be modified concurrently
    // this is the thread-safe way to interact with global variable.
    Node h = head;
    if (h != null) {
      Node n = h.next;
      if (n != null) {
        // unblock the first thread waiting in the linked list
        LockSupport.unpark(n.thread);
      }
    }

    /* The below way of writing the seemingly same logic is thread unsafe as head/tail is shared variable

     if (head != null && head.next != null) {
      LockSupport.unpark(head.next.thread);
     }

     */
  }

  @Override public void lockInterruptibly() throws InterruptedException {
    // implementation omitted ...
  }

  @Override public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    // implementation omitted ...
    return false;
  }

  @Override public Condition newCondition() {
    // implementation omitted ...
    return null;
  }
}
