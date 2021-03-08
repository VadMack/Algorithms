package com.ga;

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
        int populationSize = population.size();
        Genome bestGenome = population.get(0);
        for (int i = 0; i < numOfCycles; i++) {
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
                    bufPopulation.add(crossover(parent1, parent2, parent3));
                }
            }
            population = bufPopulation;  // Resulted population is main now
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
            System.out.println("BEST " + bestGenome.getFitness());
            System.out.println(bestGenome.getSequence());

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
        Integer[] sequence = new Integer[child.getSequence().size()];
        long time = System.currentTimeMillis();
        for (int i = 0; i < child.getSequence().size(); i++) {
            sequence[i] = child.getSequence().get(i);
        }
        int crossoverPoint1 = random.nextInt(parent1.getLength());
        int crossoverPoint2 = random.nextInt(parent1.getLength() - crossoverPoint1) + crossoverPoint1;
        for (int i = 0; i < crossoverPoint1; i++) {
            int bufIndex = 0;
            for (int j = 0; j < sequence.length; j++) {
                if(sequence[j] == parent2.getSequence().get(i)){
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
            for (int j = 0; j < sequence.length; j++) {
                if(sequence[j] == parent3.getSequence().get(i)){
                    bufIndex = j;
                    break;
                }
            }
            int buf = sequence[bufIndex];
            sequence[bufIndex] = sequence[i];
            sequence[i] = buf;
        }
        sequence[sequence.length-1] = sequence[0];
        child.setSequence(Arrays.asList(sequence));
        System.out.println((double) System.currentTimeMillis() - time);
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
            System.out.println("generating " + i + "/" + size);
            Genome newGenome = new Genome(init);
            List<Integer> newSequence = new ArrayList<>(init.getSequence());
            Collections.shuffle(newSequence);
            newSequence.add(newSequence.get(0));
            newGenome.setSequence(newSequence);
            System.out.println("FITNESS CALCULATING");
            newGenome.setFitness(newGenome.calculateFitness());
            System.out.println("FITNESS CALCULATED");
            population.add(newGenome);
        }
        return population;
    }

}
