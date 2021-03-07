package com.ga;

import java.util.ArrayList;
import java.util.Collections;
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

    public Genome crossover(Genome parent1, Genome parent2, Genome parent3){
        Genome child = new Genome(parent1);
        List<Integer> sequence = new ArrayList<>(child.getSequence());
        Random random = new Random();
        int crossoverPoint1 = random.nextInt(parent1.getLength());
        int crossoverPoint2 = random.nextInt(parent1.getLength() - crossoverPoint1) + crossoverPoint1;
        System.out.println(crossoverPoint1 + "+" + crossoverPoint2);
        for (int i = 0; i < crossoverPoint1; i++) {
            Collections.swap(sequence, sequence.indexOf(parent2.getSequence().get(i)), i);
        }
        for (int i = crossoverPoint1; i < crossoverPoint2; i++) {
            Collections.swap(sequence, sequence.indexOf(parent3.getSequence().get(i)), i);
        }
        sequence.set(sequence.size()-1, sequence.get(0));
        child.setSequence(sequence);
        return child;
    }

}
