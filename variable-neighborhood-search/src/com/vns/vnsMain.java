package com.vns;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class vnsMain {
    public static int numOfMachines, numOfDetails;
    public static ArrayList<Machine> machines;
    public static ArrayList<Cluster> clusters = new ArrayList<>();
    public static int[][] table;
    public static int[] detailsPositions;
    public static int[] detailsValues;
    public static String output;
    public static double bestEfficiency = 0;

    public static void main(String[] args) {
        importFromFile();
        createInitial();
        sortMachines();
        sortDetails();
        generateCluster();
        calculateEfficiency();
        System.out.println("A");
        shaking();
        System.out.println(output);
        System.out.println(bestEfficiency);

    }

    private static void importFromFile() {
        machines = new ArrayList<>();
        try {
            Scanner file = new Scanner(new File("Input.txt"));
            numOfMachines = file.nextInt();
            numOfDetails = file.nextInt();
            Scanner line = new Scanner(file.nextLine());
            for (int i = 0; i < numOfMachines; ++i) {
                line = new Scanner(file.nextLine());
                machines.add(new Machine(line.nextInt()));
                for (int j = 0; line.hasNextInt(); j++) {
                    machines.get(i).getDetails().add(line.nextInt());
                }
            }
            file.close();
            line.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
        }
    }

    static void createInitial() {
        table = new int[numOfMachines][numOfDetails];
        detailsPositions = new int[numOfDetails];
        detailsValues = new int[numOfDetails];

        for (int i = 0; i < numOfMachines; i++) {
            for (int j = 1; j <= numOfDetails; j++) {
                if (machines.get(i).getDetails().contains(j)) {
                    table[i][j - 1] = 1;
                } else {
                    table[i][j - 1] = 0;
                }
            }
        }

        for (int i = 0; i < numOfDetails; i++) {
            detailsPositions[i] = i + 1;
        }
    }

    static void sortMachines() {
        for (int i = 0; i < table.length; i++) {
            int value = 0;
            int index = 0;
            for (int j = table[i].length - 1; j >= 0; j--) {
                value += table[i][j] * Math.pow(2, index++);
            }
            machines.get(i).setValue(value);
        }
        for (int i = 0; i < table.length - 1; i++) {
            for (int j = 0; j < table.length - i - 1; j++) {
                if (machines.get(j).getValue() < machines.get(j + 1).getValue()) {
                    int[] temp;
                    temp = Arrays.copyOf(table[j], table[j].length);
                    table[j] = Arrays.copyOf(table[j + 1], table[j + 1].length);
                    table[j + 1] = Arrays.copyOf(temp, temp.length);

                    Machine bufMachine = machines.get(j);
                    machines.set(j, machines.get(j + 1));
                    machines.set(j + 1, bufMachine);
                }
            }
        }
    }

    static void sortDetails() {
        for (int i = 0; i < numOfDetails; i++) {
            int value = 0;
            int index = 0;
            for (int j = numOfMachines - 1; j >= 0; j--) {
                value += table[j][i] * Math.pow(2, index++);
            }
            detailsValues[i] = value;
        }

        for (int i = 0; i < numOfDetails - 1; i++) {
            for (int j = 0; j < numOfDetails - i - 1; j++) {
                if (detailsValues[j] < detailsValues[j + 1]) {
                    int[] temp = new int[numOfMachines];

                    // swap of columns
                    for (int k = 0; k < numOfMachines; k++) {
                        temp[k] = table[k][j];
                    }
                    for (int k = 0; k < numOfMachines; k++) {
                        table[k][j] = table[k][j + 1];
                    }
                    for (int k = 0; k < numOfMachines; k++) {
                        table[k][j + 1] = temp[k];
                    }

                    // swap of values
                    int tempValue = detailsValues[j];
                    detailsValues[j] = detailsValues[j + 1];
                    detailsValues[j + 1] = tempValue;

                    // swap of positions
                    int tempPosition = detailsPositions[j];
                    detailsPositions[j] = detailsPositions[j + 1];
                    detailsPositions[j + 1] = tempPosition;
                }
            }
        }
    }

    static void generateCluster() {
        int x = 0;
        int y = 0;
        while (x < numOfDetails && y < numOfMachines) {
            int newX = x + (int) (Math.random() * (numOfDetails - x));
            int newY = y + (int) (Math.random() * (numOfMachines - y));
            clusters.add(new Cluster(x, y, newX, newY));
            x = newX + 1;
            y = newY + 1;
        }
    }

    static boolean calculateEfficiency() {
        double numOfOnes = 0;
        double numOfOnesInClusters = 0;
        double numOfZerosInClusters = 0;
        for (int i = 0; i < numOfMachines; i++) {
            for (int j = 0; j < numOfDetails; j++) {
                if (table[i][j] == 1) {
                    numOfOnes++;
                }
            }
        }
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = clusters.get(i).getUx(); j <= clusters.get(i).getDx(); j++) {
                for (int k = clusters.get(i).getUy(); k <= clusters.get(i).getDy(); k++) {
                    if (table[k][j] == 1) {
                        numOfOnesInClusters++;
                    } else {
                        numOfZerosInClusters++;
                    }
                }
            }

        }
        double efficiency = numOfOnesInClusters / (numOfOnes + numOfZerosInClusters);
        if (efficiency > bestEfficiency) {
            bestEfficiency = efficiency;
            generateOutput();
            return true;
        }
        return false;

    }

    static void generateOutput() {
        output = "";

        // generate string of output for machines
        int[] clustersToMachines = new int[numOfMachines + 1];
        for (int i = 0; i < numOfMachines; i++) {
            for (int j = 0; j < clusters.size(); j++) {
                if (clusters.get(j).getUy() <= i && clusters.get(j).getDy() >= i) {
                    clustersToMachines[machines.get(i).getId()] = j + 1;
                    break;
                }
            }
        }
        for (int i = 1; i < numOfMachines + 1; i++) {
            output += clustersToMachines[i] + " ";
        }
        output += "\n";

        // generate string of output for details

        int[] clustersToDetails = new int[numOfDetails + 1];
        for (int i = 0; i < numOfDetails; i++) {
            for (int j = 0; j < clusters.size(); j++) {
                if (clusters.get(j).getUx() <= i && clusters.get(j).getDx() >= i) {
                    clustersToDetails[detailsPositions[i]] = j + 1;
                }
            }
        }
        for (int i = 1; i < numOfDetails + 1; i++) {
            output += clustersToDetails[i] + " ";
        }
    }

    static void shaking(){
        ArrayList<Cluster> bufList = new ArrayList<>(clusters);

        for (int i = 0; i < clusters.size() - 1; i++) {
            bufList.get(i).setDx(clusters.get(i+1).getDx());
            bufList.get(i).setUx(clusters.get(i+1).getUx());
            bufList.remove(i+1);

            ArrayList<Cluster> bufbuf = new ArrayList<>(clusters);
            clusters.clear();
            clusters.addAll(bufList);
            bufList.clear();
            bufList.addAll(bufbuf);

            if(!calculateEfficiency()){
                bufList.clear();
                bufList.addAll(clusters);
                clusters.clear();
                clusters.addAll(bufbuf);
            }
        }
    }
}
