package threadjiggler.test.counter;

import net.jcip.annotations.NotThreadSafe;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@NotThreadSafe
public class BadCounter implements Counter {
	private int counter = 0 ;

	public void count() {
		counter++;
	}

	public int getCount() {
		return counter;
	}
}
