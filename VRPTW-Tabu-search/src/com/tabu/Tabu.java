package com.tabu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;

public class Tabu {

  private int numOfVertices;
  private int numOfCars;
  private int carCapacity;
  private final double[][] pathLengths;
  private List<Vertex> vertices;
  private List<TabuElem> tabuElems;
  private List<Vehicle> vehicles;
  private List<Vehicle> bestSolution;
  private List<List<Vehicle>> solutionNeighbourhood;
  private double bestTime;

  public Tabu() {
    vertices = new ArrayList<>();
    tabuElems = new ArrayList<>();
    bestSolution = new ArrayList<>();
    bestTime = Double.MAX_VALUE;
    importFromFile("input.txt");
    pathLengths = fillLengths(vertices);
    createVehicles();
  }


  public void algorithm() {
    List<Vehicle> best
    solutionNeighbourhood = new ArrayList<List<Vehicle>>();
    for (int i = 0; i < 100; i++) {
      Random random = new Random();
      int randId = random.nextInt(numOfCars);
      Vehicle vehicle = new Vehicle(vehicles.get(randId));
      while (vehicle.getRoute().isEmpty()) {
        randId = random.nextInt(numOfCars);
        vehicle = new Vehicle(vehicles.get(randId));
      }
      System.out.println(vehicle);
      innerExchange(vehicle);
      System.out.println(vehicle);
      solutionNeighbourhood.add(List.copyOf(vehicles));
      solutionNeighbourhood.get(solutionNeighbourhood.size() - 1).set(randId, vehicle);
    }
    for (List<Vehicle> potentialSolution:
    solutionNeighbourhood) {

    }
    double currentTime = 0;
    for (Vehicle oneVehicle :
        solutionNeighbourhood.get(solutionNeighbourhood.size() - 1)) {
      currentTime += oneVehicle.getTime();
    }
      Iterator<TabuElem> iterator = tabuElems.iterator();
      while (iterator.hasNext()) {
        TabuElem tabuElem = iterator.next();
        tabuElem.decrementTime();
        if (tabuElem.getTime() <= 0) {
          iterator.remove();
        }
      }
      System.out.println(tabuElems);

      double currentTime = 0;
      for (Vehicle oneVehicle :
          vehicles) {
        currentTime += oneVehicle.getTime();
      }

      if (currentTime > bestTime) {

      }


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
    vehicles = new ArrayList<>();
    for (int i = 0; i < numOfCars; i++) {
      vehicles.add(new Vehicle(carCapacity, vertices, pathLengths));
    }
  }

  public void exchangeBetween(Vehicle vehicle1, Vehicle vehicle2) {
    for (int i = 1; i < vehicle1.getRoute().size() - 1; i++) {
      for (int j = 1; j < vehicle2.getRoute().size() - 1; j++) {
        if (checkNeighboursTimeLimits(vehicle1, i, vehicle2, j) &&
            vehicle1.getCapacity() + vehicle1.getRoute().get(i).getDemand() -
                vehicle2.getRoute().get(j).getDemand() >= 0 &&
            vehicle2.getCapacity() + vehicle2.getRoute().get(j).getDemand() -
                vehicle1.getRoute().get(i).getDemand() >= 0 &&
            !isInTabu(vehicle1.getRoute().get(i).getId(), vehicle2.getRoute().get(j).getId())) {
          swap(vehicle1, i, vehicle2, j);
          vehicle1.recalculate(i, pathLengths, true);
          vehicle2.recalculate(j, pathLengths, true);
          if (vehicle1.getTime() > vertices.get(0).getFinishTime() ||
              vehicle2.getTime() > vertices.get(0).getFinishTime()) {
            swap(vehicle1, i, vehicle2, j);
            vehicle1.recalculate(i, pathLengths, true);
            vehicle2.recalculate(j, pathLengths, true);
          } else {
            /*tabuElems.add(new TabuElem(vehicle1.getRoute().get(i).getId(),
                vehicle2.getRoute().get(j).getId()));*/
            break;
          }
        }
      }
    }
  }

  public void innerExchange(Vehicle vehicle) {
    List<Pair> poolOfSolutions = new ArrayList<>();

    for (int i = 1; i < vehicle.getRoute().size() - 1; i++) {
      for (int j = i + 1; j < vehicle.getRoute().size() - 1; j++) {
        if (checkNeighboursTimeLimits(vehicle, i, vehicle, j) &&
            !isInTabu(vehicle.getRoute().get(i).getId(), vehicle.getRoute().get(j).getId())) {
          swap(vehicle, i, vehicle, j);
          vehicle.recalculate(i, pathLengths, false);
          // acceptable time
          if (vehicle.getTime() <= vertices.get(0).getFinishTime()) {
            //save to pool here
            poolOfSolutions.add(new Pair(i, j));
          }
          swap(vehicle, i, vehicle, j);
          vehicle.recalculate(i, pathLengths, false);
            /*tabuElems.add(
                new TabuElem(vehicle.getRoute().get(i).getId(), vehicle.getRoute().get(j).getId()));*/
            //break;
        }
      }
    }
    Random random = new Random();
    Pair pair = poolOfSolutions.get(random.nextInt(poolOfSolutions.size()));
    swap(vehicle, pair.getId1(), vehicle, pair.getId2());
  }

  public void swap(Vehicle vehicle1, int toSwap1, Vehicle vehicle2, int toSwap2) {
    Vertex bufVertex = vehicle1.getRoute().get(toSwap1);
    vehicle1.getRoute().set(toSwap1, vehicle2.getRoute().get(toSwap2));
    vehicle2.getRoute().set(toSwap2, bufVertex);
  }

  public boolean isInTabu(int id1, int id2) {
    for (TabuElem tabuElem :
        tabuElems) {
      if (((tabuElem.getId1() == id1) &&
          (tabuElem.getId2() == id2)) ||
          ((tabuElem.getId1() == id2) &&
              (tabuElem.getId2() == id1))) {
        return true;
      }
    }
    return false;
  }

  private boolean checkNeighboursTimeLimits(Vehicle vehicle1, int i, Vehicle vehicle2, int j) {
    return (vehicle2.getRoute().get(j).getFinishTime()
        >= vehicle1.getRoute().get(i - 1).getServicedTime() +
        pathLengths[vehicle1.getRoute().get(i - 1).getId()][vehicle2.getRoute().get(j).getId()] &&
        vehicle1.getRoute().get(i).getFinishTime()
            >= vehicle2.getRoute().get(j - 1).getServicedTime() +
            pathLengths[vehicle2.getRoute().get(j - 1).getId()][vehicle1.getRoute().get(i)
                .getId()]);
  }

}


class TabuElem {

  private int id1;
  private int id2;
  private int time;

  public TabuElem(int id1, int id2) {
    this.id1 = id1;
    this.id2 = id2;
    this.time = 5;
  }

  public void decrementTime() {
    this.time--;
  }

  public int getId1() {
    return id1;
  }

  public void setId1(int id1) {
    this.id1 = id1;
  }

  public int getId2() {
    return id2;
  }

  public void setId2(int id2) {
    this.id2 = id2;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "TabuElem{" +
        "id1=" + id1 +
        ", id2=" + id2 +
        ", time=" + time +
        '}';
  }
}



