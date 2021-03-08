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
        GeneticAlgorithm ga = new GeneticAlgorithm(15, 15, 3);
        List<Genome> population = ga.generatePopulation(initialGenome, 25);
        System.out.println("generation generated");
        Genome bestGenome = ga.mainFun(population);
        System.out.println(bestGenome.getFitness());
        System.out.println(bestGenome.getSequence());
    }
}
