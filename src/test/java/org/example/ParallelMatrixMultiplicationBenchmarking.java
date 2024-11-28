package org.example;

import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class ParallelMatrixMultiplicationBenchmarking {

	@State(Scope.Thread)
	public static class Operands {

		@Param({"10", "100", "500", "1000", "2000", "3000"})
		private int n;

		@Param({"1", "2", "4", "8", "16"})
		private int numThreads;

		private double[][] a;
		private double[][] b;

		private long initialMemory;

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
			long memoryUsed = finalMemory - initialMemory;

			com.sun.management.OperatingSystemMXBean osBean =
					(com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
			double processCpuLoad = osBean.getProcessCpuLoad();
			double systemCpuLoad = osBean.getSystemCpuLoad();

			System.out.println("\nMatrix size: " + n + "x" + n);
			System.out.println("Total Memory used: " + memoryUsed / (1024) + " KB");
			System.out.println("Process CPU Load: " + processCpuLoad * 100 + " %");
			System.out.println("System CPU Load: " + systemCpuLoad * 100 + " %");

		}
	}

	@Benchmark
	public void multiplicationParallelStreams(Operands operands) {
		ParallelStreamsMatrixMultiplication parallelStreamsMatrixMultiplication = new ParallelStreamsMatrixMultiplication(operands.numThreads);
		parallelStreamsMatrixMultiplication.execute(operands.a, operands.b);
	}

	@Benchmark
	public void multiplicationFixedThreads(Operands operands) {
		FixedThreadsMatrixMultiplication fixedThreadsMatrixMultiplication = new FixedThreadsMatrixMultiplication(operands.numThreads);
		fixedThreadsMatrixMultiplication.execute(operands.a, operands.b);
	}

}