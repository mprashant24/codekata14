package pm.code.kata.model;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class TrigramMapTest {

	@Test
	public void testPutAndGet() {
		TrigramsMap trigramsMap = new TrigramsMap();
		trigramsMap.put("TestKey", "Value");
		assertEquals("Value", trigramsMap.get("TestKey"));
	}

	@Test
	public void testSameKeyMultipleValue() {
		TrigramsMap trigramsMap = new TrigramsMap();
		trigramsMap.put("Test Key", "Value1");
		trigramsMap.put("Test Key", "Value2");
		trigramsMap.get("Test Key");
		assertEquals("Value2", trigramsMap.get("Test Key"));
	}

	@Test
	public void testSize() {
		TrigramsMap trigramsMap = new TrigramsMap();
		int randomNumber = new Random().nextInt(10);
		for (int index = 0; index < randomNumber; index++) {
			trigramsMap.put("key" + index, "Value" + index);
		}
		assertEquals(randomNumber, trigramsMap.size());
	}

	@Test
	public void testRandomKey() {
		TrigramsMap trigramsMap = new TrigramsMap();
		int randomNumber = new Random().nextInt(10);
		for (int index = 0; index < randomNumber; index++) {
			trigramsMap.put("key" + index, "Value" + index);
		}

		String key1 = trigramsMap.getRandomKey();
		String key2 = trigramsMap.getRandomKey();
		assertFalse(key1.equals(key2));
	}
}
