package com.zhangxin.javacore.concurrent.threadlocal;

class Order {

  private String orderId;
  private String username;

  String getUsername() {
    return username;
  }

  void setUsername(String username) {
    this.username = username;
  }

  String getOrderId() {
    return orderId;
  }

  void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
