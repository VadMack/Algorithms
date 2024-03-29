package com.tabu;

import java.util.Objects;

public class Vertex {
    private int id;
    private int x;
    private int y;
    private int demand;
    private int startTime;
    private int finishTime;
    private int serviceTime;
    private double servicedTime;
    private boolean isUsed;

    public Vertex() {
    }

    public Vertex(int id, int x, int y, int demand, int startTime, int finishTime, int serviceTime,
                  boolean isUsed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.serviceTime = serviceTime;
        this.isUsed = isUsed;
    }

    public Vertex(Vertex clone){
        this.id = clone.id;
        this.x = clone.x;
        this.y = clone.y;
        this.demand = clone.demand;
        this.startTime = clone.startTime;
        this.finishTime = clone.finishTime;
        this.serviceTime = clone.serviceTime;
        this.isUsed = clone.isUsed;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public double getServicedTime() {
        return servicedTime;
    }

    public void setServicedTime(double servicedTime) {
        this.servicedTime = servicedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return id == vertex.id && x == vertex.x && y == vertex.y && demand == vertex.demand
                && startTime == vertex.startTime && finishTime == vertex.finishTime
                && serviceTime == vertex.serviceTime && isUsed == vertex.isUsed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, demand, startTime, finishTime, serviceTime, isUsed);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", servicedTime=" + servicedTime +
                '}';
    }
}
