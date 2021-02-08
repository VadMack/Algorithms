package com.vns;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class vnsMain {
    public static int numOfMachines, numOfParts;
    public static ArrayList<Machine> machines;
    public static int[][] table;

    public static void main(String[] args) {
        importFromFile();
        createInitial();
        sortByDecimal();
        System.out.println("d");

    }

    private static void importFromFile() {
        machines = new ArrayList<>();
        try {
            Scanner file = new Scanner(new File("Input.txt"));
            numOfMachines = file.nextInt();
            numOfParts = file.nextInt();
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
        table = new int[numOfMachines][numOfParts];
        for (int i = 0; i < numOfMachines; i++) {
            machines.get(i).setStrNum(i);
            for (int j = 1; j <= numOfParts; j++) {
                if (machines.get(i).getDetails().contains(j)) {
                    table[i][j - 1] = 1;
                } else {
                    table[i][j - 1] = 0;
                }
            }
        }
    }

    static void sortByDecimal() {
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
                if(j == 4){
                    System.out.println();
                }
                if (machines.get(j).getValue() < machines.get(j + 1).getValue()) {
                    int[] temp;
                    int bufNum;
                    temp = Arrays.copyOf(table[j], table[j].length);
                    table[j] = Arrays.copyOf(table[j + 1], table[j + 1].length);
                    table[j + 1] = Arrays.copyOf(temp, temp.length);

                    bufNum = machines.get(j).getStrNum();
                    machines.get(j).setStrNum(machines.get(j + 1).getStrNum());
                    machines.get(j + 1).setStrNum(bufNum);

                    Machine bufMachine = machines.get(j);
                    machines.set(j, machines.get(j + 1));
                    machines.set(j+1, bufMachine);
                }
            }
        }
    }
}
