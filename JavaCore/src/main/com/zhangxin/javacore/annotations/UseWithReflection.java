package com.zhangxin.javacore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@TestAnnotation(className = "com.zhangxin.javacore.annotations.TestClass",methodName = "haha")
public class UseWithReflection {

  public static void main(String[] args) throws Exception {
    Class<UseWithReflection> cls = UseWithReflection.class;
    TestAnnotation annotation = cls.getAnnotation(TestAnnotation.class);
    if(annotation != null){
      String className = annotation.className();
      String methodName = annotation.methodName();
      Class<?> aClass = Class.forName(className);
      Method declaredMethod = aClass.getDeclaredMethod(methodName);
      Object o = aClass.newInstance();
      declaredMethod.invoke(o);
    }
  }
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation {
  String className();
  String methodName();
}

class TestClass {
  public void haha(){
        System.out.println("haha");
    }
}
