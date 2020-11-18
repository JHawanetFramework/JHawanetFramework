/*
 *
 *  * Code taken and modify from https://github.com/jMetal/jMetal
 *  *
 *  * Copyright <2017> <Antonio J. Nebro, Juan J. Durillo>
 *  *
 *  * Permission is hereby granted, free of charge, to any person
 *  * obtaining a copy of this software and associated
 *  * documentation files (the "Software"), to deal in the Software
 *  * without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense,
 *  * and/or sell copies of the Software, and to permit persons to
 *  * whom the Software is furnished to do so, subject to the
 *  * following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall
 *  * be included in all copies or substantial portions of the
 *  * Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. © 2019
 *  * GitHub, Inc.
 *
 */

package model.metaheuristic.util.evaluator.impl;

import epanet.core.EpanetException;
import model.metaheuristic.problem.Problem;
import model.metaheuristic.solution.Solution;
import model.metaheuristic.util.evaluator.SolutionListEvaluator;

import java.util.List;

public class SequentialSolutionEvaluator<S extends Solution<?>> implements SolutionListEvaluator<S> {

    @Override
    public List<S> evaluate(List<S> solutionList, Problem<S> problem) throws EpanetException {
        for (S s : solutionList) {
            problem.evaluate(s);
        }
        return solutionList;
    }
}
