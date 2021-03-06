/*
 * Base on code from https://github.com/jMetal/jMetal
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
package model.metaheuristic.util.solutionattribute;

import model.metaheuristic.solution.Solution;
import model.metaheuristic.util.comparator.ObjectiveComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class implements the crowding distance
 */
public class CrowdingDistance<S extends Solution<?>> extends SolutionAttribute<S, Double> {

	/**
	 * Assigns crowding distances to all solutions in a <code>SolutionSet</code>.
	 *
	 * @param solutionList The <code>SolutionSet</code>.
	 * @throws NullPointerException if solutionList is null
	 */
	public void computeDensityEstimator(List<S> solutionList) {
		Objects.requireNonNull(solutionList);
		int size = solutionList.size();

		if (size == 0) {
			return;
		}

		if (size == 1) {
			solutionList.get(0).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);
			return;
		}

		if (size == 2) {
			solutionList.get(0).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);
			solutionList.get(1).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);

			return;
		}

		// Use a new SolutionSet to avoid altering the original solutionSet
		List<S> front = new ArrayList<>(size);
		front.addAll(solutionList);

		for (int i = 0; i < size; i++) {
			front.get(i).setAttribute(getAttributeIdentifier(), 0.0);
		}

		double objectiveMaxn;
		double objectiveMinn;
		double distance;

		int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();

		for (int i = 0; i < numberOfObjectives; i++) {
			// Sort the population by objective n
			Collections.sort(front, new ObjectiveComparator<S>(i));
			objectiveMinn = front.get(0).getObjective(i);
			objectiveMaxn = front.get(front.size() - 1).getObjective(i);

			// Set de crowding distance
			front.get(0).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);
			front.get(size - 1).setAttribute(getAttributeIdentifier(), Double.POSITIVE_INFINITY);

			for (int j = 1; j < size - 1; j++) {
				distance = front.get(j + 1).getObjective(i) - front.get(j - 1).getObjective(i);
				distance = distance / (objectiveMaxn - objectiveMinn);
				distance += (double) front.get(j).getAttribute(getAttributeIdentifier());
				front.get(j).setAttribute(getAttributeIdentifier(), distance);
			}
		}
	}

	/**
	 * Get the attribute identifier
	 */
	@Override
	public Object getAttributeIdentifier() {
		return this.getClass();
	}
}
