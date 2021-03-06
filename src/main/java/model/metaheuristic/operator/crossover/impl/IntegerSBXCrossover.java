/*
 * Code taken from https://github.com/jMetal/jMetal
 *
 * Copyright <2017> <Antonio J. Nebro, Juan J. Durillo>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to
 * whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. © 2019
 * GitHub, Inc.
 */
package model.metaheuristic.operator.crossover.impl;

import annotations.NumberInput;
import annotations.operator.DefaultConstructor;
import model.metaheuristic.operator.crossover.CrossoverOperator;
import model.metaheuristic.solution.impl.IntegerSolution;
import model.metaheuristic.util.random.JavaRandom;
import model.metaheuristic.util.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Applies the SBXCrossover on a IntegerSolution
 */
public class IntegerSBXCrossover implements CrossoverOperator<IntegerSolution> {
    /*minimum difference allowed between real values */
    private static final double EPS = 1.0e-14;

    private final double crossoverProbability;
    private final double distributionIndex;
    private final RandomGenerator<Double> random;

    /**
     * Constructor
     *
     * @param crossoverProbability the crossover probability
     * @param distributionIndex    the distribution index
     */
    @DefaultConstructor(numbers = {@NumberInput(displayName = "CrossoverProbability", defaultValue = 0.9)
            , @NumberInput(displayName = "DistributionIndex", defaultValue = 20)})
    public IntegerSBXCrossover(double crossoverProbability, double distributionIndex) {
        this(crossoverProbability, distributionIndex, () -> JavaRandom.getInstance().nextDouble());

    }

    /**
     * Constructor
     *
     * @param crossoverProbability the crossover probability
     * @param distributionIndex    the distribution index
     * @param randomGenerator      a random generator
     * @throws IllegalArgumentException if crossover probability or distributionIndex is negative
     */
    public IntegerSBXCrossover(double crossoverProbability, double distributionIndex,
                               RandomGenerator<Double> randomGenerator) {

        if (crossoverProbability < 0) {
            throw new IllegalArgumentException("Crossover probability is negative: " + crossoverProbability);
        } else if (distributionIndex < 0) {
            throw new IllegalArgumentException("Distribution index is negative: " + distributionIndex);
        }
        this.crossoverProbability = crossoverProbability;
        this.distributionIndex = distributionIndex;
        this.random = randomGenerator;
    }

    /**
     * Get the crossover probability.
     *
     * @return the crossover probability.
     */
    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    /**
     * Get the distribution index.
     *
     * @return the distribution index.
     */
    public double getDistributionIndex() {
        return distributionIndex;
    }

    /**
     * Apply crossover to solution
     *
     * @param source the parents to operates.
     * @return the crossover elements
     * @throws NullPointerException     if source is null
     * @throws IllegalArgumentException if source size is other than 2
     */
    @Override
    public List<IntegerSolution> execute(List<IntegerSolution> source) {
        Objects.requireNonNull(source);
        if (source.size() != getNumberOfRequiredParents()) {
            throw new IllegalArgumentException("There must be two parents instead of " + source.size());
        }

        IntegerSolution parent1 = source.get(0);
        IntegerSolution parent2 = source.get(1);

        List<IntegerSolution> offspring = new ArrayList<IntegerSolution>(2);

        offspring.add((IntegerSolution) parent1.copy());
        offspring.add((IntegerSolution) parent2.copy());

        int i;
        double rand;
        double y1, y2, yL, yU;
        double c1, c2;
        double alpha, beta, betaq;
        int valueX1, valueX2;

        if (random.getRandomValue() <= crossoverProbability) {
            for (i = 0; i < parent1.getNumberOfVariables(); i++) {
                valueX1 = parent1.getVariable(i);
                valueX2 = parent2.getVariable(i);
                if (random.getRandomValue() <= 0.5) {
                    if (Math.abs(valueX1 - valueX2) > EPS) // noinspection DuplicatedCode
                    {

                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }

                        yL = parent1.getLowerBound(i);
                        yU = parent1.getUpperBound(i);
                        rand = random.getRandomValue();
                        //--------------------------------------
                        beta = 1.0 + (2.0 * (y1 - yL) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }

                        c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));

                        //--------------------------------------
                        beta = 1.0 + (2.0 * (yU - y2) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }

                        c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

                        if (c1 < yL) {
                            c1 = yL;
                        }

                        if (c2 < yL) {
                            c2 = yL;
                        }

                        if (c1 > yU) {
                            c1 = yU;
                        }

                        if (c2 > yU) {
                            c2 = yU;
                        }

                        if (random.getRandomValue() <= 0.5) {
                            offspring.get(0).setVariable(i, (int) c2);
                            offspring.get(1).setVariable(i, (int) c1);
                        } else {
                            offspring.get(0).setVariable(i, (int) c1);
                            offspring.get(1).setVariable(i, (int) c2);
                        }
                    } else {
                        offspring.get(0).setVariable(i, valueX1);
                        offspring.get(1).setVariable(i, valueX2);
                    }
                } else {
                    offspring.get(0).setVariable(i, valueX2);
                    offspring.get(1).setVariable(i, valueX1);
                }
            }
        }

        return offspring;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfRequiredParents() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfGeneratedChildren() {
        return 2;
    }

}
