package com.zhangxin.javacore.generics;

import java.lang.reflect.*;


public class Erasure {

  static class G<T> {

    T fieldName;

    <K extends Number> K fun(K k) { return k; }
  }

  interface Interface<T> {
    T echo(T t);
  }

  static class Implementation implements Interface<String> {
    @Override
    public String echo(String s) {
      return s;
    }
  }

  /*
  * Class Implementation is known to implement Interface<String> during compile time.
  *
  * The "echo" method under Interface is still erased to take an Object and return an Object.
  *
  * But the "echo" method under Implementation should take a String and return a String.
  *
  * The above creates a violation to @Override rule.
  *
  * Therefore a "Bridge Method" will be added to the Implementation.class source to resolve the conflict.
  *
  * static class Implementation implements Interface<String> {
  *   public String echo(String s) {
  *     return s;
  *   }
  *
  *   @Override
  *   public Object echo(Object o) {
  *     return o;
  *   }
  * }
  */

  public static void main(String[] args) {

    for (Field f : G.class.getDeclaredFields()) {
      // "fieldName:Object" -> Unbounded generics will be erased and replaced by Object during runtime.
      System.out.println(f.getName() + ":" + f.getType().getSimpleName());
    }

    for (Method m : G.class.getDeclaredMethods()) {
      // "fun:Number" -> Bounded generics will be erased and replaced by the bound type during runtime.
      System.out.println(m.getName() + ":" + m.getReturnType().getSimpleName());
    }

    Implementation impl = new Implementation();
    impl.echo("haha");
  }
}

