package pm.code.kata.processors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pm.code.kata.exception.TrigramGeneratorException;
import pm.code.kata.model.TrigramsMap;

public class TrigramsGenerator implements Callable<TrigramsMap> {

	private BlockingQueue<String> lineQueue;
	private TrigramsMap trigramsMap;
	private CountDownLatch latch;

	public TrigramsGenerator(BlockingQueue<String> queue, CountDownLatch latch)
			throws TrigramGeneratorException {
		lineQueue = queue;
		trigramsMap = new TrigramsMap();
		this.latch = latch;

		if (lineQueue == null) {
			throw new TrigramGeneratorException(
					"TrigramsGenerator initialization failed! Line queue should not be null.");
		}

		if (latch == null) {
			throw new TrigramGeneratorException(
					"TrigramsGenerator initialization failed! Countdown latch should not be null.");
		}
	}

	@Override
	public TrigramsMap call() throws TrigramGeneratorException {
		try {
			Pattern pattern = Pattern
					.compile("([^$]|[^\\s]*)\\s+(([^\\s]*)\\s+([^\\s]*)(\\s.*)?)");
			String line = "";
			String newLine = "";
			while (!lineQueue.isEmpty() || latch.getCount() > 1) {
				newLine = lineQueue.take();
				if (line.endsWith(" ") && !newLine.isEmpty()) {
					line = line + newLine;
				} else if (!newLine.isEmpty()) {
					line = line + " " + newLine;
				}

				line = line.replaceAll("[\\.,\\!\":]", "");

				Matcher matcher = pattern.matcher(line);
				while (line != null && matcher.matches()) {
					if (!matcher.group(1).isEmpty()) {
						trigramsMap.put(
								matcher.group(1) + " " + matcher.group(3),
								matcher.group(4));
					}
					line = matcher.group(2);
					matcher = pattern.matcher(line);
				}
			}
			if (trigramsMap.size() == 0) {
				throw new TrigramGeneratorException(
						"Trigrams generation failed! TrigramsMap size should not be zero.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
		return trigramsMap;
	}

}
