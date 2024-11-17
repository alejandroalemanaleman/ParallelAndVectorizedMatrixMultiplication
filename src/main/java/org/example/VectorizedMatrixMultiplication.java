package org.example;

public class VectorizedMatrixMultiplication {

    public static void main(String[] args) {
        // Define two matrices
        int[][] matrixA = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        int[][] matrixB = {
            {9, 8, 7},
            {6, 5, 4},
            {3, 2, 1}
        };

        // Check if multiplication is possible
        if (matrixA[0].length != matrixB.length) {
            System.out.println("Matrix multiplication not possible with given matrices.");
            return;
        }

        // Resultant matrix of the appropriate size
        int[][] result = new int[matrixA.length][matrixB[0].length];

        // Perform vectorized matrix multiplication
        System.out.println("Starting vectorized matrix multiplication...");
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                result[i][j] = vectorizedDotProduct(matrixA[i], getColumn(matrixB, j));
                System.out.printf("Result[%d][%d] = %d\n", i, j, result[i][j]);
            }
        }

        // Display the result matrix
        System.out.println("\nResult Matrix:");
        printMatrix(result);
    }

    /**
     * Helper method to compute the dot product of two vectors
     * This method performs a "vectorized-like" operation on two arrays (vectors)
     *
     * @param row the row vector from matrix A
     * @param column the column vector from matrix B
     * @return the dot product of row and column vectors
     */
    private static int vectorizedDotProduct(int[] row, int[] column) {
        int sum = 0;
        System.out.print("Dot product of " + arrayToString(row) + " and " + arrayToString(column) + " = ");
        
        for (int k = 0; k < row.length; k++) {
            sum += row[k] * column[k];
            System.out.print("(" + row[k] + "*" + column[k] + ") ");
            if (k < row.length - 1) System.out.print("+ ");
        }
        
        System.out.println("= " + sum);
        return sum;
    }

    /**
     * Helper method to extract a column from a matrix as a vector
     *
     * @param matrix the matrix to extract the column from
     * @param colIndex the index of the column to extract
     * @return the column as a vector
     */
    private static int[] getColumn(int[][] matrix, int colIndex) {
        int[] column = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][colIndex];
        }
        return column;
    }

    /**
     * Helper method to print a matrix
     *
     * @param matrix the matrix to print
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Helper method to convert an array to a string for printing
     *
     * @param array the array to convert to a string
     * @return the string representation of the array
     */
    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

