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
package model.metaheuristic.experiment.util;

import model.metaheuristic.problem.Problem;
import model.metaheuristic.solution.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class used to add a tag field to a problem.
 */
public final class ExperimentProblem<S extends Solution<?>> {
	@NotNull private final Problem<S> problem;
	@NotNull private final String tag;

	/**
	 * Constructor.
	 * @param problem the problem.
	 * @param tag the tag of algorithm.
	 * @throws NullPointerException if problem or tag is null.
	 */
	public ExperimentProblem(@NotNull Problem<S> problem, @NotNull String tag) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(tag);
		this.problem = problem;
		if (problem.getName().isEmpty()){
			this.tag = problem.getClass().getSimpleName();
		}
		else{
			this.tag = tag;
		}
	}

	/**
	 * Constructor.
	 * @param problem the problem.
	 * @throws NullPointerException if problem is null.
	 */
	public ExperimentProblem(@NotNull Problem<S> problem) {
		this(problem, problem.getName());
	}

	/**
	 * Get the problem
	 * @return the problem
	 */
	public @NotNull Problem<S> getProblem() {
		return problem;
	}

	/**
	 * Get the tag.
	 * @return the tag.
	 */
	public @NotNull String getTag() {
		return tag;
	}

	/**
	 * Close the resource of the problems calling this close method.
	 * This method is called when the experiment finish.
	 * @throws Exception
	 */
	public void closeResources() throws Exception {
		this.problem.closeResources();
	}
}
