package parser.combinator.example.json;

import java.util.List;

public class Converters {

	public static JsonNode.JsonObject jobject(final Object x) {
		final List<JsonNode.JsonObject.Entry> entries = (List<JsonNode.JsonObject.Entry>) x;
		return JsonNode.jobject(entries);
	}

	public static JsonNode.JsonObject.Entry entry(final Object x) {
		final List<Object> list = (List<Object>) x;
		final JsonNode.JsonString key = (JsonNode.JsonString) list.get(0);
		final JsonNode value = (JsonNode) list.get(2);
		return JsonNode.entry(key, value);
	}

	public static JsonNode.JsonArray jarray(final Object x) {
		final List<JsonNode> elements = (List<JsonNode>) x;
		return JsonNode.jarray(elements);
	}

	public static JsonNode.JsonNull jnull(final Object x) {
		return JsonNode.jnull();
	}

	public static JsonNode.JsonBoolean jboolean(final Object x) {
		return JsonNode.jboolean(Boolean.parseBoolean((String) x));
	}

	public static JsonNode.JsonString jstring(final Object x) {
		return JsonNode.jstring((String) ((List<Object>) x).get(1));
	}

	public static JsonNode.JsonNumber jnumber(final Object x) {
		return JsonNode.jnumber(Integer.parseInt((String) x));
	}
}
