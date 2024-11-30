package org.example;

public class SynchronizedBlocksMatrixMultiplication implements  MatrixMultiplication{

    private double[][] a;
    private double[][] b;
    private double[][] result;
    private int nThreads;

    public SynchronizedBlocksMatrixMultiplication(int nThreads) {
        this.nThreads = nThreads;
    }

    public double[][] execute(double[][] a, double[][] b) {
        result = new double[a.length][b[0].length];
        Thread[] threads = new Thread[nThreads];
        int rowsPerThread = a.length / nThreads;

        for (int i = 0; i < nThreads; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = (i == nThreads - 1) ? a.length : startRow + rowsPerThread;

            threads[i] = new Thread(() -> {
                for (int row = startRow; row < endRow; row++) {
                    for (int col = 0; col < b[0].length; col++) {
                        double sum = 0;
                        for (int k = 0; k < a[0].length; k++) {
                            sum += a[row][k] * b[k][col];
                        }
                        synchronized (this) {
                            result[row][col] = sum;
                        }
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}