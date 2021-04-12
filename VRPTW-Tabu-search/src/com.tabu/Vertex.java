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

  public Vertex() {
  }

  public Vertex(int id, int x, int y, int demand, int startTime, int finishTime, int serviceTime) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.demand = demand;
    this.startTime = startTime;
    this.finishTime = finishTime;
    this.serviceTime = serviceTime;
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
        && serviceTime == vertex.serviceTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, x, y, demand, startTime, finishTime, serviceTime);
  }

  @Override
  public String toString() {
    return "Vertex{" +
        "id=" + id +
        ", x=" + x +
        ", y=" + y +
        ", demand=" + demand +
        ", startTime=" + startTime +
        ", finishTime=" + finishTime +
        ", serviceTime=" + serviceTime +
        '}';
  }
}
