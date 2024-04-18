package org.example;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixGenerator {
    private static final String MATRICES_DIR = "C:\\Users\\Саша Головня\\IdeaProjects\\parallel_courseWork\\src\\main\\java\\org\\example\\test_data";
    public static RealMatrix findInverseMatrix(List<List<Double>> matrixList, int dim) {
        double[][] matrixArray = new double[dim][dim];
        int index = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                matrixArray[i][j] = matrixList.get(0).get(index++);
            }
        }

        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrixArray);
        RealMatrix inverseMatrix = MatrixUtils.inverse(realMatrix);
        return inverseMatrix;
    }
    public static List<List<Double>> generateRegularMatrices(int dim) {
        List<List<Double>> matrices = new ArrayList<>();
        Random random = new Random();

        double[][] matrix = new double[dim][dim];
        for (int j = 0; j < dim; j++) {
            for (int k = 0; k < dim; k++) {
                matrix[j][k] = random.nextDouble(1)+2;
            }
        }

        List<Double> matrixList = new ArrayList<>();
        for (double[] row : matrix) {
            for (double val : row) {
                matrixList.add(val);
            }
        }
        matrices.add(matrixList);

        return matrices;
    }

    public static void main(String[] args) {
        int[][] creationData = {
                {5},
        };

        for (int[] params : creationData) {
            int dim = params[0];

            List<List<Double>> matrices = generateRegularMatrices(dim);
            List<List<Double>> matricestemp = matrices;
            //////////////////////////////////////////////////////////////////////
            RealMatrix inverseMatrix = findInverseMatrix(matricestemp, dim);
            // Print the inverse matrix
            System.out.println("Inverse matrix for dimension " + dim + ":");
            for (List<Double> matrix : matrices) {
                for (Double val : matrix) {
                    System.out.print(val + " ");

                }
                System.out.println();
            }
//            for (int i = 0; i < dim; i++) {
//                for (int j = 0; j < dim; j++) {
//                    System.out.print(inverseMatrix.getEntry(i, j) + " ");
//                }
//                System.out.println();
//            }
            ///////////////////////////////////////////////////////////////////////////
            try (FileOutputStream fos = new FileOutputStream(MATRICES_DIR + "/matrices_d-" + dim);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(matrices);
                System.out.println("Generated matrix with dim " + dim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

