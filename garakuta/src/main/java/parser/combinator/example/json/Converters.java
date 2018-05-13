package parser.combinator.example.json;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    public static JsonNode.JsonObject jobject(final Object x) {
        final List<JsonNode.JsonObject.Entry> entries = new ArrayList<>();
        final List<Object> list1 = (List<Object>) x; //and
        final List<Object> list2 = (List<Object>) list1.get(1); //{と}を捨てる
        if (list2.isEmpty()) {
            return JsonNode.jobject(entries);
        }
        final List<Object> list3 = (List<Object>) list2.get(0); //and
        entries.add(JsonNode.entry((JsonNode.JsonString) list3.get(0), (JsonNode) list3.get(2))); //1つめの要素
        final List<Object> list4 = (List<Object>) list3.get(3); //repeat
        for (final Object element : list4) {
            final List<Object> list5 = (List<Object>) element; //and
            entries.add(
                    JsonNode.entry((JsonNode.JsonString) list5.get(1), (JsonNode) list5.get(3)));
        }
        return JsonNode.jobject(entries);
    }

    public static JsonNode.JsonArray jarray(final Object x) {
        final List<JsonNode> elements = new ArrayList<>();
        final List<Object> list1 = (List<Object>) x; //and
        final List<Object> list2 = (List<Object>) list1.get(1); //[と]を捨てる
        if (list2.isEmpty()) {
            return JsonNode.jarray(elements);
        }
        final List<Object> list3 = (List<Object>) list2.get(0); //and
        elements.add((JsonNode) list3.get(0)); //1つめの要素
        final List<Object> list4 = (List<Object>) list3.get(1); //repeat
        for (final Object element : list4) {
            final List<Object> list5 = (List<Object>) element; //and
            elements.add((JsonNode) list5.get(1));
        }
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
