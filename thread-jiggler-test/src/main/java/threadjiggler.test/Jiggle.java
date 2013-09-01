package threadjiggler.test;

import java.lang.annotation.*;

/**
 * Indicate which classes to jiggle..
 *
 * @author alexec (alex.e.c@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Jiggle {
	/**
	 * @return A regular expression for classes to jiggle.
	 */
	String value();
}
