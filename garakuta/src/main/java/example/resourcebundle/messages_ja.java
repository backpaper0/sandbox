package example.resourcebundle;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

public final class messages_ja extends ResourceBundle {

	private final Map<String, String> messages = Map.of("hello", "こんにちは世界！");

	@Override
	protected Object handleGetObject(String key) {
		return messages.get(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(messages.keySet());
	}
}
