package com.zhangxin.javacore.concurrent.lock;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * It demo the use of ReadWriteLock
 */
class ThreadSafeDictionary<K, V> {

  private final Map<K, V> m = new TreeMap<K, V>();
  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
  private final Lock r = rwl.readLock();
  private final Lock w = rwl.writeLock();

  public V get(K key) {
    r.lock();
    try {
      return m.get(key);
    } finally {
      r.unlock();
    }
  }

  public K[] allKeys() {
    r.lock();
    try {
      return (K[]) m.keySet().toArray();
    } finally {
      r.unlock();
    }
  }

  public V put(K key, V value) {
    w.lock();
    try {
      return m.put(key, value);
    } finally {
      w.unlock();
    }
  }

  public void clear() {
    w.lock();
    try {
      m.clear();
    } finally {
      w.unlock();
    }
  }
}
