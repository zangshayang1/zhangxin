package com.zhangxin.framework.mybatis;

import com.zhangxin.framework.mybatis.pojo.Person;
import com.zhangxin.framework.mybatis.pojo.Student;
import org.apache.ibatis.reflection.*;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.junit.Test;

public class ReflectionModuleDemo {

  /**
   * Reflector handles "reflective" operation from class.
   */
  @Test public void reflectorDemo() throws Exception {
    Reflector reflector = new DefaultReflectorFactory().findForClass(Person.class);
    System.out.println(reflector.getType());
    for (String getablePropertyName : reflector.getGetablePropertyNames()) {
      System.out.println(getablePropertyName);
    }
    for (String setablePropertyName : reflector.getSetablePropertyNames()) {
      System.out.println(setablePropertyName);
    }
    Object o = reflector.getDefaultConstructor().newInstance();
    System.out.println(o);
    String name = reflector.findPropertyName("name");
    System.out.println(name);

    Invoker nameGetInvoker = reflector.getGetInvoker("name");
    nameGetInvoker.invoke(o, new Object[] {});
    System.out.println(new Person().getClass().isAssignableFrom(Object.class));
    System.out.println(Object.class.isAssignableFrom(Person.class));
  }

  /**
   * MetaClass handles "reflective" operation with more complicated expressions
   */
  @Test public void metaClassDemo() {
    MetaClass metaClass = MetaClass.forClass(Student.class, new DefaultReflectorFactory());
    System.out.println(metaClass.findProperty("list"));
    System.out.println(metaClass.findProperty("list[0].id"));
    System.out.println(metaClass.hasGetter("list[0].id"));
  }

  /**
   * MetaObject handles "reflective" operation from "object"
   */
  @Test public void metaObjectDemo() {
    Person person = new Person();
    MetaObject metaObject = SystemMetaObject.forObject(person);
    metaObject.setValue("id", 666);
    System.out.println(metaObject.getValue("id"));
  }

}
