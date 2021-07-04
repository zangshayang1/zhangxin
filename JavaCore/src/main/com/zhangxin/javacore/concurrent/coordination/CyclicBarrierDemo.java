package com.zhangxin.javacore.concurrent.coordination;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

  private static volatile int batch = 0;
  private static final Random RANDOM = new Random();

  public static void main(String[] args) {
    startTogether();
//    multiStageStartTogether();
  }

	private static void startTogether() {

    int concurrency = 100;

		CyclicBarrier cb = new CyclicBarrier(10, new Runnable() {

			@Override
			public void run() {
				System.out.println("Hit specified number of parties, trigger barrier runnable");
				batch++;
        System.out.println("Batch - " + batch);
			}
		});

		for (int i = 0; i < concurrency; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// prepare itself
					try {
						Thread.sleep(RANDOM.nextInt(10000));
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					System.out.println(Thread.currentThread().getName() + " Ready");

					try {
						cb.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
						return;
					}

					System.out.println(Thread.currentThread().getName() + " Done");
				}
			}).start();
		}
	}

  private static int STAGE = 0;

  public static void multiStageStartTogether() {

    int partyNum = 3;

    final CyclicBarrier cb = new CyclicBarrier(partyNum, new Runnable() {
      @Override
      public void run() {
        switch (STAGE) {
          case 0:
            System.out.println("********** STAGE 0: Every one is at the company **********");
            break;
          case 1:
            System.out.println("********** STAGE 1: Every one is at the park **********");
            break;
          case 2:
            System.out.println("********** STAGE 2: Every one is at the restaurant **********");
            break;
        }
        // stage switch
        STAGE++;
      }
    });

    for (int i = 0; i < partyNum; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {

          String staff = "[" + Thread.currentThread().getName() + "]";

          try {
            System.out.println(staff + " going to company...");
            Thread.sleep(RANDOM.nextInt(5000));

            // wait for every one to gather at the company before going to park
            cb.await();

            System.out.println(staff + " going to park...");
            Thread.sleep(RANDOM.nextInt(5000));

            // wait for every one to gather at the park before going to restaurant
            cb.await();

            System.out.println(staff + " going to restaurant...");
            Thread.sleep(RANDOM.nextInt(5000));

            // wait for every one to gather at the restaurant before call it a day
            cb.await();

            System.out.println(staff + " going back home...");

          } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
          }

        }
      }).start();
    }
  }

}
