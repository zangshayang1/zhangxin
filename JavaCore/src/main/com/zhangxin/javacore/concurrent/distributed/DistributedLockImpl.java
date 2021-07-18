package com.zhangxin.javacore.concurrent.distributed;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * This implementation is built on top of ZK
 *
 * There are 2 versions of the implementation.
 *
 * Version 1 - with thundering herd problem.
 *
 *  It uses ZkClient.createEphemeral() to implement exclusivity of a lock.
 *
 *  tryLock() will catch "ZkNodeExistsException" and return false when the above call fails.
 *
 *  unLock() will delete the ephemeral node.
 *
 *  lock() will keep trying to acquire a lock (and keep waiting on the deletion of the ephemeral
 *  node) till it gets one. When a deletion happens, all the listeners will be notified.
 *
 * Version 2 - without thundering herd problem. (below)
 *
 *  It uses ZkClient.createEphemeralSequential() implement exclusivity of a lock.
 *
 *  tryLock() will create for each caller thread an ephemeral node in the order they come in
 *  and grant the lock to the first caller (return true).
 *
 *  unLock() will delete the current ephemeral node.
 *
 *  lock() will keep trying to acquire a lock (and keep waiting on the deletion of the previous
 *  ephemeral node) till it gets one. When a deletion happens, only the next listener will be
 *  notified.
 */
public class DistributedLockImpl implements DistributedLock {

  private String rootPath;
  private String currentNodePath;
  private String previousNodePath;
  private ZkClient client;
  private Thread owner;

  public DistributedLockImpl(String rootPath) {
    super();
    this.rootPath = rootPath;
    this.client = new ZkClient("localhost:2181");
    try {
      // create a persistent node as the parent to all subsequent ephemeral sequential nodes
      this.client.createPersistent(rootPath);
    } catch (ZkNodeExistsException ignored) {;}
  }

  @Override public boolean tryLock() {
    if (currentNodePath == null) {
      // under root path, create sequential ephemeral nodes 0000000001, 0000000002, ...
      // client.createEphemeral(path, "") will throw "ZkNodeExistsException" when "path" exists
      // client.createEphemeralSequential(path, "") will always create the next node in the sequence
      currentNodePath = client.createEphemeralSequential(rootPath + "/", "");
    }

    List<String> children = client.getChildren(rootPath);
    Collections.sort(children);

    // the lock is granted if currentNodePath is the first one is the list
    if (currentNodePath.equals(rootPath + "/" + children.get(0))) {
      owner = Thread.currentThread();
      return true;
    } else {
      // otherwise the previousNodePath before currentNodePath
      String currentNode = currentNodePath.substring(rootPath.length() + 1, currentNodePath.length());
      previousNodePath = rootPath + "/" + children.get(children.indexOf(currentNode) - 1);
      return false;
    }
  }

  @Override public void lock() {
    // blocking the caller thread till it acquires a lock
    // note that it happens on the caller host and thus doesn't take any resource on Zk cluster
    // or lock-protected OrderIdGeneratorClient
    while (!tryLock()) {
      final CountDownLatch cdl = new CountDownLatch(1);

      IZkDataListener listener = new IZkDataListener() {

        // not needed here
        @Override public void handleDataChange(String s, Object o) throws Exception { ; }

        // When the lock owner releases the lock, it will delete the ephemeral file.
        // When listener captures the deletion, it releases count down latch from waiting.
        @Override public void handleDataDeleted(String s) throws Exception { cdl.countDown(); }
      };

      // attach listener to zk client
      client.subscribeDataChanges(previousNodePath, listener);

      // check again before entering waiting stage to avoid dead blocking (waiting on something not gonna happen)
      if (client.exists(previousNodePath)) {
        try {
          cdl.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      // detach listener to zk client
      client.unsubscribeDataChanges(previousNodePath, listener);
    }
  }

  @Override public void unlock() {
    if (owner != Thread.currentThread()) {
      throw new RuntimeException("Only the thread holding a lock can be unlocked.");
    }
    client.delete(currentNodePath);
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
