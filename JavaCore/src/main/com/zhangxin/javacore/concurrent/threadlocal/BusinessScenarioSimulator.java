package com.zhangxin.javacore.concurrent.threadlocal;

public class BusinessScenarioSimulator {

  private static OrderController OrderController = new OrderController();

  // Using ThreadLocal to mimic a session that contains the original request
  // where downstream process can conveniently get the request
  private static final ThreadLocal<OrderRequest> requestSession = new ThreadLocal<>();

  // Simulate 100 concurrent requests hitting the service
  // the service created a 100 threads to handle the request
  // with each thread stores its request into the "session".
  // Note that each "session" is uniquely mapped to each thread only, including all downstream process.
  public static void start() {
    for (int i = 0; i < 100; i++) {
      new Thread(() -> {
        OrderRequest request = new OrderRequest();
        request.setUsername(Thread.currentThread().getName());
        try {
          requestSession.set(request);
          OrderController.createOrder();
        } finally {
          requestSession.remove();
        }
      }).start();
    }
  }
}
