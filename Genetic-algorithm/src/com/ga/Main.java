package com.ga;

import static com.ga.FileHandler.importFromFile;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*List<City> cities = importFromFile("cities.csv");
        System.out.println(cities);*/

        List<City> cities = new ArrayList<>();
        City city1 = new City(1, 2, 2);
        City city2 = new City(2, 2, 4);
        City city3 = new City(3, 2, 6);
        City city4 = new City(4, 2, 8);
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        cities.add(city4);
        cities.add(city1);
        Genome genome = new Genome(cities);
        System.out.println(genome.getFitness());
        List<City> cities2 = new ArrayList<>();
        cities2.add(city1);
        cities2.add(city4);
        cities2.add(city2);
        cities2.add(city3);
        cities2.add(city1);
        Genome genome2 = new Genome(cities2);
        System.out.println(genome2.getFitness());
        List<Genome> genomes = new ArrayList<>();
        genomes.add(genome);
        genomes.add(genome2);
        GeneticAlgorithm ga = new GeneticAlgorithm();
        System.out.println("Roulette said: " + ga.roulette(genomes).getFitness());
    }
}
