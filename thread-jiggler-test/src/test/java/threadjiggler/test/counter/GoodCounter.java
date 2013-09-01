package threadjiggler.test.counter;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@ThreadSafe
public class GoodCounter implements Counter {
	private AtomicInteger counter = new AtomicInteger();

	public void count() {
		counter.incrementAndGet();
	}

	public int getCount() {
		return counter.get();
	}
}
