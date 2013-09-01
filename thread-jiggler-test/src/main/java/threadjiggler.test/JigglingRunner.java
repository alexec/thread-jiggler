package threadjiggler.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import threadjiggler.core.JigglingClassLoader;


/**
 * A JUnit runner for running tests, but jiggling the threads. Those tests must be annotated with {@link Jiggle} so
 * we can tell which classes to jiggle.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class JigglingRunner extends BlockJUnit4ClassRunner {

	public JigglingRunner(Class<?> klass) throws InitializationError {
		super(get(klass));
	}

	private static Class<?> get(Class<?> klass) throws InitializationError {

		if (!klass.isAnnotationPresent(Jiggle.class)) {
			throw new InitializationError("class must be annotated with " + Jiggle.class);
		}

		final String pattern = klass.getAnnotation(Jiggle.class).value();

		try {
			return new JigglingClassLoader(Thread.currentThread().getContextClassLoader(), pattern).loadClass(klass.getName());
		} catch (ClassNotFoundException e) {
			throw new InitializationError(e);
		}
	}
}
