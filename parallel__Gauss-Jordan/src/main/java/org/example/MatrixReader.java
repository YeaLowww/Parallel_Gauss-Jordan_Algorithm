package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class MatrixReader {
    private static final String MATRICES_DIR = "C:\\Users\\Саша Головня\\IdeaProjects\\parallel_courseWork\\src\\main\\java\\org\\example\\test_data";

    public static void main(String[] args) {
        int[][] creationData = {
                //{5}, {25}, {50},
                {5},//{150},{250},
                //{300},{500},{1000},{1500},{2000}, {3000}, {4000},
                //


        };

        for (int[] params : creationData) {
            int dim = params[0];

            try (FileInputStream fis = new FileInputStream(MATRICES_DIR + "/matrices_d-" + dim );
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                List<List<Double>> matrices = (List<List<Double>>) ois.readObject();
                System.out.println("Matrices for dim " + dim + ":");
                for (List<Double> matrix : matrices) {
                    for (Double val : matrix) {
                        System.out.print(val + " ");

                    }
                    System.out.println();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
