package com.tabu;

public class Move {
  private int i;
  private int j;
  private Vehicle vehicle1;
  private Vehicle vehicle2;
  private double newTime;

  public Move(Vehicle vehicle1, int i, Vehicle vehicle2, int j, double newTime) {
    this.i = i;
    this.j = j;
    this.vehicle1 = vehicle1;
    this.vehicle2 = vehicle2;
    this.newTime = newTime;
  }

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public int getJ() {
    return j;
  }

  public void setJ(int j) {
    this.j = j;
  }

  public Vehicle getVehicle1() {
    return vehicle1;
  }

  public void setVehicle1(Vehicle vehicle1) {
    this.vehicle1 = vehicle1;
  }

  public Vehicle getVehicle2() {
    return vehicle2;
  }

  public void setVehicle2(Vehicle vehicle2) {
    this.vehicle2 = vehicle2;
  }

  public double getNewTime() {
    return newTime;
  }

  public void setNewTime(double newTime) {
    this.newTime = newTime;
  }

  /*public boolean isTheSame(int id1, int id2) {
    return (this.id1 == id1 && this.id2 == id2) ||
        (this.id1 == id2 && this.id2 == id1);
  }*/

  @Override
  public String toString() {
    return "Move{" +
            "i=" + i +
            ", j=" + j +
            ", vehicle1=" + vehicle1 +
            ", vehicle2=" + vehicle2 +
            ", newTime=" + newTime +
            '}';
  }
}
