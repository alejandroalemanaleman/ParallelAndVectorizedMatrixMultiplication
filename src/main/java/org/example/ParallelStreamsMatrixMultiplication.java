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

        IntStream.range(0, size).parallel().forEach(i -> {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        });

        return result;
    }
}
