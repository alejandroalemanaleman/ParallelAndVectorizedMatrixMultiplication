package org.example;

import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class MatrixMultiplicationBenchmarking {

	@State(Scope.Thread)
	public static class Operands {

		@Param({"10", "100", "500", "1000", "2000", "3000"})
		private int n;
		/*
		@Param({"1", "2", "4", "8", "16"})
		private int num_threads;
		 */

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
		}


		@TearDown(Level.Trial)
		public void tearDown() {
			Runtime runtime = Runtime.getRuntime();
			long finalMemory = runtime.totalMemory() - runtime.freeMemory();

			ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
			long finalCpuTime = threadMXBean.getCurrentThreadCpuTime();

			long memoryUsed = finalMemory - initialMemory;
			long cpuTimeUsed = finalCpuTime - initialCpuTime;
			com.sun.management.OperatingSystemMXBean osBean =
					(com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

			int availableProcessors = osBean.getAvailableProcessors();
			double processCpuLoad = osBean.getProcessCpuLoad();
			double systemCpuLoad = osBean.getSystemCpuLoad();

			System.out.println("Matrix size: " + n + "x" + n);
			System.out.println("Total Memory used: " + memoryUsed / (1024 * 1024) + " MB");
			System.out.println("Process CPU Load: " + processCpuLoad * 100 + " %");
			System.out.println("System CPU Load: " + systemCpuLoad * 100 + " %");
			int coresUsed = (int) (processCpuLoad * availableProcessors);
			System.out.println("Available Processors: " + availableProcessors);
			System.out.println("Cores utilized: ~" + coresUsed);

		}
	}
/*
	@Benchmark
	public void multiplicationBasic(Operands operands) {
		BasicMatrixMultiplication basicMatrixMultiplication = new BasicMatrixMultiplication();
		basicMatrixMultiplication.execute(operands.a, operands.b);
	}

 */
/*
	@Benchmark
	public void multiplicationParallelStreams(Operands operands) {
		MatrixMultiplicationParallelStreams matrixMultiplicationParallelStreams = new MatrixMultiplicationParallelStreams();
		matrixMultiplicationParallelStreams.execute(operands.a, operands.b, operands.num_threads);
	}

 */
	@Benchmark
	public void multiplicationBasic(Operands operands) {
		BasicMatrixMultiplication basicMatrixMultiplication = new BasicMatrixMultiplication();
		basicMatrixMultiplication.execute(operands.a, operands.b);
	}

	@Benchmark
	public void multiplicationVectorized(Operands operands) {
		VectorizedMatrixMultiplication vectorizedMatrixMultiplication = new VectorizedMatrixMultiplication();
		vectorizedMatrixMultiplication.execute(operands.a, operands.b);
	}

	/*
	@Benchmark
	public void multiplicationFixedThreads(Operands operands) {
		double[][] result = new double[operands.a.length][operands.a.length];
		MatrixMultiplicationFixedThreads matrixMultiplicationFixedThreads = new MatrixMultiplicationFixedThreads(operands.a, operands.b, result);
		matrixMultiplicationFixedThreads.execute(16);
	}

	@Benchmark
	public void multiplicationAtomic(Operands operands) {
		Atomic matrixMultiplicationAtomic = new Atomic();
		matrixMultiplicationAtomic.execute(operands.a, operands.b);
	}

	 */
}