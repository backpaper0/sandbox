package example.annotation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AnnotationTest {

	@Test
	void getAnnotationFoo() throws Exception {
		assertNull(Fuga.class.getAnnotation(Foo.class));
	}

	@Test
	void getAnnotationBar() throws Exception {
		assertNotNull(Fuga.class.getAnnotation(Bar.class));
	}

	@Test
	void getDeclaredAnnotationFoo() throws Exception {
		assertNull(Fuga.class.getDeclaredAnnotation(Foo.class));
	}

	@Test
	void getDeclaredAnnotationBar() throws Exception {
		assertNull(Fuga.class.getDeclaredAnnotation(Bar.class));
	}
}
