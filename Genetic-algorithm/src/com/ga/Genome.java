package com.ga;

import static com.ga.SupportClass.isPrime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  public Genome(Genome genome){
    this.length = genome.getLength();
    this.fitness = genome.getFitness();
    this.cities = new ArrayList<>(genome.getCities());
    this.sequence = new ArrayList<>(genome.getSequence());
  }

  double calculateFitness() {
    long time = System.currentTimeMillis();
    double distance = 0;
    for (int i = 0; i < length - 1; i++) {
      double toAdd = Math.sqrt(Math.pow(cities.get(sequence.get(i)).getX() - cities.get(sequence.get(i + 1)).getX(), 2) +
          Math.pow(cities.get(sequence.get(i)).getY() - cities.get(sequence.get(i + 1)).getY(), 2));
      if (!isPrime(sequence.get(i)) && (i % 10 == 0) && (i != 0)) {
        toAdd *= 1.1;
      }
      distance += toAdd;
    }
    System.out.println((double) System.currentTimeMillis() - time);
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

  public List<Integer> getSequence() {
    return sequence;
  }

  public void setSequence(List<Integer> sequence) {
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
        ", sequence=" + sequence +
        '}';
  }
}


