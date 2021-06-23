package com.zhangxin.javacore.generics;

class GenericMethod<T>{

  // directly use generic type declared at class level
  void fun1(T t) {}

  // static method cannot directly use generic type declared at class level
  // static void fun2(T t) {}

  // method level declaration takes precedence if it uses same cap as the class level generic type
  <T> void fun3(T t) {}

  // parameterized static method usually comes with self-declared generic type
  static <T> void fun4(T t) {}

  // method level declaration can be completely independent
  <K, E> void fun(K k, E e) {}
}
