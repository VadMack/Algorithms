package com.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    int numOfSurvivors;
    int numOfCycles;
    int numOfMutations;
    Random random;

    public GeneticAlgorithm(int numOfSurvivors, int numOfCycles, int numOfMutations) {
        this.numOfSurvivors = numOfSurvivors;
        this.numOfCycles = numOfCycles;
        this.numOfMutations = numOfMutations;
        random = new Random();
    }

    public Genome mainFun(List<Genome> population) {
        int populationSize = population.size();
        Genome bestGenome = population.get(0);
        for (int i = 0; i < numOfCycles; i++) {
            List<Genome> bufPopulation = new ArrayList<>();
            for (int j = 0; j < numOfSurvivors; j++) {
                bufPopulation.add(roulette(population));
            }
            population.addAll(bufPopulation);
            while (bufPopulation.size() < populationSize) {
                Genome parent1 = population.get(random.nextInt(populationSize));
                Genome parent2 = population.get(random.nextInt(populationSize));
                Genome parent3 = population.get(random.nextInt(populationSize));
                if (!parent1.equals(parent2) && !parent1.equals(parent3) && !parent2.equals(parent3)) {
                    bufPopulation.add(crossover(parent1, parent2, parent3));
                }
            }
            population = bufPopulation;
            for (int j = 0; j < numOfMutations; j++) {
                int rnd = random.nextInt(populationSize);
                population.set(rnd, mutation(population.get(rnd)));
            }
            for (Genome genome : population) {
                if(genome.getFitness()<bestGenome.getFitness()){
                    bestGenome = genome;
                }
            }

        }
        return bestGenome;
    }

    public Genome roulette(List<Genome> genomes) {
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

    public Genome crossover(Genome parent1, Genome parent2, Genome parent3) {
        Genome child = new Genome(parent1);
        List<Integer> sequence = new ArrayList<>(child.getSequence());
        int crossoverPoint1 = random.nextInt(parent1.getLength());
        int crossoverPoint2 = random.nextInt(parent1.getLength() - crossoverPoint1) + crossoverPoint1;
        for (int i = 0; i < crossoverPoint1; i++) {
            Collections.swap(sequence, sequence.indexOf(parent2.getSequence().get(i)), i);
        }
        for (int i = crossoverPoint1; i < crossoverPoint2; i++) {
            Collections.swap(sequence, sequence.indexOf(parent3.getSequence().get(i)), i);
        }
        sequence.set(sequence.size() - 1, sequence.get(0));
        child.setSequence(sequence);
        child.setFitness(child.calculateFitness());
        return child;
    }

    public Genome mutation(Genome genome) {
        List<Integer> sequence = new ArrayList<>(genome.getSequence());
        int first, last;
        first = sequence.get(0);
        last = sequence.get(sequence.size() - 1);
        Collections.swap(sequence, random.nextInt(sequence.size()),
                random.nextInt(sequence.size()));
        if (!sequence.get(0).equals(first)) {
            sequence.set(sequence.size() - 1, sequence.get(0));
        } else if (!sequence.get(sequence.size() - 1).equals(last)) {
            sequence.set(0, sequence.get(sequence.size() - 1));
        }
        genome.setSequence(sequence);
        genome.setFitness(genome.calculateFitness());
        return genome;
    }

    public List<Genome> generatePopulation(Genome init, int size) {
        List<Genome> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Genome newGenome = new Genome(init);
            List<Integer> newSequence = new ArrayList<>(init.getSequence());
            Collections.shuffle(newSequence);
            newSequence.add(newSequence.get(0));
            newGenome.setSequence(newSequence);
            newGenome.setFitness(newGenome.calculateFitness());
            population.add(newGenome);
        }
        return population;
    }

}
