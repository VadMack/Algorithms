package com.tabu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tabu {
    private int numOfVertices;
    private int numOfCars;
    private int carCapacity;
    private double[][] pathLengths;
    private List<Vertex> vertices;

    public Tabu() {
        vertices = new ArrayList<>();
        importFromFile("input.txt");
        pathLengths = fillLengths(vertices);
    }

    public void importFromFile(String path) {
        try {
            Scanner scanner = new Scanner(new File(path));
            numOfVertices = scanner.nextInt() + 1;
            numOfCars = scanner.nextInt();
            carCapacity = scanner.nextInt();

            while (scanner.hasNextLine()) {
                Vertex vertex = new Vertex();
                if (!scanner.hasNextInt()) {
                    return;
                }
                vertex.setId(scanner.nextInt());
                vertex.setY(scanner.nextInt());
                vertex.setX(scanner.nextInt());
                vertex.setDemand(scanner.nextInt());
                vertex.setStartTime(scanner.nextInt());
                vertex.setFinishTime(scanner.nextInt());
                vertex.setServiceTime(scanner.nextInt());
                vertices.add(vertex);
            }
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
        }
    }

    public double[][] fillLengths(List<Vertex> vertices) {
        double[][] lengths = new double[numOfVertices][numOfVertices];
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if (i == j) {
                    lengths[i][j] = Integer.MAX_VALUE;
                    continue;
                }
                lengths[i][j] = Math.sqrt((Math.pow(vertices.get(j).getX() - vertices.get(i).getX(), 2))
                        + Math.pow(vertices.get(j).getY() - vertices.get(i).getY(), 2));
            }
        }
        return lengths;
    }

    public void createVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < numOfCars; i++) {
            vehicles.add(new Vehicle(carCapacity, vertices, pathLengths));
            System.out.println(vehicles.get(i));
            innerExchange(vehicles.get(i));
            System.out.println(vehicles.get(i));
        }


    }

    public void twoOpt(Vehicle vehicle1, Vehicle vehicle2) {

    }

    public void exchangeBetween(Vehicle vehicle1, Vehicle vehicle2) {

    }

    public void innerExchange(Vehicle vehicle) {
        for (int i = 1; i < vehicle.route.size() - 1; i++) {  //scan for the available vertices to exchange in terms of time constraints
            for (int j = i + 1; j < vehicle.route.size() - 1; j++) {
                if (vehicle.route.get(j).getFinishTime() >= vehicle.route.get(i - 1).getServicedTime() +
                        pathLengths[vehicle.route.get(i - 1).getId()][vehicle.route.get(j).getId()] &&
                        vehicle.route.get(i).getFinishTime() >= vehicle.route.get(j - 1).getServicedTime() +
                                pathLengths[vehicle.route.get(j - 1).getId()][vehicle.route.get(i).getId()]) {
                    Vertex buf = vehicle.route.get(j);
                    vehicle.route.set(j, vehicle.route.get(i));
                    vehicle.route.set(i, buf);
                    vehicle.recalculate(i, pathLengths);
                }
            }
        }
    }
}



