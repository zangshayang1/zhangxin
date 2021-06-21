package com.zhangxin.javacore.reflection;


import com.zhangxin.javacore.reflection.models.Car;

import java.lang.reflect.Method;

public class ReflectionPerformance {

    /**
     * fun1耗时：5
     * fun2耗时：344
     * fun3耗时：264
     */
  public static void main(String[] args) throws Exception {
    fun1();
    fun2();
    fun3();
  }

  /**
   * Conventional
   */
  public  static void fun1(){
    long start = System.currentTimeMillis();
    Car car = new Car();
    for (int i = 0; i < 100000000; i++) {
        car.hi();
    }
    long end = System.currentTimeMillis();
    System.out.println("fun1耗时：" + (end-start));
  }

  /**
   * Reflection
   */
  public  static void fun2() throws Exception{
    long start = System.currentTimeMillis();
    Class<?> cls = Class.forName("com.zhangxin.javacore.reflection.models.Car");
    Object o = cls.newInstance();
    Method hi = cls.getMethod("hi");
    for (int i = 0; i < 100000000; i++) {
        hi.invoke(o);
    }
    long end = System.currentTimeMillis();
    System.out.println("fun2耗时：" + (end-start));
  }

  /**
   * Reflection without security check
   */
  public  static void fun3() throws Exception{
    long start = System.currentTimeMillis();
    Class<?> cls = Class.forName("com.zhangxin.javacore.reflection.models.Car");
    Object o = cls.newInstance();
    Method hi = cls.getMethod("hi");
    hi.setAccessible(true);
    for (int i = 0; i < 100000000; i++) {
        hi.invoke(o);
    }
    long end = System.currentTimeMillis();
    System.out.println("fun3耗时：" + (end-start));
  }
}
