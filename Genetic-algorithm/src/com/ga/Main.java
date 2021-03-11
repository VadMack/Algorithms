package com.ga;

import static com.ga.FileHandler.importFromFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<City> cities = importFromFile("cities.csv");
        System.out.println("file parsed");
        GeneticAlgorithm ga = new GeneticAlgorithm(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            Genome initialGenome = new Genome(cities);
            System.out.println("initial created");
            List<Genome> population = ga.generatePopulation(initialGenome, Integer.parseInt(args[0]));
            System.out.println("generation generated");
            Genome bestGenome = ga.mainFun(population);
            System.out.println(bestGenome.getFitness());
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(bestGenome.getFitness() + ".csv");
            } catch (IOException io) {
                System.out.println("fileError");
            }
            writer.println("Path");
            for (int j = 0; j < bestGenome.getLength(); j++) {
                writer.println(bestGenome.getSequence()[i]);
            }
            writer.close();
        }
    }
}
