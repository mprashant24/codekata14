package pm.code.kata.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class TrigramsMap {

	private HashMap<String, ArrayList<String>> trigramStore;
	private HashMap<String, Integer> counterMap;

	public TrigramsMap() {
		trigramStore = new HashMap<>();
		counterMap = new HashMap<>();
	}

	public int size() {
		return trigramStore.size();
	}

	public void put(String key, String value) {
		ArrayList<String> valueList = trigramStore.get(key);
		if (valueList == null) {
			valueList = new ArrayList<>();
			trigramStore.put(key, valueList);
		}
		valueList.add(value);
	}

	public String get(String key) {
		String result = null;
		if (trigramStore.containsKey(key)) {
			Integer valueListCounter = counterMap.get(key);
			if (valueListCounter == null) {
				valueListCounter = new Integer(0);
			}
			ArrayList<String> valueList = trigramStore.get(key);
			int index = valueListCounter.intValue() % valueList.size();
			result = valueList.get(index);
			counterMap.put(key, valueListCounter + 1);
		}

		return result;
	}

	public String getRandomKey() {
		String result = null;
		Collection<String> keySet = trigramStore.keySet();
		if (keySet.size() > 0) {
			ArrayList<String> keys = new ArrayList<>();
			keys.addAll(keySet);
			Random random = new Random();
			result = keys.get(random.nextInt(keys.size()));
		}
		return result;
	}

}
