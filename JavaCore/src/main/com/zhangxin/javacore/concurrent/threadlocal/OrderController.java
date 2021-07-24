package com.zhangxin.javacore.concurrent.threadlocal;

class OrderController {

  // @Autowired
  private ThreadLocal<OrderRequest> requestSession;

  // @Autowired
  private OrderService orderService;

  boolean createOrder() {
    OrderRequest request = requestSession.get();
    System.out.println("Controller handling request: " + request.toString());
    return orderService.createOrder();
  }
}
