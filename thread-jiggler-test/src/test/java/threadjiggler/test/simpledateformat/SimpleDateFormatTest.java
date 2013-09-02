package threadjiggler.test.simpledateformat;

import org.junit.Test;
import org.junit.runner.RunWith;
import threadjiggler.test.Jiggle;
import threadjiggler.test.JigglingRunner;
import threadjiggler.test.Threads;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(JigglingRunner.class)
@Jiggle("threadjiggler.test.counter.*")
public class SimpleDateFormatTest {
	Date[] dates = new Date[3];
	String[] formatted = new String[dates.length];

	{
		//final Date date = new Date(2012, 1, 7, 2, 4, 41);
		//System.out.println("dateToFormatted.put(new Date(" + date.getTime()+"l), \"" + format.format(date) +"\");");
		dates[0] = new Date(61338726000000l);
		formatted[0] = "01/10/13 00:00";
		dates[1] = new Date(61322919781000l);
		formatted[1] = "01/04/13 01:23";
		dates[2] = new Date(61286724281000l);
		formatted[2] = "07/02/12 02:04";
	}

	SimpleDateFormat format = new SimpleDateFormat("dd/MM/YY hh:mm", Locale.ENGLISH);
	Random random = new Random();
	int n = 1000;

	@Test
	public void singleThreaded() throws Exception {

		for (int i = 0; i < n; i++) {
			assertFormatting();
		}
	}

	@Test(expected=AssertionError.class)
	public void multiThreaded() throws Throwable {

		try {
			Threads.call(n, 2, new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					assertFormatting();
					return null;
				}
			});
		} catch (ExecutionException e) {
			throw e.getCause();
		}
	}

	private void assertFormatting() {
		int j = random.nextInt(dates.length);
		assertEquals(formatted[j], format.format(dates[j]));
	}
}
