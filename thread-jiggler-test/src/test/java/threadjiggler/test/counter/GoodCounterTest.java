package threadjiggler.test.counter;

import org.junit.Before;
import org.junit.runner.RunWith;
import threadjiggler.test.Jiggle;
import threadjiggler.test.JigglingRunner;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(JigglingRunner.class)
@Jiggle("threadjiggler.test.counter.*")
public class GoodCounterTest extends CounterTest {
	@Before
	public void setUp() throws Exception {
		counter = new GoodCounter();
	}
}
