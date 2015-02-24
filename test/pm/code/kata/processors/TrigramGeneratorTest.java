package pm.code.kata.processors;

import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import pm.code.kata.exception.TrigramGeneratorException;
import pm.code.kata.model.TrigramsMap;
import pm.code.kata.processors.TrigramsGenerator;

public class TrigramGeneratorTest {

	@Test
	public void testTrigramGeneratorInitializationNullLineQueue() {
		try {
			new TrigramsGenerator(null, null);
		} catch (TrigramGeneratorException e) {
			assertEquals(
					"TrigramsGenerator initialization failed! Line queue should not be null.",
					e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void testTrigramGeneratorInitializationNullLatch() {
		try {
			new TrigramsGenerator(new ArrayBlockingQueue<String>(1), null);
		} catch (TrigramGeneratorException e) {
			assertEquals(
					"TrigramsGenerator initialization failed! Countdown latch should not be null.",
					e.getMessage());
			return;
		}
		fail();
	}

	@Test
	public void testTrigramsFromQueue() {
		try {
			BlockingQueue<String> lineQueue = new ArrayBlockingQueue<String>(1);
			lineQueue.put("This is test line.");
			TrigramsGenerator trigramsGenerator = new TrigramsGenerator(
					lineQueue, new CountDownLatch(1));
			ExecutorService excutorService = Executors
					.newSingleThreadExecutor();
			Future<TrigramsMap> trigramGeneratorResult = excutorService
					.submit(trigramsGenerator);
			TrigramsMap trigramsMap = trigramGeneratorResult.get();
			assertEquals(trigramsMap.size(), 2);
		} catch (Exception e) {
			fail();
		}
	}
}
