package com.zhangxin.javacore.concurrent.distributed;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderIdGeneratorClient {

  private static int i = 0;

  public static String getNextOrderId() {
    i++;
    Date now = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
    return sdf.format(now) + i;
  }
}
