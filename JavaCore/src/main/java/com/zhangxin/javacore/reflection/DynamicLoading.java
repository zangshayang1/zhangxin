package com.zhangxin.javacore.reflection;

import com.zhangxin.javacore.reflection.models.Person;

import java.util.Scanner;

public class DynamicLoading {

  public static void main(String[] args) throws Exception {
    Scanner in = new Scanner(System.in);
    System.out.println("1 or 2 ?");
    String key = in.next();
    switch (key) {
      case "1":
        // static loading fails to compile when the class source cannot be found during compile time.
        Person p = new Person();
        break;
      case "2":
        // dynamic loading throws RuntimeException when the class source cannot be found during runtime.
        Class<?> clazz = Class.forName("com.zhangxin.javacore.reflection.models.Person");
        Object o = clazz.newInstance();
        break;
      default:
        System.out.println("Wrong input.");
    }
  }

}
