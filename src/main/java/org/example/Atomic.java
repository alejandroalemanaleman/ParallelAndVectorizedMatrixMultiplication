package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class Atomic {

    public double[][] execute (double[][] matrixA, double[][] matrixB) {
        int n = matrixA.length; // Tamaño de la matriz (n*n)
        double[][] result = new double[n][n];
        AtomicInteger row = new AtomicInteger(0); // Controla las filas procesadas

        // Procesar todas las filas
        while (row.get() < n) {
            int r = row.getAndIncrement(); // Obtener y actualizar la fila actual
            if (r < n) { // Asegurar que la fila está dentro de los límites
                for (int c = 0; c < n; c++) { // Iterar sobre columnas
                    for (int k = 0; k < n; k++) { // Realizar la multiplicación y acumulación
                        result[r][c] += matrixA[r][k] * matrixB[k][c];
                    }
                }
            }
        }

        return result; // Devolver la matriz resultante
    }
}
