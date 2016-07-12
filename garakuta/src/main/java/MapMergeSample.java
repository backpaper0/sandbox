import java.util.HashMap;
import java.util.Map;

public class MapMergeSample {

    public static void main(String[] args) {

        Map<String, String> map1 = new HashMap<>();
        map1.put("x", "a");
        map1.put("y", "b");

        Map<String, String> map2 = new HashMap<>();
        map2.put("x", "c");
        map2.put("z", "d");

        map2.forEach((key, value) -> {
            map1.merge(key, value, String::concat);
        });

        System.out.println(map1);
    }
}
