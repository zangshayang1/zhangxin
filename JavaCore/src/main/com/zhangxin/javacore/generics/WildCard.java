package com.zhangxin.javacore.generics;

public class WildCard {

  static class G<T> {}

  static class S0 {}
  static class S1 extends S0 {}
  static class S2 extends S1 {}

  static void acceptAnyGenericType(G<?> g) {}

  static void acceptUpperBoundedGenericType(G<? extends Number> g) {}

  static void acceptLowerBoundedGenericType(G<? super S2> g) {}

  public static void main(String[] args) {
    G<String> g1 = new G<>();
    G<Integer> g2 = new G<>();
    acceptAnyGenericType(g1);
    acceptAnyGenericType(g2);

    G<Double> g3 = new G<>();
    G<Number> g4 = new G<>();
    acceptUpperBoundedGenericType(g3);
    acceptUpperBoundedGenericType(g4);

    G<S0> g5 = new G<>();
    G<S1> g6 = new G<>();
    acceptLowerBoundedGenericType(g5);
    acceptLowerBoundedGenericType(g6);
  }
}


