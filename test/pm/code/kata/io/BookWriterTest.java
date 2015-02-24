package pm.code.kata.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import pm.code.kata.exception.BookWriterException;
import pm.code.kata.model.TrigramsMap;

public class BookWriterTest {

	private TrigramsMap trigramsMap;

	@Before
	public void setUp() throws Exception {
		trigramsMap = new TrigramsMap();
		trigramsMap.put("This is", "test");
		trigramsMap.put("is test", "text");
	}

	@Test
	public void testBookWriterInitializationDstFileIssue() {
		try {
			new BookWriter("\\\\unknownhost\\c$\\test.txt", trigramsMap, null,
					null);
		} catch (BookWriterException e) {
			assertEquals(
					"BookWriter initialization failed! Unable to create book at "
							+ new File("\\\\unknownhost\\c$\\test.txt")
									.getAbsolutePath(),
					e.getMessage());
		}

	}

	@Test
	public void testBookWriterInitializationNullTriageMap() {
		try {
			new BookWriter("test.txt", null, null, new CountDownLatch(2));
		} catch (BookWriterException e) {
			assertEquals(
					"BookWriter initialization failed! TrigramMap should not be null",
					e.getMessage());
		}

	}

	@Test
	public void testBookWriterInitializationNullQueue() {
		try {
			new BookWriter("test.txt", trigramsMap, null, null);
		} catch (Exception e) {
			assertEquals(
					"BookWriter initialization failed! Shared object line queue should not be null",
					e.getMessage());
		}

	}

	@Test
	public void testBookWriterInitializationNullLatch() {
		try {
			new BookWriter("test.txt", trigramsMap,
					new ArrayBlockingQueue<String>(1), null);
		} catch (Exception e) {
			assertEquals(
					"BookWriter initialization failed! ! Countdown latch should not be null",
					e.getMessage());
		}

	}

}
