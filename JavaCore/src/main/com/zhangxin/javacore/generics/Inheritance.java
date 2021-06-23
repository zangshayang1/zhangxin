package com.zhangxin.javacore.generics;

public class Inheritance {

  static class ParentClass<T> {}

  static class ChildClass1<T> extends ParentClass<T> {}

  static class ChildClass2<K> extends ParentClass<String> {}

  interface ParentInterface<T> {}

  static class ChildImplementation1<T> implements ParentInterface<T> {}

  static class ChildImplementation2<K> implements ParentInterface<String> {}
}
