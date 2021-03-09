package com.ga;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GeneticAlgorithm {
    private final int numOfSurvivors;
    private final int numOfCycles;
    private final int numOfMutations;
    private final int numOfThreads;
    private final int maxCyclesWithoutOptimization;
    private final Random random;
    ExecutorService service;


    public GeneticAlgorithm(int numOfSurvivors, int numOfCycles, int numOfMutations, int numOfThreads, int maxCyclesWithoutOptimization) {
        this.numOfSurvivors = numOfSurvivors;
        this.numOfCycles = numOfCycles;
        this.numOfMutations = numOfMutations;
        this.numOfThreads = numOfThreads;
        this.maxCyclesWithoutOptimization = maxCyclesWithoutOptimization;
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
        int counter = 0;
        for (int i = 0; i < numOfCycles; i++) {
            long time = System.currentTimeMillis();

            List<Genome> bufPopulation = new ArrayList<>();  // Buffer list for new children and survivors
            for (int j = 0; j < numOfSurvivors; j++) {
                bufPopulation.add(roulette(population));  // Selecting survivors by roulette
            }
            population.addAll(bufPopulation);  // Returning survivors to initial population to make crossover possible

            // Selecting parents and adding them to crossover task, then putting task to queue
            List<Callable<Genome>> todoList = new ArrayList<>();
            for (int j = 0; j < populationSize - numOfSurvivors; j++) {
                Genome parent1 = population.get(random.nextInt(populationSize));
                Genome parent2 = population.get(random.nextInt(populationSize));
                Genome parent3 = population.get(random.nextInt(populationSize));
                if (!parent1.equals(parent2) && !parent1.equals(parent3) && !parent2.equals(parent3)) {
                    todoList.add(new Crossover(parent1, parent2, parent3));
                } else {
                    j--;
                }
            }

            // working with crossover queue in multiple threads
            List<Future<Genome>> futureGenome = null;
            service = Executors.newFixedThreadPool(numOfThreads);
            try {
                futureGenome = service.invokeAll(todoList);
                for (Future<Genome> genomeFuture : futureGenome) {
                    bufPopulation.add(genomeFuture.get());
                }
            } catch (InterruptedException | ExecutionException exception) {
                System.out.println("caught" + exception.getMessage());
            }
            service.shutdown();

            population = bufPopulation;  // Resulted population is main now
            for (int j = 0; j < numOfMutations; j++) {
                int rnd = random.nextInt(populationSize);
                population.set(rnd, mutation(population.get(rnd)));  // Applying mutations
            }

            boolean isBestChanged = false;
            for (Genome genome : population) {
                if (genome.getFitness() < bestGenome.getFitness()) {  // Search for a best genome
                    bestGenome = genome;
                    isBestChanged = true;
                }
            }
            System.out.println("BEST " + bestGenome.getFitness() + " : ");

            System.out.println("\n/////////\nGENERATION done by " + ((double) System.currentTimeMillis() - time) + "\n");

            writer.println("Generation " + i + " BEST : " + bestGenome.getFitness());
            for (int j = 0; j < bestGenome.getLength(); j++) {
                writer.print(bestGenome.getSequence()[j] + " ");
            }
            writer.println();
            writer.println("///////////////////////////////////////////////");
            writer.println("///////////////////////////////////////////////");

            if (!isBestChanged) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter >= maxCyclesWithoutOptimization) {
                break;
            }
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

    public Genome mutation(Genome genome) {
        Integer[] sequence = Arrays.copyOf(genome.getSequence(), genome.getLength());
        int bufIndex1 = random.nextInt((sequence.length - 2) - 1) + 1;
        int bufIndex2 = random.nextInt((sequence.length - 2) - 1) + 1;
        int buf = sequence[bufIndex1];
        sequence[bufIndex1] = sequence[bufIndex2];
        sequence[bufIndex2] = buf;
        genome.setSequence(sequence);
        genome.setFitness(genome.calculateFitness());
        return genome;
    }

    public List<Genome> generatePopulation(Genome init, int size) {
        List<Callable<Genome>> todoList = new ArrayList<>();
        List<Future<Genome>> futurePopulation;
        List<Genome> population = new ArrayList<>();
        service = Executors.newFixedThreadPool(numOfThreads);
        for (int i = 0; i < size; i++) {
            todoList.add(new GenomeGenerator(init));
        }
        try {
            futurePopulation = service.invokeAll(todoList);
            for (int i = 0; i < size; i++) {
                population.add(futurePopulation.get(i).get());
            }
        } catch (InterruptedException | ExecutionException exception) {
            System.out.println("caught" + exception.getMessage());
        }
        service.shutdown();
        return population;
    }

}

// Implementing Callable for crossover to be able to execute it in multiple threads
class Crossover implements Callable<Genome> {
    Genome parent1, parent2, parent3;
    Random random;

    public Crossover(Genome parent1, Genome parent2, Genome parent3) {
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.parent3 = parent3;
        this.random = new Random();
    }

    private Genome crossover() {
        Genome child = new Genome(parent1);
        Integer[] sequence = child.getSequence();
        long time = System.currentTimeMillis();
        int crossoverPoint1 = random.nextInt((parent1.getLength() - 2) - 1) + 1;
        int crossoverPoint2 = random.nextInt(parent1.getLength() - crossoverPoint1) + crossoverPoint1;
        for (int i = 1; i < crossoverPoint1; i++) {
            int bufIndex = 0;
            for (int j = 1; j < sequence.length - 1; j++) {
                if (sequence[j].equals(parent2.getSequence()[i])) {
                    bufIndex = j;
                    break;
                }
            }
            int buf = sequence[bufIndex];
            sequence[bufIndex] = sequence[i];
            sequence[i] = buf;
        }
        for (int i = crossoverPoint1; i < crossoverPoint2; i++) {
            int bufIndex = 0;
            for (int j = 1; j < sequence.length - 1; j++) {
                if (sequence[j].equals(parent3.getSequence()[i])) {
                    bufIndex = j;
                    break;
                }
            }
            int buf = sequence[bufIndex];
            sequence[bufIndex] = sequence[i];
            sequence[i] = buf;
        }
        sequence[sequence.length - 1] = sequence[0];
        child.setSequence(sequence);
        System.out.println("Crossover done by " + ((double) System.currentTimeMillis() - time));
        child.setFitness(child.calculateFitness());
        return child;
    }

    public Genome call() {
        return crossover();
    }
}

// Implementing Callable for generator to be able to execute it in multiple threads
class GenomeGenerator implements Callable<Genome> {
    private final Genome init;

    public GenomeGenerator(Genome init) {
        this.init = init;
    }

    public Genome generatePopulation() {
        Genome newGenome = new Genome(init);
        Collections.shuffle(Arrays.asList(newGenome.getSequence()).subList(1, newGenome.getSequence().length));
        Integer[] newSequence = Arrays.copyOf(newGenome.getSequence(), newGenome.getLength() + 1);
        newSequence[newSequence.length - 1] = newSequence[0];
        newGenome.setSequence(newSequence);
        newGenome.setLength(newGenome.getLength() + 1);
        newGenome.setFitness(newGenome.calculateFitness());
        System.out.println("genome generated");
        return newGenome;
    }

    public Genome call() {
        return generatePopulation();
    }
}