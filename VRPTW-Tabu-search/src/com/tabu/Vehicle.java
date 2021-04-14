package com.tabu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Vehicle {

    private int capacity;
    private double time;
    List<Vertex> route;

    public Vehicle(int capacity, List<Vertex> vertices, double[][] pathLengths) {
        this.capacity = capacity;
        this.time = 0;
        createInitial(vertices, pathLengths);
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

    public void recalculate(int start, double[][] pathLengths) {
        for (int i = start; i < route.size(); i++) {
            route.get(i).setServicedTime(route.get(i - 1).getServicedTime() +
                    pathLengths[route.get(i - 1).getId()][route.get(i).getId()] +
                    Math.max(route.get(i).getStartTime() - route.get(i - 1).getServicedTime() -
                            pathLengths[route.get(i - 1).getId()][route.get(i).getId()], 0) +
                    route.get(i).getServiceTime());
        }
        time = route.get(route.size() - 1).getServicedTime();
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
