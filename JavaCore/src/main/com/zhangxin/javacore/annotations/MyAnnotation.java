package com.zhangxin.javacore.annotations;

/**
 * The compiled source code of this annotation will look like:
 *
 * public interface MyAnnotation extends java.lang.annotation.Annotation {
 *   ...
 * }
 */
public @interface MyAnnotation {

  // Annotation allows for abstract method that returns the following 5 types:

  String stringValue();

  Enum enumValue();

  int intValue();

  Anno annotationValue();

  String[] arrayValue();


}

@interface Anno {}

enum Enum {
  A, B, C, D;
}