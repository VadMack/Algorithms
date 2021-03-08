package com.ga;

import static com.ga.FileHandler.importFromFile;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*GeneticAlgorithm ga = new GeneticAlgorithm(1, 50, 3);
        List<City> cities = new ArrayList<>();
        City city1 = new City(0, 0, 0);
        City city2 = new City(1, 2, 0);
        City city3 = new City(2, 1, 0);
        City city4 = new City(3, 3, 0);
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        cities.add(city4);
        Genome initialGenome = new Genome(cities);
        List<Genome> population = ga.generatePopulation(initialGenome, 50);
        for (int i = 0; i < initialGenome.getLength(); i++) {
            System.out.print(initialGenome.getSequence()[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < population.get(i).getLength(); j++) {
                System.out.print(population.get(i).getSequence()[j] + " ");
            }
            System.out.print(population.get(i).getFitness());
            System.out.println();
        }
        Genome best = ga.mainFun(population);
        System.out.print(best.getFitness() + ": ");
        for (int i = 0; i < best.getLength(); i++) {
            System.out.print(best.getSequence()[i] + " ");
        }*/
        List<City> cities = importFromFile("cities.csv");
        System.out.println("file parsed");
        Genome initialGenome = new Genome(cities);
        System.out.println("initial created");
        GeneticAlgorithm ga = new GeneticAlgorithm(2, 1, 1);
        List<Genome> population = ga.generatePopulation(initialGenome, 5);
        System.out.println("generation generated");
        System.out.println(cities.size());
        System.out.println(population.get(3).getLength());
        ga.mainFun(population);
        /*
        Genome bestGenome = ga.mainFun(population);
        System.out.println(bestGenome.getFitness());*/
    }
}
