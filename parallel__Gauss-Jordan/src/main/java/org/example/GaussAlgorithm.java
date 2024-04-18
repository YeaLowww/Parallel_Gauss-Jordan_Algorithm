package org.example;

import java.util.Arrays;

public class GaussAlgorithm {

    public static double[][] inverseMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] augmentedMatrix = augmentMatrix(matrix);
        // Forward Elimination
        for (int pivotRow = 0; pivotRow < n; pivotRow++) {
            for (int currentRow = pivotRow + 1; currentRow < n; currentRow++) {
                double factor = augmentedMatrix[currentRow][pivotRow] / augmentedMatrix[pivotRow][pivotRow];
                for (int j = pivotRow; j < 2 * n; j++) {
                    augmentedMatrix[currentRow][j] -= augmentedMatrix[pivotRow][j] * factor;
                }
            }
        }

        // Backward Substitution
        for (int pivotRow = n - 1; pivotRow >= 0; pivotRow--) {
            for (int currentRow = pivotRow - 1; currentRow >= 0; currentRow--) {
                double factor = augmentedMatrix[currentRow][pivotRow] / augmentedMatrix[pivotRow][pivotRow];
                for (int j = pivotRow; j < 2 * n; j++) {
                    augmentedMatrix[currentRow][j] -= augmentedMatrix[pivotRow][j] * factor;
                }
            }
        }

        // Normalize each row
        for (int i = 0; i < n; i++) {
            double divisor = augmentedMatrix[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] /= divisor;
            }
        }

        // Extract the inverse matrix
        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmentedMatrix[i], n, inverse[i], 0, n);
        }

        return inverse;
    }

    //Хоча копіювання може займати додатковий час, воно допомагає зберегти структуру програми більш зрозумілою та підтримуваною. У цьому випадку час копіювання є прийнятним, оскільки він відбувається лише один раз на початку алгоритму, а не у внутрішньому циклі, який виконується множину разів.
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
        if (determinant(matrix) == 0) {
            System.out.println("Determinant == 0");
        } else {
            System.out.println("Determinant = " + determinant(matrix));
            double[][] inverse = inverseMatrix(matrix);
            System.out.println("Original Matrix:");
            printMatrix(matrix);
            System.out.println("\nInverse Matrix:");
            printMatrix(inverse);

        }
    }

    public static double determinant(double[][] matrix) {
        int n = matrix.length;
        double[][] temp = new double[n][n];
        double det = 1;

        // Copying the input matrix to a temporary matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, temp[i], 0, n);
        }

        for (int pivotRow = 0; pivotRow < n; pivotRow++) {
            // If the pivot element is zero, swap with a non-zero element below it
            if (temp[pivotRow][pivotRow] == 0) {
                for (int i = pivotRow + 1; i < n; i++) {
                    if (temp[i][pivotRow] != 0) {
                        // Swap rows pivotRow and i
                        double[] tempRow = temp[pivotRow];
                        temp[pivotRow] = temp[i];
                        temp[i] = tempRow;

                        // Change sign of determinant because of row swap
                        det = -det;
                        break;
                    }
                }
            }

            // If the pivot element is still zero, the determinant is zero
            if (temp[pivotRow][pivotRow] == 0) {
                return 0;
            }

            // Perform row operations to make elements below the pivot element zero
            for (int currentRow = pivotRow + 1; currentRow < n; currentRow++) {
                double factor = temp[currentRow][pivotRow] / temp[pivotRow][pivotRow];
                for (int j = pivotRow; j < n; j++) {
                    temp[currentRow][j] -= factor * temp[pivotRow][j];
                }
            }
        }

        // Multiply the diagonal elements to get the determinant
        for (int i = 0; i < n; i++) {
            det *= temp[i][i];
        }

        return det;
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
