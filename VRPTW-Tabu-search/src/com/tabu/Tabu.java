package com.tabu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tabu {
  private static int numOfVertices;
  private static int numOfCars;
  private static int carCapacity;

  public List<Vertex> importFromFile(String path) {
    List<Vertex> vertices = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(new File(path));
      numOfVertices = scanner.nextInt();
      numOfCars = scanner.nextInt();
      carCapacity = scanner.nextInt();

      while (scanner.hasNextLine()) {
        Vertex vertex = new Vertex();
        if (!scanner.hasNextInt()){
          return vertices;
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
    return vertices;
  }
}
