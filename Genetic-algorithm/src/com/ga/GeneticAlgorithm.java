package com.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    int numOfGenomes;
    int genomeLength;

    public GeneticAlgorithm() {

    }

    public Genome roulette(List<Genome> genomes) {
        Random random = new Random();
        List<Double> wheel = new ArrayList<>();
        wheel.add(1 / genomes.get(0).getFitness());
        for (int i = 1; i < genomes.size(); i++) {
            wheel.add(1 / genomes.get(i).getFitness() + wheel.get(i - 1));
        }
        double randomNumber = random.nextDouble() * wheel.get(wheel.size() - 1);

        for (int i = 0; i < genomes.size(); i++) {
            if (wheel.get(i) >= randomNumber) {
                Genome result = genomes.get(i);
                genomes.remove(i);
                return result;
            }
        }
        return null;
    }

}
