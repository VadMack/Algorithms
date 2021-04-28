package com.tabu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Vehicle {

    private final int startingCapacity;
    private int capacity;
    private double time;
    private List<Vertex> route;
    private double penalty;

    public Vehicle(int capacity, List<Vertex> vertices, double[][] pathLengths) {
        this.startingCapacity = capacity;
        this.capacity = capacity;
        this.time = 0;
        createInitial(vertices, pathLengths);
        this.penalty = 0;
    }

    public Vehicle(Vehicle clone) {
        this.startingCapacity = clone.startingCapacity;
        this.capacity = clone.capacity;
        this.time = clone.time;
        this.route = clone.route;
    }

    private void createInitial(List<Vertex> vertices, double[][] pathLengths) {
        double minLength;
        route = new ArrayList<>();
        route.add(0, vertices.get(0));
        route.get(0).setServicedTime(0);
        route.get(0).setUsed(true);
        int i = 0;
        while (capacity > 0) {
            minLength = Double.MAX_VALUE;
            Vertex nearestVertex = null;
            for (int j = 0; j < vertices.size(); j++) {
                if ((pathLengths[i][j] + Math.max(vertices.get(j).getStartTime() -
                        vertices.get(i).getServicedTime() - pathLengths[i][j], 0)) < minLength &&
                        !vertices.get(j).isUsed() &&
                        capacity >= vertices.get(j).getDemand() &&
                        (this.time + pathLengths[i][j]) <= vertices.get(j).getFinishTime()
                ) {
                    minLength = pathLengths[i][j] + Math.max(vertices.get(j).getStartTime() -
                            vertices.get(i).getServicedTime() - pathLengths[i][j], 0);
                    nearestVertex = vertices.get(j);
                }
            }

            if (nearestVertex == null) {
                // System.out.println("Nearest vertex not found");
                break;
            } else {
                nearestVertex.setServicedTime(vertices.get(i).getServicedTime() +
                        pathLengths[i][nearestVertex.getId()] +
                        Math.max(vertices.get(nearestVertex.getId()).getStartTime() -
                                vertices.get(i).getServicedTime() - pathLengths[i][nearestVertex.getId()], 0) +
                        nearestVertex.getServiceTime());
                i = nearestVertex.getId();
                nearestVertex.setUsed(true);
                route.add(nearestVertex);
                time = nearestVertex.getServicedTime();
                capacity -= nearestVertex.getDemand();
            }
        }
        while (route.size() > 0 && (time + pathLengths[route.get(route.size() - 1).getId()][0]) > vertices.get(0)
                .getFinishTime()) {
            capacity += route.get(route.size() - 1).getDemand();
            route.get(route.size() - 1).setUsed(false);
            route.remove(route.size() - 1);
            if (route.size() > 0) {
                time = route.get(route.size() - 1).getServicedTime();
            }

        }
        if (route.size() > 0) {
            time += pathLengths[route.get(route.size() - 1).getId()][0];
        }
        route.add(vertices.get(0));
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void recalculate(int start, double[][] pathLengths, boolean isCapacityChanged) {
        for (int i = start; i < route.size() - 1; i++) {
            route.get(i).setServicedTime(route.get(i - 1).getServicedTime() +
                    pathLengths[route.get(i - 1).getId()][route.get(i).getId()] +
                    Math.max(route.get(i).getStartTime() - route.get(i - 1).getServicedTime() -
                            pathLengths[route.get(i - 1).getId()][route.get(i).getId()], 0) +
                    route.get(i).getServiceTime());
        }
        if (isCapacityChanged) {
            capacity = startingCapacity;
            for (Vertex vertex : route) {
                capacity -= vertex.getDemand();
            }
        }
        time = route.get(route.size() - 2).getServicedTime() + pathLengths[route.get(route.size() - 2).getId()][0];
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getStartingCapacity() {
        return startingCapacity;
    }

    public List<Vertex> getRoute() {
        return route;
    }

    public void setRoute(List<Vertex> route) {
        this.route = route;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
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
        return startingCapacity == vehicle.startingCapacity && capacity == vehicle.capacity
            && Double.compare(vehicle.time, time) == 0
            && Double.compare(vehicle.penalty, penalty) == 0 && Objects
            .equals(route, vehicle.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingCapacity, capacity, time, route, penalty);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
            ", time=" + time +
            ", route=" + route +
            ", penalty=" + penalty +
            '}';
    }
}
