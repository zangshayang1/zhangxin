package com.zhangxin.javacore.reflection.models;

@MyAnnotation("chch")
public class Person extends AbstractPerson implements PersonInterface{

  private int id;

  int age;

  String name;

  protected String sex;

  public String address;

  public Person() {}

  public Person(int id, String name, String sex, String address) {
    this.id = id;
    this.name = name;
    this.sex = sex;
    this.address = address;
  }

  private Person(int id) {
    this.id = id;
  }

  protected Person(String name) {
    this.name = name;
  }

  private void privateFunc1(){
      System.out.println("Executing privateFunc1... ");
  }

  public void publicFunc2(){
    System.out.println("Executing publicFunc2... ");
  }

  protected void protectedFunc3(){
    System.out.println("Executing protectedFunc3... ");
  }

  void packagePrivateFunc4() {
    System.out.println("Executing packagePrivateFunc4... ");
  }

  @Override public void interfaceMethod() {
    System.out.println("This person is sleeping...");
  }

  @Override void superClassProtectedMethod() {
    System.out.println("This person is eating...");
  }
}
