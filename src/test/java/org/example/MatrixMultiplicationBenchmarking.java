package org.example;

import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class MatrixMultiplicationBenchmarking {

	@State(Scope.Thread)
	public static class Operands {

		@Param({"10", "100", "500", "1000", "3000"})
		private int n;

		private double[][] a;
		private double[][] b;

		private long initialMemory;
		private long initialCpuTime;

		@Setup(Level.Trial)
		public void setup() {
			a = new double[n][n];
			b = new double[n][n];
			Random random = new Random();

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					a[i][j] = random.nextDouble();
					b[i][j] = random.nextDouble();
				}
			}
			Runtime runtime = Runtime.getRuntime();
			runtime.gc();
			initialMemory = runtime.totalMemory() - runtime.freeMemory();

			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
			initialCpuTime = threadMXBean.getCurrentThreadCpuTime();
		}


		@TearDown(Level.Trial)
		public void tearDown() {
			Runtime runtime = Runtime.getRuntime();
			long finalMemory = runtime.totalMemory() - runtime.freeMemory();

			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
			long finalCpuTime = threadMXBean.getCurrentThreadCpuTime();

			long memoryUsed = finalMemory - initialMemory;
			long cpuTimeUsed = finalCpuTime - initialCpuTime;

			System.out.println("Matrix size: " + n + "x" + n);
			System.out.println("Total Memory used: " + memoryUsed / (1024 * 1024) + " MB");
			System.out.println("Total CPU time used: " + cpuTimeUsed + " nanoseconds");
		}
	}

	@Benchmark
	public void multiplicationBasic(Operands operands) {
		BasicMatrixMultiplication basicMatrixMultiplication = new BasicMatrixMultiplication();
		basicMatrixMultiplication.execute(operands.a, operands.b);
	}

	@Benchmark
	public void multiplicationAtomic(Operands operands) {
		MatrixMultiplicationAtomic matrixMultiplicationAtomic = new MatrixMultiplicationAtomic();
		matrixMultiplicationAtomic.execute(operands.a, operands.b);
	}
}