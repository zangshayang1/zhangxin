package com.zhangxin.javacore.concurrent.forkjoin;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * in-place merge sort, this ForkJoinTask extends RecursiveAction, which doesn't have a return.
 */
public class ArrayMergerSortTask extends RecursiveAction {

	private static final int THRESHOLD = 1000;

	private final int[] array;
	private final int lo, hi;

	ArrayMergerSortTask(int[] array, int lo, int hi) {
		this.array = array;
		this.lo = lo;
		this.hi = hi;
	}

	private ArrayMergerSortTask(int[] array) {
		this(array, 0, array.length);
	}

  /**
   * Merge sort entry point
   *
   * RecursiveAction doesn't have a return value
   */
	protected void compute() {
		if (hi - lo < THRESHOLD)
			sortSequentially(lo, hi);
		else {
			int mid = (lo + hi) >>> 1;
      // invokeAll() not only submits the task into the current ForkJoinPool but also waits for execution done.
			invokeAll(new ArrayMergerSortTask(array, lo, mid), new ArrayMergerSortTask(array, mid, hi));
			merge(lo, mid, hi);
		}
	}

	private void sortSequentially(int lo, int hi) {
		Arrays.sort(array, lo, hi);
	}

  /**
   * in-place merge
   */
	private void merge(int lo, int mid, int hi) {
		int[] buf = Arrays.copyOfRange(array, lo, mid);
		for (int i = 0, k = lo, j = mid; i < buf.length; k++)
			array[k] = (j == hi || buf[i] < array[j]) ? buf[i++] : array[j++];
	}

	public static void main(String[] args) throws Exception {
		// 这里以一个长度为2千的数组做示例
		int length = 2_000;   //1.7
		int[] array = new int[length];
		// 填充数值
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			array[i] = random.nextInt();
			System.out.println(array[i]);
		}

		// 利用forkjoinpool来完成多线程快速归并排序
		ArrayMergerSortTask stask = new ArrayMergerSortTask(array);
		ForkJoinPool pool = new ForkJoinPool();
		pool.submit(stask);
		// 等待任务完成
		stask.get();

		System.out.println("----------排序后的结果:");
		for (int d : array) {
			System.out.println(d);
		}
	}
}
