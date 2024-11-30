package org.example;


import java.util.concurrent.Semaphore;

public class SemaphoreMatrixMultiplication implements MatrixMultiplication{
    private final int numthreads;
    private int numThreads;
    public SemaphoreMatrixMultiplication(int numThreads) {
        this.numthreads = numThreads;
    }

    public double[][] execute(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        Semaphore semaphore = new Semaphore(numThreads);

        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            final int row = i;
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threads[i] = new Thread(() -> {
                for (int j = 0; j < n; j++) {
                    double sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += A[row][k] * B[k][j];
                    }
                    C[row][j] = sum;
                }
                semaphore.release();
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return C;
    }
}