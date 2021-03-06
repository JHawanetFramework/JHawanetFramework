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
package model.metaheuristic.solution;

import model.metaheuristic.util.io.SolutionListOutput;

import java.util.List;
import java.util.Map;

/**
 * Representation of the solution. It contain the result of objective function
 * associated to his decision variables.
 *
 * @param <Type> The type that contains the solution.
 *
 */
public interface Solution<Type> {
	/**
	 * Get the decision variables indicated by index.
	 * 
	 * @param index Index of decision variable.
	 * @return the variable in the index.
	 */
	Type getVariable(int index);

	/**
	 * Let set or add new decision variables to solution.
	 * 
	 * @param index the index associated to variable to be added or modified.
	 * @param value the value associated to decision variable.
	 */
	void setVariable(int index, Type value);

	/**
	 * Get the variable as a String <br>
	 * <br>
	 * <br><br><strong>Notes:</strong> <br>
	 * This method is used by the result window to get the variable values and for {@link SolutionListOutput}.
	 *
	 * @param index the index of variable to return.
	 * @return the variable.
	 */
	String getVariableAsString(int index);

	/**
	 * Get all variables.
	 * 
	 * @return a list with the value of variables.
	 */
	List<Type> getVariables();

	/**
	 * Get the objective value associated to index.
	 * 
	 * @param index the index assigned to objective value when was saved.
	 * @return the value of objective in the position index.
	 */
	double getObjective(int index);

	/**
	 * Set or add a new objective.
	 * 
	 * @param index the index associated to objective.
	 * @param value the value of this objective.
	 */
	void setObjective(int index, double value);

	/**
	 * Get all objective values.
	 * 
	 * @return A array with all values of objective function.
	 */
	double[] getObjectives();

	/**
	 * Get attributed added to solution.
	 * 
	 * @param id The element associated to attribute when was saved.
	 * @return The attribute.
	 */
	Object getAttribute(Object id);

	/**
	 * Set or add attribute in this solution.
	 * 
	 * @param id    The key associated to value.
	 * @param value The value.
	 */
	void setAttribute(Object id, Object value);

	/**
	 * Check if a specific attribute is present.
	 * @param id the id of the attribute.
	 * @return true if there is present; false in otherwise.
	 */
	boolean hasAttribute(Object id) ;

	/**
	 * Get all attributes.
	 * 
	 * @return A Map with the keys and his respectives values.
	 */
	Map<Object, Object> getAttributes();


	/**
	 * Get the number of decision variables.
	 * 
	 * @return the number of variables.
	 */
	int getNumberOfVariables();

	/**
	 * Get the number of objectives.
	 * 
	 * @return the number of objectives.
	 */
	int getNumberOfObjectives();

	/**
	 * Copy the solution and make a new without any reference to original solution.
	 * 
	 * @return the copy solution.
	 */
	Solution<Type> copy();

}