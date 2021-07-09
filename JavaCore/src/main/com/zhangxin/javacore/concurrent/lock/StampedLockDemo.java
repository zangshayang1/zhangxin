package com.zhangxin.javacore.concurrent.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * Demo the use of StampedLock
 */
public class StampedLockDemo {

  class Point {
    private double x, y;
    private final StampedLock lock = new StampedLock();

    // moving this requires acquiring a write lock
    public void move(double deltaX, double deltaY) {
      long stamp = lock.writeLock();
      try {
        x += deltaX;
        y += deltaY;
      } finally {
        lock.unlockWrite(stamp);
      }
    }

    // demo the use of pessimistic read lock and "tryConvertToWriteLock"
    public void moveIfAt(double oldX, double oldY, double newX, double newY) {
      // acquire pessimistic read lock
      long stamp = lock.readLock();
      try {
        while (x == oldX && y == oldY) {
          long writeStamp = lock.tryConvertToWriteLock(stamp);
          if (writeStamp != 0L) { // conversion succeeded
            stamp = writeStamp; // releasing the lock requires updated stamp
            x = newX;
            y = newY;
            break;
          } else { // conversion failed
            lock.unlockRead(stamp); // explicitly release read lock
            stamp = lock.writeLock(); // explicitly acquire write lock
          }
        }
      } finally {
        lock.unlock(stamp);
      }
    }

    // demo the use of optimistic read lock
    public double distanceFromOrigin() {
      // acquire optimistic lock
      long stamp = lock.tryOptimisticRead();
      double currentX = x, currentY = y;
      // check if there is any WRITE operation happens after optimistic read lock is granted
      if (!lock.validate(stamp)) {
        stamp = lock.readLock();
        try {
          currentX = x;
          currentY = y;
        } finally {
          lock.unlockRead(stamp);
        }
      }
      return Math.sqrt(currentX * currentX + currentY * currentY);
    }
  }

}
