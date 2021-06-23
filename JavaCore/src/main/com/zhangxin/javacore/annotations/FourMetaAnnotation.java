package com.zhangxin.javacore.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // Where it takes effect? Class, method, field or parameter?
@Retention(RetentionPolicy.RUNTIME) // when it takes effect? Runtime? Compile time? or before compile?
@Documented // Is it documented?
@Inherited // Is it effective once the applied class gets inherited?
public @interface FourMetaAnnotation {}
