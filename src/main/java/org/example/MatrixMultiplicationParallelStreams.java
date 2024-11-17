package org.example;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.stream.IntStream;

public class MatrixMultiplicationParallelStreams {
    public static void main(String[] args) {
        // Force the use of 8 threads in ForkJoinPool
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

        // Get system information
        printSystemInfo();

        int size = 1024;
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];
        double[][] result = new double[size][size];

        // Matrix initialization
        initializeMatrix(a);
        initializeMatrix(b);

        long startTime = System.currentTimeMillis();

        // Matrix multiplication using parallel streams
        IntStream.range(0, size).parallel().forEach(i -> {
            System.out.println("Executing thread: " + Thread.currentThread().getName());
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        });

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time with parallel streams: " + (endTime - startTime) + " ms");
    }

    private static void initializeMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.random();
            }
        }
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
