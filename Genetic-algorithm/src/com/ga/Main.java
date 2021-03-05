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
        City city2 = new City(2,2,4);
        cities.add(city1);
        cities.add(city2);
        cities.add(city1);
        Genome genome = new Genome(cities);
        System.out.println(genome.getFitness());

    }
}
