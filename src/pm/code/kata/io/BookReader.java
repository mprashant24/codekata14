package pm.code.kata.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import pm.code.kata.exception.BookReaderException;

public class BookReader implements Callable<Integer> {

	private BlockingQueue<String> lineQueue;
	private File book;
	private CountDownLatch latch;

	public BookReader(BlockingQueue<String> queue, String filePath,
			CountDownLatch latch) throws BookReaderException {
		this.lineQueue = queue;
		this.latch = latch;
		book = new File(filePath);

		if (lineQueue == null) {
			// TODO log before throwing exception
			throw new BookReaderException(
					"Bookreader intialization failed! Shared object null");
		}

		if (latch == null) {
			// TODO log before throwing exception
			throw new BookReaderException(
					"Bookreader intialization failed! Countdown latch object null");
		}

		if (book.exists() == false) {
			// TODO log before throwing exception
			throw new BookReaderException(
					"Bookreader intialization failed! Book does not exists");
		}
	}

	@Override
	public Integer call() throws BookReaderException {
		Integer lineCount = 0;
		BufferedReader bookReader = null;
		String line;
		try {
			bookReader = new BufferedReader(new FileReader(book));
			while ((line = bookReader.readLine()) != null) {
				lineCount += 1;
				lineQueue.put(line);
			}
		} catch (FileNotFoundException e) {
			throw new BookReaderException("Book does not exist: "
					+ book.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new BookReaderException("Error while reading book : "
					+ book.getAbsolutePath(), e);
		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
			// Leave the reading and report the line count.
		} finally {
			if (bookReader != null) {
				try {
					bookReader.close();
				} catch (IOException e) {
					// TODO log the exception for debugging purpose
				}
			}
			latch.countDown();
		}
		return lineCount;
	}

	public void cancelReading() {
		Thread.currentThread().interrupt();
	}
}
