package com.tabu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Vehicle {

  private int capacity;
  private int time;
  List<Vertex> route;

  public Vehicle(int capacity, List<Vertex> route) {
    this.capacity = capacity;
    this.time = 0;
    this.route = route;
  }

  private void createInitial(List<Vertex> vertices, double[][] pathLengths) {
    route = new ArrayList<>();
    route.add(0, vertices.get(0));
    while (capacity > 0) {
      double minLength = Double.MAX_VALUE;
      int i = 0;
      Vertex nearestVertex = null;
      for (int j = 0; j < vertices.size(); j++) {
        if (pathLengths[i][j] < minLength &&
            !vertices.get(j).isUsed() &&
            capacity >= vertices.get(j).getDemand()
        ) {
          minLength = pathLengths[i][j];
          nearestVertex = vertices.get(j);
        }
      }

      if (nearestVertex == null) {
        System.out.println("Nearest vertex not found");
      }

      route.add(nearestVertex);
      nearestVertex.setUsed(true);
      capacity -= nearestVertex.getDemand();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vehicle vehicle = (Vehicle) o;
    return capacity == vehicle.capacity && time == vehicle.time && Objects
        .equals(route, vehicle.route);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capacity, time, route);
  }

  private String route() {
    if (this.route == null) {
      return "error";
    }
    return this.route.stream().map(Vertex::getId).map(Object::toString)
        .collect(Collectors.joining(","));
  }

  @Override
  public String toString() {
    return "Vehicle{" +
        "capacity=" + capacity +
        ", time=" + time +
        ", route=" + route() +
        '}';
  }
}
