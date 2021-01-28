package com.ils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class IlsMain {
    public static ArrayList<City> cities;
    public static int numberOfCities;
    public static ArrayList<City> shortestRoute2opt;
    public static double shortestDistance2opt;
    public static ArrayList<City> shortestRoute = new ArrayList<City>();
    public static double shortestDistance = Double.MAX_VALUE;


    public static void main(String[] args) {
        importFromFile();
        int inspections = 100000;
        double startTime = System.currentTimeMillis();

        System.out.println("Started");
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            TwoOpt(inspections);
        }
        double stopTime = System.currentTimeMillis();


        try (FileWriter writer = new FileWriter("Output.txt", true))
        {
            writer.write("The best route: \n");
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numberOfCities; i++) {
                result.append(shortestRoute.get(i).getId()).append(" ");
            }
            writer.write(result.toString());
            writer.append('\n');
            String distance = "";
            distance += "Distance travelled: " + String.valueOf(shortestDistance);
            writer.write(distance);
            // запись по символам
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

        //System.out.println("Two Opt shortest tour:");

        // старый вывод
        /*for (int i = 0; i < numberOfCities; i++) {
            System.out.print(shortestRoute2opt.get(i).getId() + " ");
        }
        System.out.println();
        System.out.println("Distance travelled: " + shortestDistance2opt);*/

        // новый вывод
        /*for (int i = 0; i < numberOfCities; i++) {
            System.out.print(shortestRoute.get(i).getId() + " ");
        }
        System.out.println();
        System.out.println("Distance travelled: " + shortestDistance);*/

        System.out.println("Two Opt execution time (in seconds): " + ((stopTime - startTime) / 1000.000000) + "\n");



    }

    private static void importFromFile() {
        cities = new ArrayList<>();
        try {    /* Input file path here */
            Scanner file = new Scanner(new File("Input.txt"));
            numberOfCities = file.nextInt();
            Scanner line = new Scanner(file.nextLine());
            for (int i = 0; i < numberOfCities; ++i) {
                line = new Scanner(file.nextLine());
                cities.add(new City(line.nextInt(), line.nextInt(), line.nextInt()));
            }
            file.close();
            line.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
        }
    }

    public static double calculateDistance(ArrayList<City> route) {
        double distance = 0;
        for (int i = 0; i < numberOfCities - 1; i++) {
            distance += Math.sqrt(Math.pow(route.get(i + 1).getX() - route.get(i).getX(), 2) +
                    Math.pow(route.get(i + 1).getY() - route.get(i).getY(), 2));
        }
        distance += Math.sqrt(Math.pow(route.get(numberOfCities - 1).getX() - route.get(0).getX(), 2) +
                Math.pow(route.get(numberOfCities - 1).getY() - route.get(0).getY(), 2));
        return distance;
    }

    /* Permute the input */
    static ArrayList<City> TwoOpt(ArrayList<City> start) {
        ArrayList<City> route = new ArrayList<>(start);

        int city1 = ThreadLocalRandom.current().nextInt(1, numberOfCities);
        int city2 = ThreadLocalRandom.current().nextInt(1, numberOfCities);

        if (city1 > city2) {
            int temp = city2;
            city2 = city1;
            city1 = temp;
        }

        ArrayList<City> middle = new ArrayList<City>();
        for (int i = city2; i >= city1; i--) {
            middle.add(route.get(i));
        }

        int counter = 0;
        for (int i = city1; i <= city2; i++) {
            route.set(i, middle.get(counter));
            counter++;
        }
        return route;
    }

    /* Improve the default route with the 2opt algorithm */
    public static void TwoOpt(int inspections) {
        ArrayList<City> route = new ArrayList<>(cities);
        Collections.shuffle(route);
        shortestDistance2opt = calculateDistance(route);
        shortestRoute2opt = new ArrayList<>(route);
        for (int j = 0; j < inspections; j++) {
            route = TwoOpt(shortestRoute2opt);
            double lengthRoute = calculateDistance(route);

            /*for (int i = 0; i < numberOfCities; i++) {
                System.out.print(route.get(i).getId() + " ");
            }
            System.out.println(" --- " + lengthRoute);*/

            if (lengthRoute < shortestDistance2opt) {
                shortestDistance2opt = lengthRoute;
                //System.out.print(shortestDistance2opt + " --- ");
                shortestRoute2opt = new ArrayList<>(route);
                /*for (int i = 0; i < numberOfCities; i++) {
                    System.out.print(shortestRoute2opt.get(i).getId() + " ");
                }
                System.out.println();*/

                if(shortestDistance2opt < shortestDistance){
                    shortestDistance = shortestDistance2opt;
                    shortestRoute = new ArrayList<>(shortestRoute2opt);
                }
            }
        }

        try (FileWriter writer = new FileWriter("Output.txt", true))
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numberOfCities; i++) {
                result.append(shortestRoute2opt.get(i).getId()).append(" ");
            }
            writer.write(result.toString() + "\n");
            String distance = "";
            distance += "Distance travelled: " + String.valueOf(shortestDistance2opt);
            writer.write(distance + "\n\n");
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}

