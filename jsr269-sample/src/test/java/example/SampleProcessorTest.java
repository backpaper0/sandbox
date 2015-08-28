package example;

import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import org.junit.Test;
import org.truth0.Truth;

import com.google.common.truth.codegen.CompilingClassLoader;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;

import sample.SampleProcessor;

public class SampleProcessorTest {

    @Test
    public void testAnnotationProcessing() throws Exception {
        Truth.ASSERT.about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forSourceLines("Hello",
                        "@sample.Sample public class Hello {}"))
                .processedWith(new SampleProcessor()).compilesWithoutError();
    }

    @Test
    public void testCompilingClassLoader() throws Exception {
        DiagnosticListener<JavaFileObject> collector = new DiagnosticCollector<>();

        Class<?> c1 = new CompilingClassLoader(null, "Foo",
                "public class Foo {}", collector).loadClass("Foo");

        Class<?> c2 = new CompilingClassLoader(null, "Foo",
                "public class Foo {}", collector).loadClass("Foo");

        System.out.printf("%s(%s)%n", c1, c1.getClassLoader());
        System.out.printf("%s(%s)%n", c2, c2.getClassLoader());
        System.out.printf("%s == %s : %s%n", c1, c2, c1 == c2);
    }
}
