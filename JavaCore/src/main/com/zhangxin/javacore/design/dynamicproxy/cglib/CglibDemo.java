package com.zhangxin.javacore.design.dynamicproxy.cglib;

import com.zhangxin.javacore.design.dynamicproxy.reflect.Executor;
import com.zhangxin.javacore.design.dynamicproxy.reflect.ExecutorImplA;
import com.zhangxin.javacore.design.dynamicproxy.reflect.ExecutorImplB;
import net.sf.cglib.proxy.Enhancer;

public class CglibDemo {

  public static void main(String[] args) {

    Object executorA = new ExecutorImplA();
    Object executorB = new ExecutorImplB();

    /*
     * Generate proxies against defined interfaces
     */
    Enhancer interfaceProxyGenerator = new Enhancer();

    interfaceProxyGenerator.setCallback(new InterfaceProxyInterceptor(executorA));
    interfaceProxyGenerator.setInterfaces(new Class[] { Executor.class });
    Executor proxy = (Executor) interfaceProxyGenerator.create();
    proxy.execute();
    interfaceProxyGenerator.setCallback(new InterfaceProxyInterceptor(executorB));
    interfaceProxyGenerator.setInterfaces(new Class[] { Executor.class });
    proxy = (Executor) interfaceProxyGenerator.create();
    proxy.execute();

    /*
     * Generate proxies against defined classes
     */
    Enhancer superclassProxyGenerator = new Enhancer();
    superclassProxyGenerator.setCallback(new SuperclassProxyInterceptor());

    superclassProxyGenerator.setSuperclass(ExecutorImplA.class);
    proxy = (Executor) superclassProxyGenerator.create();
    proxy.execute();
    superclassProxyGenerator.setSuperclass(ExecutorImplB.class);
    proxy = (Executor) superclassProxyGenerator.create();
    proxy.execute();
  }
}
