package example.annotation;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationTest {

	private Class<?> clazz;
	private Method method;

	@BeforeEach
	void init() throws Exception {
		clazz = Fuga.class;
		method = clazz.getDeclaredMethod("method");
	}

	@Test
	void classGetAnnotationFoo() throws Exception {
		assertNull(clazz.getAnnotation(Foo.class));
	}

	@Test
	void classGetAnnotationBar() throws Exception {
		assertNotNull(clazz.getAnnotation(Bar.class));
	}

	@Test
	void classGetDeclaredAnnotationFoo() throws Exception {
		assertNull(clazz.getDeclaredAnnotation(Foo.class));
	}

	@Test
	void classGetDeclaredAnnotationBar() throws Exception {
		assertNull(clazz.getDeclaredAnnotation(Bar.class));
	}

	@Test
	void methodGetAnnotationFoo() throws Exception {
		assertNull(method.getAnnotation(Foo.class));
	}

	@Test
	void methodGetAnnotationBar() throws Exception {
		assertNull(method.getAnnotation(Bar.class));
	}

	@Test
	void methodGetDeclaredAnnotationFoo() throws Exception {
		assertNull(method.getDeclaredAnnotation(Foo.class));
	}

	@Test
	void methodGetDeclaredAnnotationBar() throws Exception {
		assertNull(method.getDeclaredAnnotation(Bar.class));
	}
}
