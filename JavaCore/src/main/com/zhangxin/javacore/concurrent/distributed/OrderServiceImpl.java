package com.zhangxin.javacore.concurrent.distributed;

public class OrderServiceImpl implements OrderService {

  // different services can use the same implementation of distributed lock specifying different "rootPath"
  private DistributedLock distributedLock = new DistributedLockImpl("/order_lock");

  @Override public String createOrder() {
    String orderId;
    // lock pattern
    try {
      distributedLock.lock();
      // use static invocation to mimic client side singleton
      orderId = OrderIdGeneratorClient.getNextOrderId();
    } finally {
      distributedLock.unlock();
    }
    return orderId;
  }
}
