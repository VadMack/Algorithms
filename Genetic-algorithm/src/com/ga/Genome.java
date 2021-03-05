package com.ga;

import static com.ga.SupportClass.isPrime;

import java.util.List;
import java.util.stream.Collectors;

public class Genome {
  private int length;
  private double fitness;
  private List<City> cities;
  private List<Integer> sequence;

  public Genome(List<City> cities) {
    length = cities.size();
    this.cities = cities;
    sequence = cities.stream().map(City::getId).collect(Collectors.toList());
    fitness = calculateFitness();
  }

  // TODO
  double calculateFitness() {
    double distance = 0;
    for (int i = 0; i < length - 1; i++) {
      double toAdd = Math.sqrt(Math.pow(findCityById(sequence.get(i)).getX() - findCityById(sequence.get(i + 1)).getX(), 2) +
          Math.pow(findCityById(sequence.get(i)).getY() - findCityById(sequence.get(i + 1)).getY(), 2));
      if (!isPrime(sequence.get(i)) && (i % 10 == 0) && (i != 0)) {
        toAdd *= 1.1;
      }
      distance += toAdd;
    }
    return  distance;
  }

  private City findCityById(int id){
    for (City city : cities) {
      if (city.getId() == id) {
        return city;
      }
    }
    return null;
  }

  public double getFitness() {
    return fitness;
  }
}
