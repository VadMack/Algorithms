package com.ga;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeneticAlgorithm {
    private final int numOfSurvivors;
    private final int numOfCycles;
    private final int numOfMutations;
    private final Random random;
    private final ExecutorService exec = Executors.newFixedThreadPool(10);
    private CompletionService<Integer> service = new ExecutorCompletionService<>(exec);


    public GeneticAlgorithm(int numOfSurvivors, int numOfCycles, int numOfMutations) {
        this.numOfSurvivors = numOfSurvivors;
        this.numOfCycles = numOfCycles;
        this.numOfMutations = numOfMutations;
        random = new Random();
    }

    public Genome mainFun(List<Genome> population) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("output.txt");
        } catch (IOException io) {
            System.out.println("fileError");
        }

        int populationSize = population.size();
        Genome bestGenome = population.get(0);
        for (int i = 0; i < numOfCycles; i++) {
            long time = System.currentTimeMillis();

            List<Genome> bufPopulation = new ArrayList<>();  // Buffer list for new children and survivors
            for (int j = 0; j < numOfSurvivors; j++) {
                bufPopulation.add(roulette(population));  // Selecting survivors by roulette
            }
            population.addAll(bufPopulation);  // Returning survivors to initial population to make crossover possible
            System.out.println("Roulette DONE");
            while (bufPopulation.size() < populationSize) {  // Crossover
                Genome parent1 = population.get(random.nextInt(populationSize));
                Genome parent2 = population.get(random.nextInt(populationSize));
                Genome parent3 = population.get(random.nextInt(populationSize));
                if (!parent1.equals(parent2) && !parent1.equals(parent3) && !parent2.equals(parent3)) {
                System.out.println("Parents selected");
                bufPopulation.add(crossover(parent1, parent2, parent3));
                }
            }
            population = bufPopulation;  // Resulted population is main now
            System.out.println(population.get(3).getLength());
            System.out.println("Crossover DONE");
            for (int j = 0; j < numOfMutations; j++) {
                int rnd = random.nextInt(populationSize);
                population.set(rnd, mutation(population.get(rnd)));  // Applying mutations
            }
            System.out.println("Mutations DONE");
            for (Genome genome : population) {
                if (genome.getFitness() < bestGenome.getFitness()) {  // Search for a best genome
                    bestGenome = genome;
                }
            }
            System.out.println("BEST " + bestGenome.getFitness() + " : ");

            System.out.println("GENERATION done by " + ((double) System.currentTimeMillis() - time));

            writer.println("Generation " + i + " BEST : " + bestGenome.getFitness());
            for (int j = 0; j < bestGenome.getLength(); j++) {
                writer.print(bestGenome.getSequence()[j] + " ");
            }
            writer.println();
            writer.println("///////////////////////////////////////////////");
            writer.println("///////////////////////////////////////////////");
        }

        writer.println("FINAL BEST : " + bestGenome.getFitness());
        for (int j = 0; j < bestGenome.getLength(); j++) {
            writer.print(bestGenome.getSequence()[j] + " ");
        }
        writer.println();
        writer.close();

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
        Integer[] sequence = child.getSequence();
        long time = System.currentTimeMillis();
        int crossoverPoint1 = random.nextInt(parent1.getLength());
        int crossoverPoint2 = random.nextInt(parent1.getLength() - crossoverPoint1) + crossoverPoint1;
        System.out.println("Crossover points selected :" + crossoverPoint1 + ", " + crossoverPoint2);
        for (int i = 0; i < crossoverPoint1; i++) {
            int bufIndex = 0;
            for (int j = 0; j < sequence.length; j++) {
                if (sequence[j].equals(parent2.getSequence()[i])) {
                    bufIndex = j;
                    break;
                }
            }
            int buf = sequence[bufIndex];
            sequence[bufIndex] = sequence[i];
            sequence[i] = buf;
        }
        System.out.println("Crossover 1 DONE");
        for (int i = crossoverPoint1; i < crossoverPoint2; i++) {
            int bufIndex = 0;
            for (int j = 0; j < sequence.length; j++) {
                if (sequence[j] == parent3.getSequence()[i]) {
                    bufIndex = j;
                    break;
                }
            }
            int buf = sequence[bufIndex];
            sequence[bufIndex] = sequence[i];
            sequence[i] = buf;
        }
        System.out.println("Crossover 2 DONE");
        sequence[sequence.length - 1] = sequence[0];
        child.setSequence(sequence);
        System.out.println("Crossover done by " + ((double) System.currentTimeMillis() - time));
        child.setFitness(child.calculateFitness());
        return child;
    }

    public Genome mutation(Genome genome) {
        Integer[] sequence = Arrays.copyOf(genome.getSequence(), genome.getLength());
        int first = sequence[0];
        int last = sequence[sequence.length - 1];
        int bufIndex1 = random.nextInt(sequence.length);
        int bufIndex2 = random.nextInt(sequence.length);
        int buf = sequence[bufIndex1];
        sequence[bufIndex1] = sequence[bufIndex2];
        sequence[bufIndex2] = buf;
        if (sequence[0] != first) {
            sequence[sequence.length - 1] = sequence[0];
        } else if (sequence[sequence.length - 1] != last) {
            sequence[0] = sequence[sequence.length - 1];
        }
        genome.setSequence(sequence);
        genome.setFitness(genome.calculateFitness());
        return genome;
    }

    public List<Genome> generatePopulation(Genome init, int size) {
        List<Genome> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            System.out.println("generating " + i + "/" + size);
            Genome newGenome = new Genome(init);
            Collections.shuffle(Arrays.asList(newGenome.getSequence()));
            Integer[] newSequence = Arrays.copyOf(newGenome.getSequence(), newGenome.getLength() + 1);
            newSequence[newSequence.length - 1] = newSequence[0];
            newGenome.setSequence(newSequence);
            newGenome.setLength(newGenome.getLength() + 1);
            //System.out.println("FITNESS CALCULATING");
            newGenome.setFitness(newGenome.calculateFitness());
            //System.out.println("FITNESS CALCULATED");
            population.add(newGenome);
        }
        return population;
    }

}
