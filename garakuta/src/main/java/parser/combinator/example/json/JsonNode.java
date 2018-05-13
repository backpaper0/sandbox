package parser.combinator.example.json;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class JsonNode {

    public static JsonObject jobject(final List<JsonObject.Entry> entries) {
        return new JsonObject(entries);
    }

    public static JsonObject.Entry entry(final JsonString key, final JsonNode value) {
        return new JsonObject.Entry(key, value);
    }

    public static JsonArray jarray(final List<JsonNode> elements) {
        return new JsonArray(elements);
    }

    public static JsonBoolean jboolean(final boolean value) {
        return new JsonBoolean(value);
    }

    public static JsonNull jnull() {
        return new JsonNull();
    }

    public static JsonString jstring(final String value) {
        return new JsonString(value);
    }

    public static JsonNumber jnumber(final int value) {
        return new JsonNumber(value);
    }

    public static class JsonObject extends JsonNode {

        private final List<Entry> entries;

        public JsonObject(final List<Entry> entries) {
            this.entries = entries;
        }

        @Override
        public String toString() {
            return entries.stream().map(Objects::toString)
                    .collect(Collectors.joining(", ", "{", "}"));
        }

        public static class Entry {

            private final JsonString key;
            private final JsonNode value;

            public Entry(final JsonString key, final JsonNode value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public String toString() {
                return String.format("%s : %s", key, value);
            }
        }
    }

    public static class JsonArray extends JsonNode {

        private final List<JsonNode> elements;

        public JsonArray(final List<JsonNode> elements) {
            this.elements = elements;
        }

        @Override
        public String toString() {
            return elements.stream().map(Objects::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }

    public static class JsonBoolean extends JsonNode {

        private final boolean value;

        public JsonBoolean(final boolean value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static class JsonNull extends JsonNode {

        @Override
        public String toString() {
            return "null";
        }
    }

    public static class JsonString extends JsonNode {

        private final String value;

        public JsonString(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return '"' + value + '"';
        }
    }

    public static class JsonNumber extends JsonNode {

        private final int value;

        public JsonNumber(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
