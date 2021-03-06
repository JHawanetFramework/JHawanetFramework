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
package model.metaheuristic.problem;

import epanet.core.EpanetException;
import model.epanet.element.Network;
import model.metaheuristic.solution.Solution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class that denote a problem. <br>
 * <br>
 * <p>
 * If the problem open a resource override the close method to close its.
 *
 * <strong>Notes:</strong>
 * <p>
 * For developer: If you implement a function that call {@link #closeResources} be careful not to close epanet until the experiment is finished,
 * otherwise the program will close not show any warning error. This is probably due to a problem in epanet dll library.
 *
 * @param <S> Type of solution
 */
public interface Problem<S extends Solution<?>> {

    /**
     * Get the number of variables associated to problem.
     *
     * @return number of variables.
     */
    int getNumberOfVariables();

    /**
     * Get the number of objectives that contain this problem.
     *
     * @return number of objectives.
     */
    int getNumberOfObjectives();

    /**
     * Get the number of constrains to this problem.
     *
     * @return number of constrains.
     */
    int getNumberOfConstraints();

    /**
     * Evaluate the solution
     *
     * @param solution The solution object to evaluate
     * @throws EpanetException If there is a problem in EPANETToolkit to evaluate
     *                         the solution.
     */
    void evaluate(S solution) throws EpanetException;

    /**
     * Make a solution to this problem. This can be created randomly and be used to
     * fill the initial population needed in some algorithms.
     * <p>
     * Must not return null value.
     *
     * @return Solution.
     */
    @NotNull S createSolution();

    /**
     * Setting the lower bound for each decision variable
     *
     * @param index the index of decision variable
     * @return lower bound of the decision variable
     */
    double getLowerBound(int index);

    /**
     * Setting the upper bound for each decision variable
     *
     * @param index the index of decision variable
     * @return upper bound of the decision variable
     */
    double getUpperBound(int index);

    /**
     * Apply the solution to the network.
     *
     * <br>
     * <br>
     * <strong>Notes:</strong> <br>
     * This method is used to save the solution as a inp from the ResultWindow.
     * Return null if you don't want use this method, i.e, you don't want save
     * solution in inp for a problem.
     *
     * @param network  a copy of the network instance opened configured with inp
     *                 setting up.
     * @param solution the solution to be setting in the network. It solution is the
     *                 same type of S, so you can cast it.
     * @return the network received and modified or null.
     */
    @Nullable default Network applySolutionToNetwork(Network network, Solution<?> solution){
        return null;
    }

    /**
     * Get the name of the problem.
     *
     * @return the name of problem algorithm or empty string.
     */
    @NotNull String getName();

    /**
     * Use this method if you need close a resource override this method to close it. His default
     * implementation is a empty body.
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * This method will be called when the experiment finish.
     * @throws Exception if a exception is throw while close resources
     */
    default void closeResources() throws Exception {
        /*
         * Be careful not to close epanet until the experiment is finished, otherwise the program will close
         * and not show any warning error. This is probably due to a problem in the library.
         */
    }
}