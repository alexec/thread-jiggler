package threadjiggler.test;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import static junit.framework.TestCase.assertEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class CounterTest {
	Counter counter;
	int n = 10;

	@Test
	public void singleThreadedTest() throws Exception {

		for (int i = 0; i < n; i++) {
			counter.count();
		}

		assertEquals(n, counter.getCount());
	}

	@Test
	public void threadedTest() throws Exception {

		final CompletionService<Void> service = new ExecutorCompletionService<Void>(Executors.newFixedThreadPool(2));

		for (int i = 0; i < n; i++) {
			service.submit(new Callable<Void>() {
				@Override
				public Void call() {
					counter.count();
					return null;
				}
			});
		}

		for (int i = 0; i < n; ++i) {
			service.take().get();
		}

		assertEquals(n, counter.getCount());
	}
}
