package model.metaheuristic.operator.mutation.impl;

import annotations.NumberInput;
import annotations.operator.DefaultConstructor;
import model.metaheuristic.operator.mutation.MutationOperator;
import model.metaheuristic.solution.impl.IntegerSolution;
import model.metaheuristic.util.random.BoundedRandomGenerator;
import model.metaheuristic.util.random.JavaRandom;
import model.metaheuristic.util.random.RandomGenerator;

import java.util.Objects;

/**
 * Class that perform a mutation setting up each element of the solution randomly between the defined bounds.
 *
 */
public class IntegerSimpleRandomMutation implements MutationOperator<IntegerSolution> {

	private double mutationProbability;
	private final RandomGenerator<Double> randomGenerator;
	private final BoundedRandomGenerator<Integer> pointRandomGenerator;

	/**
	 * Constructor
	 * @param probability the probability of mutation
	 */
	@DefaultConstructor(numbers = @NumberInput(displayName = "Probability", defaultValue = 0.03))
	public IntegerSimpleRandomMutation(double probability) {
		this(probability, () -> JavaRandom.getInstance().nextDouble(),
				(a, b) -> JavaRandom.getInstance().nextInt(a, b));
	}
	
	/**
	 * Constructor
	 * @param probability the probability of mutation.
	 * @param randomGenerator a random generator.
	 * @param pointRandomGenerator a random generator that generate numbers between a lower and a upper bound.
	 * @throws IllegalArgumentException if probability is negative.
	 * @throws NullPointerException if randomGenerator or pointRandonGenerator is null.
	 */
	public IntegerSimpleRandomMutation(double probability, RandomGenerator<Double> randomGenerator,
			BoundedRandomGenerator<Integer> pointRandomGenerator) {
		if (probability < 0) {
			throw new IllegalArgumentException("Mutation probability is negative: " + mutationProbability);
		}
		Objects.requireNonNull(randomGenerator);
		Objects.requireNonNull(pointRandomGenerator);

		this.mutationProbability = probability;
		this.randomGenerator = randomGenerator;
		this.pointRandomGenerator = pointRandomGenerator;
	}

	/* Getters */
	/**
	 * Get the mutation probability.
	 * @return the mutation probability.
	 */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/* Setters */
	/**
	 * Set the mutation probability.
	 * @param mutationProbability the mutation probability.
	 */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntegerSolution execute(IntegerSolution solution) {
		Objects.requireNonNull(solution);

		doMutation(mutationProbability, solution);

		return solution;
	}

	/** Implements the mutation operation */
	private void doMutation(double probability, IntegerSolution solution) {

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			if (randomGenerator.getRandomValue() <= probability) {
				Integer value = pointRandomGenerator.getRandomValue(solution.getLowerBound(i),
						solution.getUpperBound(i) + 1); // The last element is exclude for it is needed the +1
				solution.setVariable(i, value);
			}
		}

	}

}
