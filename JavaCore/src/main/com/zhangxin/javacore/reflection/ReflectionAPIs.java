package com.zhangxin.javacore.reflection;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reflective APIs
 */
public class ReflectionAPIs {

  public static void main(String[] args) throws Exception {

    Class<?> cls = Class.forName("com.zhangxin.javacore.reflection.models.Person");
    System.out.println("cls.getName() = " + cls.getName());
    System.out.println("cls.getSimpleName() = " + cls.getSimpleName());
    System.out.println("cls.getPackage() = " + cls.getPackage());
    System.out.println("cls.getSuperclass() = " + cls.getSuperclass());

    // Get public fields
    System.out.println("==== Get public fields ====");
    Field[] fields = cls.getFields();
    for (Field field : fields) {
        System.out.println("field.getName() = " + field.getName());
    }

    // Get all fields
    System.out.println("==== Get all fields ====");
    Field[] declaredFields = cls.getDeclaredFields();
    for (Field declaredField : declaredFields) {
        System.out.println("declaredField.getName() = " + declaredField.getName());
    }

    // Get all public methods including those inherited from Object
    System.out.println("==== Get all public methods including those inherited ====");
    Method[] methods = cls.getMethods();
    for (Method method : methods) {
        System.out.println("method.getName() = " + method.getName());
    }

    // Get all declared methods excluding those inherited from Object
    System.out.println("==== Get all declared methods excluding those inherited from Object ====");
    for (Method declaredMethod : cls.getDeclaredMethods()) {
        System.out.println("declaredMethod.getName() = " + declaredMethod.getName());
    }

    // Get public constructors
    System.out.println("==== Get public constructors ====");
    Constructor<?>[] constructors = cls.getConstructors();
    for (Constructor<?> constructor : constructors) {
        System.out.println("constructor.getName() = " + constructor.getName());
    }

    // Get all constructors
    System.out.println("==== Get all constructors ====");
    for (Constructor<?> declaredConstructor : cls.getDeclaredConstructors()) {
        System.out.println("declaredConstructor.getName() = " + declaredConstructor.getName());
    }

    System.out.println("==== Get interfaces ====");
    Class<?>[] interfaces = cls.getInterfaces();
    for (Class<?> anInterface : interfaces) {
        System.out.println("anInterface.getName() = " + anInterface.getName());
    }

    System.out.println("==== Get annotations ====");
    Annotation[] annotations = cls.getAnnotations();
    for (Annotation annotation : annotations) {
        System.out.println("annotation.annotationType() = " + annotation);
    }

    // Get field modifier
    System.out.println("==== Get field modifier ====");
    Field age = cls.getDeclaredField("age");
    System.out.println("age.getModifiers() = " + age.getModifiers());
    System.out.println("age.getType() = " + age.getType());

    // Method invocation
    System.out.println("==== Method invocation ====");
    Method method = cls.getMethod("publicFunc2");
    Object o1 = cls.newInstance();
    method.invoke(o1);
    System.out.println("method.getReturnType() = " + method.getReturnType());
    System.out.println("method.getReturnType() = " + method.getParameterTypes());

    // Constructor invocation
    System.out.println("==== Constructor invocation ====");
    Constructor<?> constructor = cls.getDeclaredConstructor(int.class, String.class, String.class, String.class);
    Object o2 = constructor.newInstance(18, "haha", "hehe", "xixi");
    System.out.println("o2 = " + o2);
  }
}
