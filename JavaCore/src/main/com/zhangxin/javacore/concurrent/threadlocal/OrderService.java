package com.zhangxin.javacore.concurrent.threadlocal;

class OrderService {

  // @Autowired
  private ThreadLocal<OrderRequest> requestSession;

  // @Autowired
  private OrderDao orderDao;

  // @Autowired
  private OrderIdGenerator orderIdGenerator;

  boolean createOrder() {
    OrderRequest request = requestSession.get();
    System.out.println("Service handling request: " + request.toString());
    Order order = new Order();
    order.setOrderId(orderIdGenerator.getNextId());
    order.setUsername(request.getUsername());
    return orderDao.save(order);
  }
}
