package org.example;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> fileNames = Arrays.asList("matrices_d-300", "matrices_d-500", "matrices_d-1000",
                "matrices_d-2000");
//        List<String> fileNames = Arrays.asList("matrices_d-300", "matrices_d-500", "matrices_d-1000",
//                "matrices_d-2000", "matrices_d-3000", "matrices_d-4000", "matrices_d-5000");

        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("Elapsed Time");

            int rowNum = 0;
            for (String fileName : fileNames) {
                String filePath = "C:\\Users\\Саша Головня\\IdeaProjects\\parallel_courseWork\\src\\main\\java\\org\\example\\test_data\\" + fileName;
                FileInputStream fis = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fis);

                List<List<Double>> matrices = (List<List<Double>>) ois.readObject();
                for (List<Double> matrixList : matrices) {
                    int dim = (int) Math.sqrt(matrixList.size());
                    double[][] matrix = new double[dim][dim];
                    int index = 0;
                    for (int i = 0; i < dim; i++) {
                        for (int j = 0; j < dim; j++) {
                            matrix[i][j] = matrixList.get(index++);
                        }
                    }
                    long GaussTime = measureExecutionTime(() -> {
                        double[][] inverse = GaussAlgorithm.inverseMatrix(matrix);
//                        System.out.println("Original Matrix:");
//                        printMatrix(matrix);
//                        System.out.println("\nInverse Matrix:");
//                        printMatrix(inverse);
//                        System.out.println("Коректність алгоритму:" + verifyInverseMatrix(matrix, inverse)+"\n");
                    });

                    long ParallelGaussTime = measureExecutionTime(() -> {
                        double[][] inverse = GaussParallelAlgorithm.inverseMatrix(matrix);
//                        System.out.println("Original Matrix:");
//                        printMatrix(matrix);
//                        System.out.println("\nInverse Matrix:");
//                        printMatrix(inverse);
//                        System.out.println("Коректність алгоритму:" + verifyInverseMatrix(matrix, inverse)+"\n");

//                        RealMatrix inverseMatrix = findInverseMatrix(matrix, dim);
                        // Print the inverse matrix
//                        System.out.println("Inverse matrix for dimension " + dim + ":");
//                        printMatrix(inverseMatrix.getData());
//                        boolean result = compareMatrices(inverseMatrix, inverse);
//                        System.out.println("\nMatrices are equal: " + result);
                    });


                    System.out.println("Matrix d = " + fileName);
                    System.out.println("Time taken to find inverse matrix Gauss: " + GaussTime / 1_000_000 + "ms");
                    System.out.println("Time taken to find inverse matrix Parallel Gauss: " + ParallelGaussTime / 1_000_000 + "ms");
//
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue("Matrix d = " + fileName);
                    row.createCell(1).setCellValue("Time taken to find inverse matrix Parallel Gauss: " + GaussTime / 1_000_000 + "ms");
                    row.createCell(2).setCellValue("Time taken to find inverse matrix Parallel Gauss: " + ParallelGaussTime / 1_000_000 + "ms");
                }
            }

            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Саша Головня\\IdeaProjects\\parallel_courseWork\\src\\main\\java\\org\\example\\output.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static RealMatrix findInverseMatrix(double[][] matrixArray, int dim) {

        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrixArray);
        RealMatrix inverseMatrix = MatrixUtils.inverse(realMatrix);
        return inverseMatrix;
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    private static long measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static boolean compareMatrices(RealMatrix matrix1, double[][] matrix2) {
        double[][] matrix1Array = matrix1.getData();
        if (matrix1Array.length != matrix2.length || matrix1Array[0].length != matrix2[0].length) {
            return false;
        }
        double epsilon = 1e-10;
        for (int i = 0; i < matrix1Array.length; i++) {
            for (int j = 0; j < matrix1Array[0].length; j++) {
                if (Math.abs(matrix1Array[i][j] - matrix2[i][j]) > epsilon) {
                    return false;
                }
            }
        }

        return true;
    }

    public static double determinant(double[][] matrix) {
        int n = matrix.length;
        double det = 1;
        double[][] matrixtemp = matrix;
        // Check if the matrix is square
        if (n != matrixtemp[0].length) {
            throw new IllegalArgumentException("The matrix must be square");
        }

        // Calculate determinant using Kramer's method
        for (int pivotRow = 0; pivotRow < n - 1; pivotRow++) {
            for (int currentRow = pivotRow + 1; currentRow < n; currentRow++) {
                double factor = matrixtemp[currentRow][pivotRow] / matrixtemp[pivotRow][pivotRow];
                for (int j = pivotRow; j < n; j++) {
                    matrixtemp[currentRow][j] -= factor * matrixtemp[pivotRow][j];
                }
            }
        }

        // Multiply the diagonal elements to get the determinant
        for (int i = 0; i < n; i++) {
            det *= matrixtemp[i][i];
        }

        return det;
    }

    public static boolean verifyInverseMatrix(double[][] original, double[][] inverse) {
        // Перевірка відповідності між вихідною та оберненою матрицями
        double[][] product = matrixMultiplication(original, inverse);
        double epsilon = 1e-10; // Точність порівняння для плаваючої точки
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original.length; j++) {
                if (i == j) {
                    if (Math.abs(product[i][j] - 1.0) > epsilon) {
                        System.out.println("Verification failed.");
                        return false;
                    }
                } else {
                    if (Math.abs(product[i][j]) > epsilon) {
                        System.out.println("Verification failed.");
                        return false;
                    }
                }
            }
        }

        System.out.println("Verification successful.");
        return true;
    }

    public static double[][] matrixMultiplication(double[][] matrix1, double[][] matrix2) {
        int m1Rows = matrix1.length;
        int m1Cols = matrix1[0].length;
        int m2Cols = matrix2[0].length;
        double[][] result = new double[m1Rows][m2Cols];
        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }
}

