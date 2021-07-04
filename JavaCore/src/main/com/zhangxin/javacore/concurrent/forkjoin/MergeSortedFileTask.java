package com.zhangxin.javacore.concurrent.forkjoin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class MergeSortedFileTask extends RecursiveTask<String> {

	private List<String> partFiles;
	private int lo, hi;

	private String filename;

	private MergeSortedFileTask(List<String> partFiles, int lo, int hi, String filename) {
		super();
		this.partFiles = partFiles;
		this.lo = lo;
		this.hi = hi;
		this.filename = filename;
	}

	public MergeSortedFileTask(List<String> partFiles, String filename) {
		this(partFiles, 0, partFiles.size(), filename);
	}

	@Override
	protected String compute() {
		int fileCount = hi - lo;

    // terminating condition 1
		if (fileCount == 1) {
		  // under extreme case, there is only one file passed into this MergeSortedFileTask in this pool
      // no output stream is created and this.filename will not be used at all
      // return the original file name and rename it out of this ForkJoinPool
      return partFiles.get(lo);
    }

    // terminating condition 2
    if (fileCount == 2) {
      try {
        mergeFile(partFiles.get(lo), partFiles.get(lo + 1));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      // divide
      int mid = fileCount / 2;
      MergeSortedFileTask task1 = new MergeSortedFileTask(partFiles, lo, lo + mid, this.filename + "-1");
      MergeSortedFileTask task2 = new MergeSortedFileTask(partFiles, lo + mid, hi, this.filename + "-2");

      // submit the task into the current ForkJoinPool
      task1.fork();
      task2.fork();

      // conquer
      try {
        // use get() to wait for execution done
        // mergeFile() takes the name of two sorted files, returned by two tasks
        mergeFile(task1.get(), task2.get());
      } catch (IOException | InterruptedException | ExecutionException e) {

        e.printStackTrace();
      }
    }
    return filename;
	}

	private void mergeFile(String f1, String f2) throws IOException {
		try (
		    // try-with-resources
        // 2 input stream + 1 output stream
        BufferedReader reader1 = new BufferedReader(new FileReader(f1));
        BufferedReader reader2 = new BufferedReader(new FileReader(f2));
        PrintWriter pw = new PrintWriter(filename)
    ) {
			String s1 = reader1.readLine(), s2 = reader2.readLine();

			while (s1 != null || s2 != null) {

        // s1 is done
        // stream line s2
				if (s1 == null) {
					while (s2 != null) {
						pw.println(s2);
						s2 = reader2.readLine();
					}
					return ;
				}

				// s2 is done
        // stream line s1
				if (s2 == null) {
					while (s1 != null) {
						pw.println(s1);
						s1 = reader1.readLine();
					}
					return ;
				}

				// compare
        Integer d1 = Integer.valueOf(s1);
        Integer d2 = Integer.valueOf(s2);
        if (d1 <= d2) {
          pw.println(s1);
          s1 = reader1.readLine();
        } else {
          pw.println(s2);
          s2 = reader2.readLine();
        }
			}
		}
	}
}
