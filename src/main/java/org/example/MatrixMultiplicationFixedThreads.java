package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicationFixedThreads {
    public MatrixMultiplicationFixedThreads(double[][] a, double[][] b, double[][] result) {
        this.a = a;
        this.b = b;
        this.result = result;
        this.n = a.length;
    }

    private static double[][] a;
    private static double[][] b;
    private static double[][] result;
    private static int n;

    public double[][] execute(int numThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < n; i++) {
            final int row = i;
            executorService.submit(() -> multiplyRow(row, n));
        }

        // Shut down the ExecutorService once all tasks have been submitted
        executorService.shutdown();

        try {
            // Wait for all tasks to complete
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

    private static void initializeMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.random();
            }
        }
    }
}
