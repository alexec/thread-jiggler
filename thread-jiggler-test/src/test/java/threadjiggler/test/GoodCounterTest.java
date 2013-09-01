package threadjiggler.test;

import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(JigglingRunner.class)
@Jiggle("threadjiggler.test.GoodCounter")
public class GoodCounterTest extends CounterTest {
	@Before
	public void setUp() throws Exception {
		counter = new GoodCounter();
	}
}
