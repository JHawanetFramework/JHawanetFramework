package annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * It class denote a int, double or his wrapper values (Integer, Double) that can
 * be injected to Registrable problem or Operator.
 * <p>
 * It will show in GUI a TextField that will inject the writed value to constructor.
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface NumberInput {
	/**
	 * The name of parameters
	 * @return the name of parameters
	 */
	String displayName() default "";

	double defaultValue() default 0;

}
