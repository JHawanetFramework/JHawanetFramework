package registrable;

import model.metaheuristic.experiment.Experiment;
import registrable.multiobjective.PumpSchedulingNSGAIIRegister;

/**
 * It class let configure experiment with his algorithm and his operator to a multiobjective
 * problem.
 * <p>
 * A example is showed in the class {@link PumpSchedulingNSGAIIRegister}.
 *
 * @see Registrable
 */
public interface MultiObjectiveRegistrable extends Registrable<Experiment<?>> {

	/**
	 * Builds a new experiment and leaves it ready for execution. This method will be
	 * called by the GUI. The call
	 * include the path of the inp file of the opened network.
	 * 
	 * @param inpPath the path of inp file, or null if there isn't a network opened.
	 * @return the experiment ready for execution
	 * @throws Exception if an exception occurs when building the algorithm
	 */
	@Override
	Experiment<?> build(String inpPath) throws Exception;
}
