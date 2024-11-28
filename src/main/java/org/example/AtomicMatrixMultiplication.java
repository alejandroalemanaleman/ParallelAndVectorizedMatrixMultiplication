package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicMatrixMultiplication implements MatrixMultiplication{

    public double[][] execute (double[][] matrixA, double[][] matrixB) {
        int n = matrixA.length;
        double[][] result = new double[n][n];
        AtomicInteger row = new AtomicInteger(0);

        while (row.get() < n) {
            int r = row.getAndIncrement();
            if (r < n) {
                for (int c = 0; c < n; c++) {
                    for (int k = 0; k < n; k++) {
                        result[r][c] += matrixA[r][k] * matrixB[k][c];
                    }
                }
            }
        }

        return result;
    }
}
