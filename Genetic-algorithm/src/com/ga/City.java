package com.ga;

import java.util.Objects;

public class City {
  private int id;
  private double x;
  private double y;

  public City(int id, double x, double y) {
    this.id = id;
    this.x = x;
    this.y = y;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City city = (City) o;
    return id == city.id && Double.compare(city.x, x) == 0
        && Double.compare(city.y, y) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, x, y);
  }

  @Override
  public String toString() {
    return "City{" +
        "id=" + id +
        ", x=" + x +
        ", y=" + y +
        '}';
  }
}
