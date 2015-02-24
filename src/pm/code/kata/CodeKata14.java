package pm.code.kata;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pm.code.kata.exception.CodekataException;
import pm.code.kata.io.BookReader;
import pm.code.kata.io.BookWriter;
import pm.code.kata.model.TrigramsMap;
import pm.code.kata.processors.TrigramsGenerator;

public class CodeKata14 {

	public TrigramsMap buildTrigramFromBook(String filePath,
			int readerQueueCapacity) throws CodekataException {
		TrigramsMap result = null;
		ArrayBlockingQueue<String> lineQueue = new ArrayBlockingQueue<>(
				readerQueueCapacity);
		CountDownLatch latch = new CountDownLatch(2);

		BookReader bookReader = new BookReader(lineQueue, filePath, latch);
		TrigramsGenerator trigramsGenerator = new TrigramsGenerator(lineQueue,
				latch);

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<Integer> bookReaderResult = executorService.submit(bookReader);

		Future<TrigramsMap> trigramsGeneratorResult = executorService
				.submit(trigramsGenerator);

		try {
			latch.await();

		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
		} finally {
			executorService.shutdown();
		}

		try {
			int lineCount = bookReaderResult.get();
			System.out.println(lineCount + " processed from " + filePath);
		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
		} catch (ExecutionException e) {
			// TODO log exception and re-throw the exception
			throw new CodekataException(e.getLocalizedMessage(), e.getCause());
		}

		try {
			result = trigramsGeneratorResult.get();

		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
		} catch (ExecutionException e) {
			// TODO log exception and re-throw the exception
			throw new CodekataException(e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}

	public void rewriteBookFromTrigram(TrigramsMap trigramsMap,
			String srcFileName, String dstFileName, int readerQueueCapacity)
			throws CodekataException {
		ArrayBlockingQueue<String> lineQueue = new ArrayBlockingQueue<>(
				readerQueueCapacity);
		CountDownLatch latch = new CountDownLatch(2);

		BookReader bookReader = new BookReader(lineQueue, srcFileName, latch);
		BookWriter bookWriter = new BookWriter(dstFileName, trigramsMap,
				lineQueue, latch);

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<Integer> bookReaderResult = executorService.submit(bookReader);

		Future<Integer> bookWriterResult = executorService.submit(bookWriter);

		try {
			latch.await();

		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
		} finally {
			executorService.shutdown();
		}

		try {
			int lineCount = bookReaderResult.get();
			System.out.println(lineCount + " processed from " + srcFileName);
		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
		} catch (ExecutionException e) {
			// TODO log exception and re-throw the exception
			throw new CodekataException(e.getLocalizedMessage(), e.getCause());
		}

		try {
			int lineCount = bookWriterResult.get();
			System.out.println(lineCount + " lines written to " + dstFileName);
		} catch (InterruptedException e) {
			// TODO log the exception for debugging purpose
		} catch (ExecutionException e) {
			// TODO log exception and re-throw the exception
			throw new CodekataException(e.getLocalizedMessage(), e.getCause());
		}

	}

	public static void main(String[] args) {
		CodeKata14 codeKata14 = new CodeKata14();
		TrigramsMap trigramMap = null;
		try {
			if (args.length != 1) {
				System.out.println("Book name agrument is missing!");
				return;
			}
			String srcFileName = args[0];
			System.out.println();
			String dstFileName = args[0].substring(0, args[0].indexOf('.'))
					+ "_" + System.currentTimeMillis() + ".txt";
			System.out.println("Building trigrams from book!");
			trigramMap = codeKata14.buildTrigramFromBook(srcFileName, 100000);
			System.out.println("Re-writing book using trigrams!");
			codeKata14.rewriteBookFromTrigram(trigramMap, srcFileName,
					dstFileName, 100000);

		} catch (CodekataException e) {
			e.printStackTrace();
		}
	}

}
