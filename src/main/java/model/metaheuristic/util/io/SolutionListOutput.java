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
package model.metaheuristic.util.io;

import model.metaheuristic.solution.Solution;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Class with methods to write solutions to file
 */
public class SolutionListOutput {
    private String separator = "\t";
    private String funFileName = "FUN";
    private String varFileName = "VAR";
    private final List<? extends Solution<?>> solutionList;

    /**
     * Constructor
     *
     * @param solutionList the list of solution
     * @throws NullPointerException if solutionList is null
     */
    public SolutionListOutput(List<? extends Solution<?>> solutionList) {
        Objects.requireNonNull(solutionList);
        this.solutionList = solutionList;
    }

    public SolutionListOutput setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    public SolutionListOutput setFunFileName(String name) {
        this.funFileName = name;
        return this;
    }

    public SolutionListOutput setVarFileName(String name) {
        this.varFileName = name;
        return this;
    }

    /**
     * Write a FUN and VAR file. The FUN has the objectives. The VAR file has the
     * decision variables.
     *
     * @throws IOException           If an I/O error occurs
     * @throws FileNotFoundException if the file exists but is a directory rather
     *                               than a regular file, does not exist but cannot
     *                               be created, or cannot be opened for any other
     *                               reason.
     */
    public void write() throws FileNotFoundException, IOException {
        printObjectivesToFile();
        printVariablesToFile();
    }

    /**
     * Format the variables of each solution to a String
     *
     * @param solution the solution
     * @return a string with the variables
     */
    private String formatVAR(Solution<?> solution) {
        StringBuilder text = new StringBuilder();
        int numberOfVariables = solution.getNumberOfVariables();
        for (int i = 0; i < numberOfVariables - 1; i++) {
            text.append(solution.getVariableAsString(i)).append(this.separator);
        }
        text.append(solution.getVariableAsString(numberOfVariables - 1));
        return text.toString();
    }

    /**
     * Format the objectives of each solution to a String
     *
     * @param solution the solution
     * @return a string with the objectives
     */
    private String formatFUN(Solution<?> solution) {
        StringBuilder text = new StringBuilder();
        int numberOfObjectives = solution.getNumberOfObjectives();
        for (int i = 0; i < numberOfObjectives - 1; i++) {
            text.append(solution.getObjective(i)).append(this.separator);
        }
        text.append(solution.getObjective(numberOfObjectives - 1));
        return text.toString();
    }

    public void printObjectivesToFile(String filepath) throws FileNotFoundException, IOException {
        try (BufferedWriter buffFunWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.ISO_8859_1))) {
            for (Solution<?> solution : this.solutionList) {
                buffFunWriter.write(formatFUN(solution));
                buffFunWriter.write("\n");
            }
        }
    }

    public void printVariablesToFile(String filepath) throws FileNotFoundException, IOException {
        try (BufferedWriter buffVarWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.ISO_8859_1))) {
            for (Solution<?> solution : this.solutionList) {
                buffVarWriter.write(formatVAR(solution));
                buffVarWriter.write("\n");
            }
        }
    }

    /**
     * Write the Objectives file
     *
     * @throws FileNotFoundException if the file exists but is a directory rather
     *                               than a regular file, does not exist but
     *                               can not be created, or cannot be opened for any
     *                               other reason
     * @throws IOException           If an I/O error occurs
     */
    public void printObjectivesToFile() throws FileNotFoundException, IOException {
        printObjectivesToFile(this.funFileName);
    }

    /**
     * Write the variables file
     *
     * @throws FileNotFoundException if the file exists but is a directory rather
     *                               than a regular file, does not exist but
     *                               can not be created, or cannot be opened for any
     *                               other reason
     * @throws IOException           If an I/O error occurs
     */
    public void printVariablesToFile() throws FileNotFoundException, IOException {
        printVariablesToFile(this.varFileName);
    }
}
