package com.example;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeDemo {

	public static void main(String[] args) throws IOException {
		new TreeDemo().run();
	}

	void run() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		String json = objectMapper.writeValueAsString(Map.of("val", "hello",
				"arr", List.of(1, 2, 3),
				"obj", Map.of("a", "foo", "b", "bar")));

		System.out.println(json);

		JsonNode root = objectMapper.readTree(json);
		System.out.println(root);

		transform(root);

		System.out.println(root);
	}

	void transform(JsonNode node) {
		System.out.println("transform: " + node);
		if (node.isObject()) {
			ObjectNode objectNode = (ObjectNode) node;
			Iterator<String> fieldNames = objectNode.fieldNames();
			while (fieldNames.hasNext()) {
				String fieldName = fieldNames.next();
				if (fieldName.equals("a")) {
					objectNode.set(fieldName, new TextNode("xxx"));
				} else {
					transform(objectNode.get(fieldName));
				}
			}
		} else {
			node.forEach(this::transform);
		}
	}
}
