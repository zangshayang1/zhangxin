package com.zhangxin.javacore.reflection;

import com.sun.tools.javac.util.Assert;
import com.zhangxin.javacore.reflection.models.Car;

import java.util.ArrayList;
import java.util.List;

public class ClassBasics {

  /**
   * Both conventional way of creating an object and reflective way of creating an object all go
   * through the following "loadClass" logic.
   *
   * public abstract class ClassLoader {
   *
   *   // implementation details omitted ...
   *
   *   public Class<?> loadClass(String name) throws ClassNotFoundException {
   *     return loadClass(name, false);
   *   }
   * }
   *
   */
  public static void main(String[] args) throws Exception {
    // During compile time, this line will load Class<Car> and create a singleton in heap.
    Car car1 = new Car();
    // The raw type "Class" is created by JVM
    Class<?> clazz = Class.forName("com.zhangxin.javacore.reflection.models.Car");
    // During runtime, the above line will load Class<Car> only if it hasn't been loaded.

    Object car2 = clazz.newInstance();
    // No matter how many objects of the type "Car" get created,
    // their "Class" are the same one.
    Assert.check(car1.getClass().hashCode() == car2.getClass().hashCode());

    /**
     * Getting Class from type is the same as getting class from object.
     */
    System.out.println("int.class = " + int.class); // Primitive
    System.out.println("Integer.class = " + Integer.class); // Primitive Contain
    System.out.println("void.class = " + void.class); // void
    System.out.println("List.class = " + List.class); // Interface
    System.out.println("String.class = " + String.class); // String Object
    System.out.println("Thread.State.class = " + Thread.State.class); // Enum
    System.out.println("Integer[].class = " + Integer[].class); // Array
    System.out.println("ArrayList.class = " + ArrayList.class); // Collection Implementation
  }
}
