package com.ga;

import static com.ga.FileHandler.importFromFile;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<City> cities = importFromFile("cities.csv");
        System.out.println("file parsed");
        Genome initialGenome = new Genome(cities);
        System.out.println("initial created");
        GeneticAlgorithm ga = new GeneticAlgorithm(60, 15, 6);
        List<Genome> population = ga.generatePopulation(initialGenome, 130);
        System.out.println("generation generated");
        System.out.println(cities.size());
        System.out.println(population.get(3).getLength());
        Genome bestGenome = ga.mainFun(population);
        System.out.println(bestGenome.getFitness());
    }
}
