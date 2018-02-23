package example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

class DeserializeAndConstructorTest extends DeserializeAndConstructor {

    @Test
    void test() throws Exception {
        final DeserializeAndConstructor a = new DeserializeAndConstructor();
        assertTrue(a.isCalled());
        assertNotNull(a.getFoo());
        assertNotNull(a.getBar());

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(a);
            os.flush();
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DeserializeAndConstructor b;
        try (ObjectInputStream in = new ObjectInputStream(bais)) {
            b = (DeserializeAndConstructor) in.readObject();
        }

        assertFalse(b.isCalled());
        assertNull(b.getFoo());
        assertNull(b.getBar());
    }

    @Test
    void test2() throws Exception {
        final DeserializeAndConstructor b = SerializableConstructorInvoker
                .newInstance(DeserializeAndConstructor.class);
        assertFalse(b.isCalled());
        assertNull(b.getFoo());
        assertNull(b.getBar());
    }
}
