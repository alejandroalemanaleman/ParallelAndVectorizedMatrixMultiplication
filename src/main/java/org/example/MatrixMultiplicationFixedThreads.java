package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicationFixedThreads {
    private static final int SIZE = 1024;
    private static final double[][] a = new double[SIZE][SIZE];
    private static final double[][] b = new double[SIZE][SIZE];
    private static final double[][] result = new double[SIZE][SIZE];

    public static void main(String[] args) {
        // Initialize matrices
        initializeMatrix(a);
        initializeMatrix(b);

        // Define the fixed number of threads
        int numThreads = 16;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        long startTime = System.currentTimeMillis();

        // Submit each row multiplication task to the ExecutorService
        for (int i = 0; i < SIZE; i++) {
            final int row = i;
            executorService.submit(() -> multiplyRow(row));
        }

        // Shut down the ExecutorService once all tasks have been submitted
        executorService.shutdown();

        try {
            // Wait for all tasks to complete
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time with a fixed number of threads: " + (endTime - startTime) + " ms");
    }

    private static void multiplyRow(int row) {
        // Print the current thread name
        System.out.println("Executing thread: " + Thread.currentThread().getName());
        for (int j = 0; j < SIZE; j++) {
            for (int k = 0; k < SIZE; k++) {
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
