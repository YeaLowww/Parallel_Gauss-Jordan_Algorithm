package org.example;
import java.util.Arrays;
import java.util.*;

import java.util.concurrent.*;
import java.util.concurrent.Executors;

public class GaussParallelAlgorithm {

    public static double[][] inverseMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = augmentMatrix(matrix);
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        //System.out.println("Кількість процесорних ядер у системі: " + numberOfThreads);

//        ExecutorService executor = Executors.newFixedThreadPool(n); // Використовуєм кількість потоків, що відповідає кількості рядків
        List<Future<?>> futures = new ArrayList<>();

        // Forward Elimination
        for (int pivotRow = 0; pivotRow < n; pivotRow++) {
            final int finalPivotRow = pivotRow; // Фінальна копія pivotRow
            for (int currentRow = pivotRow + 1; currentRow < n; currentRow++) {
                final int curRow = currentRow;
                final double factor = augmentedMatrix[currentRow][finalPivotRow] / augmentedMatrix[finalPivotRow][finalPivotRow];
                futures.add(executor.submit(() -> {
                    for (int j = finalPivotRow; j < 2 * n; j++) {
                        augmentedMatrix[curRow][j] -= augmentedMatrix[finalPivotRow][j] * factor;
                    }
                }));
            }
            waitForFutures(futures);
        }

        futures.clear();

        // Backward Substitution
        for (int pivotRow = n - 1; pivotRow >= 0; pivotRow--) {
            final int finalPivotRow = pivotRow; // Фінальна копія pivotRow
            for (int currentRow = pivotRow - 1; currentRow >= 0; currentRow--) {
                final int curRow = currentRow;
                final double factor = augmentedMatrix[currentRow][finalPivotRow] / augmentedMatrix[finalPivotRow][finalPivotRow];
                futures.add(executor.submit(() -> {
                    for (int j = finalPivotRow; j < 2 * n; j++) {
                        augmentedMatrix[curRow][j] -= augmentedMatrix[finalPivotRow][j] * factor;
                    }
                }));
            }
            waitForFutures(futures);
        }

        futures.clear();

        // Normalize each row
        for (int i = 0; i < n; i++) {
            final int row = i;
            final double divisor = augmentedMatrix[i][i];
            futures.add(executor.submit(() -> {
                for (int j = 0; j < 2 * n; j++) {
                    augmentedMatrix[row][j] /= divisor;
                }
            }));
            waitForFutures(futures);
        }

        executor.shutdown();
        // Extract the inverse matrix
        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmentedMatrix[i], n, inverse[i], 0, n);
        }

        return inverse;
    }

    private static void waitForFutures(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        futures.clear();
    }



    private static double[][] augmentMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][n + i] = 1;
        }
        return augmentedMatrix;
    }


    public static void main(String[] args) {
        double[][] matrix = {
                {1, 2, 3},
                {0, 1, 4},
                {5, 6, 0}
        };


        double[][] inverse = inverseMatrix(matrix);
        System.out.println("Original Matrix:");
        printMatrix(matrix);
        System.out.println("\nInverse Matrix:");
        printMatrix(inverse);
    }
    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
