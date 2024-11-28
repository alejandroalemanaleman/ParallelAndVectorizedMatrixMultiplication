package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedThreadsMatrixMultiplication implements MatrixMultiplication{
    public FixedThreadsMatrixMultiplication(int numThreads) {
        this.numThreads = numThreads;
    }

    private static double[][] a;
    private static double[][] b;
    private static double[][] result;
    private static int n;
    private static int numThreads;

    public double[][] execute(double[][] a, double[][] b) {
        this.a = a;
        this.b = b;
        this.result = new double[a.length][a.length];
        this.n = a.length;

        ExecutorService executorService = Executors.newFixedThreadPool(this.numThreads);

        for (int i = 0; i < n; i++) {
            final int row = i;
            executorService.submit(() -> multiplyRow(row, n));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    return result;
    }

    private static void multiplyRow(int row, int n) {

        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                result[row][j] += a[row][k] * b[k][j];
            }
        }
    }
}
