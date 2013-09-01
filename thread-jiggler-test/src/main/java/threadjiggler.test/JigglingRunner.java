package threadjiggler.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import threadjiggler.core.JigglingClassLoader;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class JigglingRunner extends BlockJUnit4ClassRunner {

	public JigglingRunner(Class<?> klass) throws InitializationError {
		super(get(klass));
	}

	private static Class<?> get(Class<?> klass) throws InitializationError {

		final String pattern = klass.getAnnotation(Jiggle.class).value();

		try {
			return new JigglingClassLoader(Thread.currentThread().getContextClassLoader(), pattern).loadClass(klass.getName());
		} catch (ClassNotFoundException e) {
			throw new InitializationError(e);
		}
	}
}
