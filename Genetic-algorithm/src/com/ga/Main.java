package com.ga;

import static com.ga.FileHandler.importFromFile;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<City> cities = importFromFile("cities.csv");
        Genome initialGenome = new Genome(cities);
        GeneticAlgorithm ga = new GeneticAlgorithm(30, 50, 1);
        List<Genome> population = ga.generatePopulation(initialGenome, 100);
        Genome bestGenome = ga.mainFun(population);
        System.out.println(bestGenome.getFitness());
        System.out.println(bestGenome.getSequence());
    }
}
