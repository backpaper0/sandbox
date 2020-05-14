package com.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Messages {

	private static Map<String, String> values = Collections.synchronizedMap(new HashMap<>());

	public static void put(String key, String value) {
		values.put(key, value);
	}

	public static String getAndRemove(String key) {
		return values.remove(key);
	}
}
