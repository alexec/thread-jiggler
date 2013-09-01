package threadjiggler.test;

import java.lang.annotation.*;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Jiggle {
	String value();
}
