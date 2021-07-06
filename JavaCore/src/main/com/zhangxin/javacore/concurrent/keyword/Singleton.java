package com.zhangxin.javacore.concurrent.keyword;

public class Singleton {

  // the big question here is why it needs volatile keyword
  private static volatile Singleton Instance;

  private Singleton() {
    if (Instance != null) {
      // prevent maliciously creating multiple instances using Reflection
      throw new RuntimeException();
    }
  }

  /**
   * A a = new A();
   *
   * The above is not an atomic operation.
   *
   * It involves:
   * Step 1: memory address allocation
   * Step 2: initialization
   * Step 3: point variable a to the address
   */
  public static Singleton getInstance() {
    // double check
    // first check rule out all future attempts to create new instance
    // second check rule out initial concurrent attempt to create new instance
    if (Instance == null) {
      // Synchronized keyword prevent concurrent modification, which is still not enough
      // because step 3 can happen before step 2 (due to instruction reorder optimization)
      // in which case, if another thread takes over, it will return the uninitialized Instance.
      // That's why we have volatile keyword in front of Instance member, which prevents instruction reorder.
      synchronized (Singleton.class) {
        if (Instance == null) {
          Instance = new Singleton();
        }
      }
    }
    return Instance;
  }

}
