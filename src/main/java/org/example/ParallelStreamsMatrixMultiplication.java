package org.example;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.stream.IntStream;

public class ParallelStreamsMatrixMultiplication implements MatrixMultiplication{
    public ParallelStreamsMatrixMultiplication(int numThreads) {
        this.numThreads = numThreads;
    }
    private final int numThreads;

    public double[][] execute(double[][] a, double[][] b) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(this.numThreads));

        int size = a.length;
        double[][] result = new double[size][size];

        // Matrix multiplication using parallel streams
        IntStream.range(0, size).parallel().forEach(i -> {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        });

        return result;
    }

    private static void printSystemInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();

        System.out.println("System Information:");
        System.out.println("Operating System: " + osBean.getName() + " " + osBean.getVersion());
        System.out.println("Architecture: " + osBean.getArch());
        System.out.println("Available Processors: " + osBean.getAvailableProcessors());
        System.out.println("Total Memory (MB): " + (runtime.totalMemory() / 1024 / 1024));
        System.out.println("Free Memory (MB): " + (runtime.freeMemory() / 1024 / 1024));
        System.out.println("Active Thread Count: " + Thread.activeCount());
    }
}
