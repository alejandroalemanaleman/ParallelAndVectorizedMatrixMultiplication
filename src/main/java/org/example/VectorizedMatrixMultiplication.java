package org.example;


import com.aparapi.Kernel;
import com.aparapi.Range;


public class VectorizedMatrixMultiplication {
    public static double[][] execute(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int rowsB = matrixB.length;
        int colsB = matrixB[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Not same number of columns in A and B matrices.");
        }

        double[] A = new double[rowsA * colsA];
        double[] B = new double[rowsB * colsB];
        double[] C = new double[rowsA * colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsA; j++) {
                A[i * colsA + j] = matrixA[i][j];
            }
        }

        for (int i = 0; i < rowsB; i++) {
            for (int j = 0; j < colsB; j++) {
                B[i * colsB + j] = matrixB[i][j];
            }
        }

        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int gid = getGlobalId();
                int row = gid / colsB;
                int col = gid % colsB;

                double sum = 0.0;
                for (int k = 0; k < colsA; k++) {
                    sum += A[row * colsA + k] * B[k * colsB + col];
                }
                C[row * colsB + col] = sum;
            }
        };

        kernel.execute(Range.create(rowsA * colsB));
        kernel.dispose();

        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                result[i][j] = C[i * colsB + j];
            }
        }

        return result;
    }
}