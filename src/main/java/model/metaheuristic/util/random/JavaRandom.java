package model.metaheuristic.util.random;

import java.util.Random;

/**
 * A class that use the singleton pattern. It class contains random method.
 *
 */
public class JavaRandom {
	private static JavaRandom instance;
	
	private final Random random;
	
	private JavaRandom() {
		this.random = new Random(System.currentTimeMillis());
	}
	
	public static JavaRandom getInstance() {
		if (instance == null) {
			instance = new JavaRandom();
		}
		return instance;
	}

	/**
	 * @param seed the seed of random generator
	 * @see java.util.Random#setSeed(long)
	 */
	public void setSeed(long seed) {
		random.setSeed(seed);
	}

	/**
	 * @return a int
	 * @see java.util.Random#nextInt()
	 */
	public int nextInt() {
		return random.nextInt();
	}

	/**
	 * @param bound the upper bound (exclusive). Must be positive
	 * @return a int number
	 * @see java.util.Random#nextInt(int)
	 */
	public int nextInt(int bound) {
		return random.nextInt(bound);
	}
	
	/**
	 * Generate a random value between lowerBound and upperBound exclusive.
	 * @param lowerBound the lowerBound
	 * @param upperBound the upperBound exclusive
	 * @return int number between lowerBound and upperBound
	 */
	public int nextInt(int lowerBound, int upperBound) {
		return lowerBound + random.nextInt(upperBound - lowerBound);
	}

	/**
	 * @return a long number
	 * @see java.util.Random#nextLong()
	 */
	public long nextLong() {
		return random.nextLong();
	}

	/**
	 * @return a boolean
	 * @see java.util.Random#nextBoolean()
	 */
	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
	 * @return a float number
	 * @see java.util.Random#nextFloat()
	 */
	public float nextFloat() {
		return random.nextFloat();
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
	 * @return a double number
	 * @see java.util.Random#nextDouble()
	 */
	public double nextDouble() {
		return random.nextDouble();
	}

	/**
	 * Generate a random value between lowerBound and upperBound (inclusive).
	 * @param lowerBound the lowerBound
	 * @param upperBound the upperBound inclusive
	 * @return double number between lowerBound and upperBound
	 */
	public double nextDouble(double lowerBound, double upperBound) {
		return lowerBound + random.nextDouble()*(upperBound - lowerBound) ;
	}

	/**
	 * @return a double number
	 * @see java.util.Random#nextGaussian()
	 */
	public double nextGaussian() {
		return random.nextGaussian();
	}
	
	
	
}
