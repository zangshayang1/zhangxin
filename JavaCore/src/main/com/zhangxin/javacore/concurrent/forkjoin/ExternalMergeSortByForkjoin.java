package com.zhangxin.javacore.concurrent.forkjoin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static com.zhangxin.javacore.concurrent.forkjoin.DataGenerator.INPUT_FILENAME;
import static com.zhangxin.javacore.concurrent.forkjoin.DataGenerator.USER_DIR;

public class ExternalMergeSortByForkjoin {

  private static final String PART_PREFIX = "part-";
  private static final String OUTPUT_FILENAME = "sorted.data";

  public static void main(String[] args) throws Exception {

    ForkJoinPool pool = new ForkJoinPool();

    // read 1/10 of the data and split them into 10 files
    int size = 100_000;
    int[] array = new int[size];
    BufferedReader reader = new BufferedReader(
        new FileReader(USER_DIR + File.separator + INPUT_FILENAME));

    String line = null;
    int i = 0;
    int partition = 0;
    List<String> partFilenames = new ArrayList<>();

    while ((line = reader.readLine()) != null) {
      array[i++] = Integer.parseInt(line);
      if (i == size) {
        i = 0;
        String filename = USER_DIR + File.separator + PART_PREFIX + partition + ".txt";
        mergeSortPartition(pool, filename, array, 0, size);
        partFilenames.add(filename);
        partition++;
      }
    }

    reader.close();

    // handle remaining numbers
    if (i > 0) {
      String filename = USER_DIR + File.separator + PART_PREFIX + partition + ".txt";
      mergeSortPartition(pool, filename, array, 0, i);
      partFilenames.add(filename);
      partition++;
    }

    // start merging sorted files
    if (partition > 1) {
      MergeSortedFileTask mtask = new MergeSortedFileTask(partFilenames,
          USER_DIR + File.separator + OUTPUT_FILENAME);
      pool.submit(mtask);
      mtask.get();

    } else {
      // rename the only partition
    }
    pool.shutdown();
  }

  private static void mergeSortPartition(ForkJoinPool pool, String filename, int[] array, int start,
      int end) throws Exception {
    ArrayMergerSortTask task = new ArrayMergerSortTask(array, start, end);
    pool.submit(task);
    task.get();

    // write out sorted partition as a file
    try (PrintWriter pw = new PrintWriter(filename)) {
      for (int i = start; i < end; i++) {
        pw.println(array[i]);
      }
    }
  }
}
