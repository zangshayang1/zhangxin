package com.zhangxin.javacore.concurrent.thread.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class ThreadStateDemo5_IO {

  public static void main(String[] args) throws Throwable {
    Charset charset = Charset.forName("UTF-8");
    Thread t1 = new Thread(() -> {
      try (ServerSocket ss = new ServerSocket(9000);) {
        while (true) {
          try {
            System.out.println("t1 started...");
            // accept() blocks until a connection is made
            // t1 is waiting for IO but its state is RUNNABLE
            Socket s = ss.accept();
            System.out.println("t1 connected.");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(s.getInputStream(), charset));

            String data = null;
            System.out.println("t1 started to read input stream...");

            while ((data = reader.readLine()) != null) {
              System.out.println(data);
            }
            s.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    t1.start();
    Thread.sleep(3000L);
    System.out.println("t1 state: " + t1.getState());
    Thread.sleep(20000L);
    System.out.println("t1 state: " + t1.getState());
  }
}
