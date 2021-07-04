package com.zhangxin.javacore.concurrent.forkjoin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class DataGenerator {

	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String INPUT_FILENAME = "random-number.data";

	public static void main(String[] args) throws IOException {

		Random random = new Random();

		try (PrintWriter out = new PrintWriter(new File(USER_DIR + File.separator + INPUT_FILENAME))) {
			for (int i = 0; i < 1_000_000; i++) {

				out.println(random.nextInt());

				if (i % 10000 == 0)
					out.flush();
			}
		}
	}

}
