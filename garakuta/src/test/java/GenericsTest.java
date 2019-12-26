
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.Test;

public class GenericsTest {

    static class C extends ArrayList<String> {

        private C(final CopyOnWriteArrayList<String> p) {
        }
    }

    LinkedList<String> f;

    @Test
    public void test_class() throws Exception {
        final ParameterizedType t = (ParameterizedType) C.class.getGenericSuperclass();

        assertArrayEquals(new Type[] { String.class }, t.getActualTypeArguments());
        assertTrue(t.getRawType() == ArrayList.class);
    }

    @Test
    public void test_field() throws Exception {
        final ParameterizedType t = (ParameterizedType) getClass()
                .getDeclaredField("f")
                .getGenericType();

        assertArrayEquals(new Type[] { String.class }, t.getActualTypeArguments());
        assertTrue(t.getRawType() == LinkedList.class);
    }

    @Test
    public void test_method_parameter() throws Exception {
        final ParameterizedType t = (ParameterizedType) getClass()
                .getDeclaredMethod("m", List.class)
                .getGenericParameterTypes()[0];

        assertArrayEquals(new Type[] { String.class }, t.getActualTypeArguments());
        assertTrue(t.getRawType() == List.class);
    }

    @Test
    public void test_constructor_parameter() throws Exception {
        final ParameterizedType t = (ParameterizedType) C.class.getDeclaredConstructor(
                CopyOnWriteArrayList.class).getGenericParameterTypes()[0];

        assertArrayEquals(new Type[] { String.class }, t.getActualTypeArguments());
        assertTrue(t.getRawType() == CopyOnWriteArrayList.class);
    }

    void m(final List<String> p) {
    }

}
