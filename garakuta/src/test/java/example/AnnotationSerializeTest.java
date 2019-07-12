package example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.inject.Named;

import org.junit.jupiter.api.Test;

class AnnotationSerializeTest {

    @Named("foobar")
    Object obj;

    @Test
    void test() throws Exception {
        final Named named = getClass().getDeclaredField("obj").getAnnotation(Named.class);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(named);
            oos.flush();
        }

        Named deserialized;
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserialized = (Named) ois.readObject();
        }

        assertEquals(named, deserialized);
    }
}
