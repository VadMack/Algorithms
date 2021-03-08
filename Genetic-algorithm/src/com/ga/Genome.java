package com.ga;

import static com.ga.SupportClass.isPrime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Genome {
  private int length;
  private double fitness;
  private List<City> cities;
  Integer[] sequence;

  public Genome(List<City> cities) {
    length = cities.size();
    this.cities = cities;
    sequence = cities.stream().map(City::getId).toArray(Integer[]::new);
    fitness = calculateFitness();
  }

  public Genome(Genome genome){
    this.length = genome.getLength();
    this.fitness = genome.getFitness();
    this.cities = new ArrayList<>(genome.getCities());
    this.sequence = Arrays.copyOf(genome.getSequence(), genome.getLength());
  }

  double calculateFitness() {
    double distance = 0;
    for (int i = 0; i < length - 1; i++) {
      double toAdd = Math.sqrt(Math.pow(cities.get(sequence[i]).getX() - cities.get(sequence[i + 1]).getX(), 2) +
          Math.pow(cities.get(sequence[i]).getY() - cities.get(sequence[i + 1]).getY(), 2));
      if (!isPrime(sequence[i]) && (i % 10 == 0) && (i != 0)) {
        toAdd *= 1.1;
      }
      distance += toAdd;
    }
    return  distance;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public void setFitness(double fitness) {
    this.fitness = fitness;
  }

  public List<City> getCities() {
    return cities;
  }

  public void setCities(List<City> cities) {
    this.cities = cities;
  }

  public Integer[] getSequence() {
    return sequence;
  }

  public void setSequence(Integer[] sequence) {
    this.sequence = sequence;
  }

  public double getFitness() {
    return fitness;
  }

  @Override
  public boolean equals(Object object){
    return object instanceof Genome && this.sequence.equals(((Genome) object).sequence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(length, fitness, cities, sequence);
  }

  @Override
  public String toString() {
    return "Genome{" +
        "length=" + length +
        ", fitness=" + fitness +
        ", cities=" + cities +
        ", sequence=" + Arrays.toString(sequence) +
        '}';
  }
}


