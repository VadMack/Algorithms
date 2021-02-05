package com.vns;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class vnsMain {
    public static int numOfMachines, numOfParts;
    public static ArrayList<Machine> machines;

    public static void main(String[] args) {
        importFromFile();
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
}
