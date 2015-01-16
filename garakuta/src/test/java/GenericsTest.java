

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Test;

public class GenericsTest {

    static class C extends ArrayList<String> {

        private C(CopyOnWriteArrayList<String> p) {
        }
    }

    LinkedList<String> f;

    @Test
    public void test_class() throws Exception {
        ParameterizedType t =
            (ParameterizedType) C.class.getGenericSuperclass();

        assertThat(t.getActualTypeArguments(), is(new Type[] { String.class }));
        assertTrue(t.getRawType() == ArrayList.class);
    }

    @Test
    public void test_field() throws Exception {
        ParameterizedType t =
            (ParameterizedType) getClass()
                .getDeclaredField("f")
                .getGenericType();

        assertThat(t.getActualTypeArguments(), is(new Type[] { String.class }));
        assertTrue(t.getRawType() == LinkedList.class);
    }

    @Test
    public void test_method_parameter() throws Exception {
        ParameterizedType t =
            (ParameterizedType) getClass()
                .getDeclaredMethod("m", List.class)
                .getGenericParameterTypes()[0];

        assertThat(t.getActualTypeArguments(), is(new Type[] { String.class }));
        assertTrue(t.getRawType() == List.class);
    }

    @Test
    public void test_constructor_parameter() throws Exception {
        ParameterizedType t =
            (ParameterizedType) C.class.getDeclaredConstructor(
                CopyOnWriteArrayList.class).getGenericParameterTypes()[0];

        assertThat(t.getActualTypeArguments(), is(new Type[] { String.class }));
        assertTrue(t.getRawType() == CopyOnWriteArrayList.class);
    }

    void m(List<String> p) {
    }

}
