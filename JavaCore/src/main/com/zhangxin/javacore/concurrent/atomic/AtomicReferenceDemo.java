package com.zhangxin.javacore.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

  public static void main(String[] args) {
    User user1 = new User("AAA", 23);
    User user2 = new User("BBB", 25);
    User user3 = new User("CCC", 20);

    // set
    AtomicReference<User> atomicReference = new AtomicReference<>();
    atomicReference.set(user1);

    // CAS succeeds
    System.out.println("user1, user2: " + atomicReference.compareAndSet(user1, user2));
    System.out.println(atomicReference.get());

    // CAS fails
    System.out.println("user1, user3: " + atomicReference.compareAndSet(user1, user3));
    System.out.println(atomicReference.get());
  }

  static class User {

    private String name;
    private Integer age;

    User(String name, Integer age) {
      this.name = name;
      this.age = age;
    }

    @Override public String toString() {
      return "User{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
  }
}
