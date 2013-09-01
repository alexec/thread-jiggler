Thread Jiggeling
====
Background
---
I was recently researching how to test multithreaded code for threading issues, and found out about a tool from IBM Haifa hard to Google name of ConTest, but couldn't find any useful implementations. So naturally, I thought I'd spike my own.

Consider this canonical simple, but thread unsafe class:

    public class Counter {
        private int count = 0 ;

        public void count() {
            count++;
        }
    }

The count method's byte code is:

    ALOAD 0
    DUP
    GETFIELD asm/Foo.counter : I
    ICONST_1
    IADD
    PUTFIELD asm/Foo.counter : I

This provides several places where calls context switch can mean that the count is not atomic. But how can you test this? Let consider a quick unit test:

	Counter counter;
	int n = 1000;

	@Test
	public void singleThreadedTest() throws Exception {

		for (int i = 0; i < n; i++) {
			counter.count();
		}

		assertEquals(n, counter.getCount());
	}

OK, so this test runs in a single thread, and passes. Lets try and run this on two threads to try and see if it fails.

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

This also passes. In fact, on my computer I can increase n to 100,000 before I have any failures.

    junit.framework.AssertionFailedError:
    Expected :1000000
    Actual   :999661

What have we learned? We've learned a simple way to run a multithreaded test, but we've learned that, because we can't control when threads do their work, it's a bit trial and error.

Thread Jiggling
---
OK. We've


Further reading:

* [Java Concurrency](ftp://ftp.cs.umanitoba.ca/pub/IPDPS03/DATA/W20_PADTD_02.PDF)
* [Concurrent Bug Patterns and How to Test Them - Eitan Farchi, Yarden Nir, Shmuel Ur IBM Haifa Research Labs](ftp://ftp.cs.umanitoba.ca/pub/IPDPS03/DATA/W20_PADTD_02.PDF)
* [A presentation describing the difficulty of testing and debugging concurrent software - Shmuel Ur](http://www.almaden.ibm.com/laborday/haifa/projects/verification/contest/papers/testingConcurrentJune2008ForMS.pdf)
