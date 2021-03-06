/*
 * Code took from https://github.com/jMetal/jMetal
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
package model.metaheuristic.qualityindicator.impl.hypervolume.impl;

import exception.InvalidConditionException;
import model.metaheuristic.qualityindicator.impl.hypervolume.Hypervolume;
import model.metaheuristic.solution.Solution;
import model.metaheuristic.util.comparator.HypervolumeContributionComparator;
import model.metaheuristic.util.front.Front;
import model.metaheuristic.util.front.impl.ArrayFront;
import model.metaheuristic.util.front.util.FrontNormalizer;
import model.metaheuristic.util.front.util.FrontUtils;
import model.metaheuristic.util.point.Point;
import model.metaheuristic.util.solutionattribute.HypervolumeContributionAttribute;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements the hypervolume indicator. The code is the a Java version of the original
 * metric implementation by Eckart Zitzler. Reference: E. Zitzler and L. Thiele Multiobjective
 * Evolutionary Algorithms: A Comparative Case Study and the Strength Pareto Approach, IEEE
 * Transactions on Evolutionary Computation, vol. 3, no. 4, pp. 257-271, 1999.
 *
 * @author Antonio J. Nebro &lt;antonio@lcc.uma.es&gt;;
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class PISAHypervolume<S extends Solution<?>> extends Hypervolume<S> {

    private static final double DEFAULT_OFFSET = 100.0;
    private double offset = DEFAULT_OFFSET;

    /**
     * Default constructor
     */
    public PISAHypervolume() {
    }

    /**
     * Constructor with reference point
     *
     * @param referencePoint the reference point
     */
    public PISAHypervolume(double[] referencePoint) {
        super(referencePoint);
    }

    /**
     * Constructor
     *
     * @param referenceParetoFrontFile the path to reference pareto front file.
     * @throws FileNotFoundException if the reference front file isn't found.
     * @throws IOException           if there is a error reading the file.
     */
    public PISAHypervolume(String referenceParetoFrontFile) throws IOException, FileNotFoundException {
        super(referenceParetoFrontFile);
    }

    /**
     * Constructor
     *
     * @param referenceParetoFront the reference front
     */
    public PISAHypervolume(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    /**
     * Evaluate() method
     *
     * @param paretoFrontApproximation the solution list to use.
     * @return the indicator value
     * @throws NullPointerException if paretoFrontApproximation is null.
     */
    @Override
    public @NotNull Double evaluate(List<S> paretoFrontApproximation) {
        Objects.requireNonNull(paretoFrontApproximation);
        return hypervolume(new ArrayFront(paretoFrontApproximation), referenceParetoFront);
    }

    /**
     * returns true if 'point1' dominates 'points2' with respect to the
     * to the first 'noObjectives' objectives
     */
    private boolean dominates(double[] point1, double[] point2, int noObjectives) {
        int i;
        int betterInAnyObjective;

        betterInAnyObjective = 0;
        for (i = 0; i < noObjectives && point1[i] >= point2[i]; i++) {
            if (point1[i] > point2[i]) {
                betterInAnyObjective = 1;
            }
        }

        return ((i >= noObjectives) && (betterInAnyObjective > 0));
    }

    private void swap(double[][] front, int i, int j) {
        double[] temp;

        temp = front[i];
        front[i] = front[j];
        front[j] = temp;
    }

    /**
     * all nondominated points regarding the first 'noObjectives' dimensions
     * are collected; the points referenced by 'front[0..noPoints-1]' are
     * considered; 'front' is resorted, such that 'front[0..n-1]' contains
     * the nondominated points; n is returned
     */
    private int filterNondominatedSet(double[][] front, int noPoints, int noObjectives) {
        int i, j;
        int n;

        n = noPoints;
        i = 0;
        while (i < n) {
            j = i + 1;
            while (j < n) {
                if (dominates(front[i], front[j], noObjectives)) {
                    /* remove point 'j' */
                    n--;
                    swap(front, j, n);
                } else if (dominates(front[j], front[i], noObjectives)) {
          /* remove point 'i'; ensure that the point copied to index 'i'
          is considered in the next outer loop (thus, decrement i) */
                    n--;
                    swap(front, i, n);
                    i--;
                    break;
                } else {
                    j++;
                }
            }
            i++;
        }
        return n;
    }

    /**
     * calculate next value regarding dimension 'objective'; consider
     * points referenced in 'front[0..noPoints-1]'
     *
     * @throws IllegalArgumentException if noPoints is less than 1.
     */
    private double surfaceUnchangedTo(double[][] front, int noPoints, int objective) {
        int i;
        double minValue, value;

        if (noPoints < 1) {
            throw new IllegalArgumentException("run-time error");
        }

        minValue = front[0][objective];
        for (i = 1; i < noPoints; i++) {
            value = front[i][objective];
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    /* remove all points which have a value <= 'threshold' regarding the
    dimension 'objective'; the points referenced by
    'front[0..noPoints-1]' are considered; 'front' is resorted, such that
    'front[0..n-1]' contains the remaining points; 'n' is returned */
    private int reduceNondominatedSet(
            double[][] front, int noPoints, int objective, double threshold) {
        int n;
        int i;

        n = noPoints;
        for (i = 0; i < n; i++) {
            if (front[i][objective] <= threshold) {
                n--;
                swap(front, i, n);
            }
        }

        return n;
    }

    /**
     * Calculate the hypervolume.
     *
     * @param front        the front.
     * @param noPoints     the number of points.
     * @param noObjectives the number of objectives.
     * @return the hypervolume value.
     * @throws InvalidConditionException if non Dominated Points calculed is less than 1.
     */
    public double calculateHypervolume(double[][] front, int noPoints, int noObjectives) {
        int n;
        double volume, distance;

        volume = 0;
        distance = 0;
        n = noPoints;
        while (n > 0) {
            int nonDominatedPoints;
            double tempVolume, tempDistance;

            nonDominatedPoints = filterNondominatedSet(front, n, noObjectives - 1);
            if (noObjectives < 3) {
                if (nonDominatedPoints < 1) {
                    throw new InvalidConditionException("run-time error");
                }

                tempVolume = front[0][0];
            } else {
                tempVolume = calculateHypervolume(front, nonDominatedPoints, noObjectives - 1);
            }

            tempDistance = surfaceUnchangedTo(front, n, noObjectives - 1);
            volume += tempVolume * (tempDistance - distance);
            distance = tempDistance;
            n = reduceNondominatedSet(front, n, noObjectives - 1, distance);
        }
        return volume;
    }

    /**
     * Returns the hypervolume value of a front of points
     *
     * @param front          The front
     * @param referenceFront The true pareto front
     */
    private double hypervolume(Front front, Front referenceFront) {

        Front invertedFront;
        invertedFront = FrontUtils.getInvertedFront(front);

        int numberOfObjectives = referenceFront.getPoint(0).getDimension();

        // STEP4. The hypervolume (control is passed to the Java version of Zitzler code)
        return this.calculateHypervolume(
                FrontUtils.convertFrontToArray(invertedFront),
                invertedFront.getNumberOfPoints(),
                numberOfObjectives);
    }

    @Override
    public void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     * Compute the hypervolume contribution.
     *
     * @param solutionList       the solution list
     * @param referenceFrontList the reference front list.
     * @return the solutions list with the contribution attribute.
     */
    @Override
    public List<S> computeHypervolumeContribution(List<S> solutionList, List<S> referenceFrontList) {
        if (solutionList.size() > 1) {
            Front front = new ArrayFront(solutionList);
            Front referenceFront = new ArrayFront(referenceFrontList);

            // STEP 1. Obtain the maximum and minimum values of the Pareto front
            double[] maximumValues = FrontUtils.getMaximumValues(referenceFront);
            double[] minimumValues = FrontUtils.getMinimumValues(referenceFront);

            // STEP 2. Get the normalized front
            FrontNormalizer frontNormalizer = new FrontNormalizer(minimumValues, maximumValues);
            Front normalizedFront = frontNormalizer.normalize(front);

            // compute offsets for reference point in normalized space
            double[] offsets = new double[maximumValues.length];
            for (int i = 0; i < maximumValues.length; i++) {
                offsets[i] = offset / (maximumValues[i] - minimumValues[i]);
            }
            // STEP 3. Inverse the pareto front. This is needed because the original
            // metric by Zitzler is for maximization problem
            Front invertedFront = FrontUtils.getInvertedFront(normalizedFront);

            // shift away from origin, so that boundary points also get a contribution > 0
            for (int i = 0; i < invertedFront.getNumberOfPoints(); i++) {
                Point point = invertedFront.getPoint(i);

                for (int j = 0; j < point.getDimension(); j++) {
                    point.setValue(j, point.getValue(j) + offsets[j]);
                }
            }

            HypervolumeContributionAttribute<S> hvContribution = new HypervolumeContributionAttribute<>();

            // calculate contributions and sort
            double[] contributions = hvContributions(FrontUtils.convertFrontToArray(invertedFront));
            for (int i = 0; i < contributions.length; i++) {
                hvContribution.setAttribute(solutionList.get(i), contributions[i]);
            }

            Collections.sort(solutionList, new HypervolumeContributionComparator<S>());
        }
        return solutionList;
    }

    @Override
    public double getOffset() {
        return offset;
    }

    /**
     * Calculates how much hypervolume each point dominates exclusively. The points have to be
     * transformed beforehand, to accommodate the assumptions of Zitzler's hypervolume code.
     *
     * @param front transformed objective values
     * @return HV contributions
     */
    private double[] hvContributions(double[][] front) {

        int numberOfObjectives = front[0].length;
        double[] contributions = new double[front.length];
        double[][] frontSubset = new double[front.length - 1][front[0].length];
        LinkedList<double[]> frontCopy = new LinkedList<double[]>();
        Collections.addAll(frontCopy, front);
        double[][] totalFront = frontCopy.toArray(frontSubset);
        double totalVolume =
                this.calculateHypervolume(totalFront, totalFront.length, numberOfObjectives);
        for (int i = 0; i < front.length; i++) {
            double[] evaluatedPoint = frontCopy.remove(i);
            frontSubset = frontCopy.toArray(frontSubset);
            // STEP4. The hypervolume (control is passed to java version of Zitzler code)
            double hv = this.calculateHypervolume(frontSubset, frontSubset.length, numberOfObjectives);
            double contribution = totalVolume - hv;
            contributions[i] = contribution;
            // put point back
            frontCopy.add(i, evaluatedPoint);
        }
        return contributions;
    }

    /**
     * The name of indicator.
     *
     * @return the name of indicator.
     */
    @Override
    public @NotNull String getName() {
        return "PISA-HV";
    }
}
