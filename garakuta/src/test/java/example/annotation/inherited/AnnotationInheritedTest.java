package example.annotation.inherited;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnnotationInheritedTest {

	private Class<?> subClass;
	private Method overriddenMethod;
	private Method methodViaSuperClass;

	@BeforeEach
	void init() throws ReflectiveOperationException {
		subClass = Fuga.class;
		overriddenMethod = subClass.getMethod("method1");
		methodViaSuperClass = subClass.getMethod("method2");
	}

	// Fuga (extends Hoge)

	@Test
	void notGetAnnotationInSubClassWithoutInherited() {
		assertNull(subClass.getAnnotation(Foo.class));
	}

	@Test
	void getAnnotationInSubClassWithInherited() {
		assertNotNull(subClass.getAnnotation(Bar.class));
	}

	@Test
	void notGetDeclaredAnnotationInSubClassWithoutInherited() {
		assertNull(subClass.getDeclaredAnnotation(Foo.class));
	}

	@Test
	void notGetDeclaredAnnotationInSubClassWithInherited() {
		assertNull(subClass.getDeclaredAnnotation(Bar.class));
	}

	// Fuga.method1 (overridden)

	@Test
	void notGetAnnotationInOverriddenMethodWithoutInherited() {
		assertNull(overriddenMethod.getAnnotation(Foo.class));
	}

	@Test
	void notGetAnnotationInOverriddenMethodWithInherited() {
		assertNull(overriddenMethod.getAnnotation(Bar.class));
	}

	@Test
	void notGetDeclaredAnnotationInOverriddenMethodWithoutInherited() {
		assertNull(overriddenMethod.getDeclaredAnnotation(Foo.class));
	}

	@Test
	void notGetDeclaredAnnotationInOverriddenMethodWithInherited() {
		assertNull(overriddenMethod.getDeclaredAnnotation(Bar.class));
	}

	// Hoge.method2

	@Test
	void getAnnotationInMethodViaSuperClassWithoutInherited() {
		assertNotNull(methodViaSuperClass.getAnnotation(Foo.class));
	}

	@Test
	void getAnnotationInMethodViaSuperClassWithInherited() {
		assertNotNull(methodViaSuperClass.getAnnotation(Bar.class));
	}

	@Test
	void getDeclaredAnnotationInMethodViaSuperClassWithoutInherited() {
		assertNotNull(methodViaSuperClass.getDeclaredAnnotation(Foo.class));
	}

	@Test
	void getDeclaredAnnotationInMethodViaSuperClassWithInherited() {
		assertNotNull(methodViaSuperClass.getDeclaredAnnotation(Bar.class));
	}
}
