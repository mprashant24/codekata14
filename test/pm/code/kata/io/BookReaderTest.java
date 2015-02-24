package pm.code.kata.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BookReaderTest {

	@Before
	public void setUp() throws Exception {
		File test = new File("test.txt");
		FileWriter fw = new FileWriter(test);
		fw.write("This is test text!");
		fw.close();
	}

	@After
	public void tearDown() throws Exception {
		File test = new File("test.txt");
		if (test.exists()) {
			test.delete();
		}
	}

	@Test
	public void testBookReaderInitializationNullQueue() {
		try {
			new BookReader(null, "test.txt", new CountDownLatch(2));
		} catch (Exception e) {
			assertEquals("Bookreader intialization failed! Shared object null",
					e.getMessage());
		}

	}

	@Test
	public void testBookReaderInitializationMissingBook() {
		try {
			new BookReader(new ArrayBlockingQueue<String>(1), "xyz.txt",
					new CountDownLatch(2));
		} catch (Exception e) {
			assertEquals(
					"Bookreader intialization failed! Book does not exists",
					e.getMessage());
		}

	}

	@Test
	public void testBookReaderInitializationNullLatch() {
		try {
			new BookReader(new ArrayBlockingQueue<String>(1), "test.txt", null);
		} catch (Exception e) {
			assertEquals(
					"Bookreader intialization failed! Countdown latch object null",
					e.getMessage());
		}

	}

}
