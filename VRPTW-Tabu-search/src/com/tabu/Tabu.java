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

            vertices.get(0).setUsed(true);
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
        }
        System.out.println(vehicles.get(0));
        System.out.println(vehicles.get(1));
        System.out.println("exchange:");
        exchangeBetween(vehicles.get(0), vehicles.get(1));
        System.out.println(vehicles.get(0));
        System.out.println(vehicles.get(1));
    }

    public void twoOpt(Vehicle vehicle1, Vehicle vehicle2) {

    }

    public void exchangeBetween(Vehicle vehicle1, Vehicle vehicle2) {
        for (int i = 1; i < vehicle1.route.size() - 1; i++) {
            for (int j = 1; j < vehicle2.route.size() - 1; j++) {
                if (vehicle2.route.get(j).getFinishTime() >= vehicle1.route.get(i - 1).getServicedTime() +
                        pathLengths[vehicle1.route.get(i - 1).getId()][vehicle2.route.get(j).getId()] &&
                        vehicle1.route.get(i).getFinishTime() >= vehicle2.route.get(j - 1).getServicedTime() +
                                pathLengths[vehicle2.route.get(j - 1).getId()][vehicle1.route.get(i).getId()] &&
                        vehicle1.getCapacity() + vehicle1.route.get(i).getDemand() -
                                vehicle2.route.get(j).getDemand() >= 0 &&
                        vehicle2.getCapacity() + vehicle2.route.get(j).getDemand() -
                                vehicle1.route.get(i).getDemand() >= 0) {
                    swap(vehicle1, i, vehicle2, j);
                    vehicle1.recalculate(i, pathLengths, true);
                    vehicle2.recalculate(j, pathLengths, true);
                    if (vehicle1.getTime() > vertices.get(0).getFinishTime() ||
                            vehicle2.getTime() > vertices.get(0).getFinishTime()) {
                        swap(vehicle1, i, vehicle2, j);
                        vehicle1.recalculate(i, pathLengths, true);
                        vehicle2.recalculate(j, pathLengths, true);
                    }
                }
            }
        }
    }

    public void innerExchange(Vehicle vehicle) {
        for (int i = 1; i < vehicle.route.size() - 1; i++) {
            for (int j = i + 1; j < vehicle.route.size() - 1; j++) {
                if (vehicle.route.get(j).getFinishTime() >= vehicle.route.get(i - 1).getServicedTime() +
                        pathLengths[vehicle.route.get(i - 1).getId()][vehicle.route.get(j).getId()] &&
                        vehicle.route.get(i).getFinishTime() >= vehicle.route.get(j - 1).getServicedTime() +
                                pathLengths[vehicle.route.get(j - 1).getId()][vehicle.route.get(i).getId()]) {
                    swap(vehicle, i, vehicle, j);
                    vehicle.recalculate(i, pathLengths, false);
                    if (vehicle.getTime() > vertices.get(0).getFinishTime()) {
                        swap(vehicle, i, vehicle, j);
                        vehicle.recalculate(i, pathLengths, false);
                    }
                }
            }
        }
    }

    public void swap(Vehicle vehicle1, int toSwap1, Vehicle vehicle2, int toSwap2) {
        Vertex bufVertex = vehicle1.route.get(toSwap1);
        vehicle1.route.set(toSwap1, vehicle2.route.get(toSwap2));
        vehicle2.route.set(toSwap2, bufVertex);
    }

}



