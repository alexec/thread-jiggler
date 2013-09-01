package threadjiggler.test;

import java.util.concurrent.*;

/**
 * Utility class for running many tasks.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public final class Threads {
	private Threads() {}

	public interface CallableFactory<T> {
		/**
		 * @return A new callable for execution.
		 */
		Callable<T> createCallable();
	}

	public static void call(final int nCalls, final int nThreads, final Callable<Void> callable) throws InterruptedException, ExecutionException {

		call(nCalls, nThreads, new CallableFactory<Void>() {
			@Override
			public Callable<Void> createCallable() {
				return callable;
			}
		});
	}

	public static void call(int nCalls, int nThreads, CallableFactory<Void> factory) throws InterruptedException, ExecutionException {
		final ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		final CompletionService<Void> service = new ExecutorCompletionService<Void>(executor);

		for (int i = 0; i < nCalls; i++) {
			service.submit(factory.createCallable());
		}

		try {
			for (int i = 0; i < nCalls; ++i) {
				service.take().get();
			}
		} finally {
			executor.shutdown();
		}
	}
}
