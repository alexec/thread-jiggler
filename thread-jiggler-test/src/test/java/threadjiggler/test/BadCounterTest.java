package threadjiggler.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(JigglingRunner.class)
@Jiggle("threadjiggler.test.*")
public class BadCounterTest extends CounterTest {

	@Before
	public void setUp() throws Exception {
		counter = new BadCounter();
	}

	@Override
	@Test(expected = AssertionError.class)
	public void threadedTest() throws Exception {
		super.threadedTest();
	}
}