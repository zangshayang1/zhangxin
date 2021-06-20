package com.zhangxin.javacore.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Accessibility {

  public static void main(String[] args) throws  Exception{

    Class<?> cls = Class.forName("com.zhangxin.javacore.reflection.models.Person");

    Constructor<?> declaredConstructor = cls.getDeclaredConstructor(int.class);
    System.out.println("Privately declared constructor = " + declaredConstructor);
    declaredConstructor.setAccessible(true);
    Object o = declaredConstructor.newInstance(0);

    Field privateFieldId = cls.getDeclaredField("id");
    System.out.println("Privately declared field = " + privateFieldId);
    privateFieldId.setAccessible(true);
    privateFieldId.set(o, 1);

    Method privateMethod = cls.getDeclaredMethod("privateFunc1");
    System.out.println("Privately declared field = " + privateMethod);
    privateMethod.setAccessible(true);
    privateMethod.invoke(o);
  }
}
