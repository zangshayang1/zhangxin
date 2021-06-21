package com.zhangxin.javacore.reflection;

public class StaticInitialization {

  /**
   * During Loading -> Linking -> Preparation Stage:
   *
   * n1: non-static member is not even allocated in memory
   * n2: static member is initialized with default value 0
   * n3: final static member is assigned with value 30
   */
  public int n1 =10;
  public static int n2 = 20;
  public static final  int n3 = 30;


  public static void main(String[] args) {
    /*
     * 1. Loading
     *
     * 2. Linking
     *   StaticCodeExecution.numA = 0;
     *   StaticCodeExecution.numB = 0;
     *
     * 3. initialization - Auto merge and execute all static operations
     *    <clinit>() {
     *      numA = 5;
     *      System.out.println("StaticCodeExecution 静态代码块执行了");
     *      numA = 8;
     *      numB = 20;
     *      numB = 10;
     *    }
     */
    System.out.println("StaticCodeExecution.numA = " + StaticCodeExecution.numA);
    System.out.println("StaticCodeExecution.numB = " + StaticCodeExecution.numB);
  }
}

class StaticCodeExecution {

  static int numA = 5;

  static {
    System.out.println("StaticCodeExecution 静态代码块执行了");
    numA = 8;
    numB = 20;
  }

  static int numB = 10;
}
