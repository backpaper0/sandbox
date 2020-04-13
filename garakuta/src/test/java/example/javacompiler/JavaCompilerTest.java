package example.javacompiler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;

class JavaCompilerTest {

    @Test
    void compile() throws Exception {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final InMemoryClassLoader classLoader = new InMemoryClassLoader();
        final InMemoryJavaFileManager fileManager = new InMemoryJavaFileManager(
                compiler.getStandardFileManager(null, null, null), classLoader);
        final InMemoryJavaFile demo = new InMemoryJavaFile("com.example.Demo");
        final String value = UUID.randomUUID().toString();
        try (PrintWriter out = new PrintWriter(demo.openWriter())) {
            out.println("package com.example;");
            out.println("");
            out.println("import java.util.function.Supplier;");
            out.println("");
            out.println("public class Demo implements Supplier<String> {");
            out.println("    @Override");
            out.println("    public String get() {");
            out.println("        return \"" + value + "\";");
            out.println("    }");
            out.println("}");
        }
        final Iterable<? extends JavaFileObject> compilationUnits = Set.of(demo);
        final CompilationTask task = compiler.getTask(null, fileManager, null, null, null,
                compilationUnits);

        assertTrue(task.call());

        final Class<?> clazz = Class.forName("com.example.Demo", true, classLoader);

        final Supplier<String> instance = (Supplier<String>) clazz.getConstructor().newInstance();
        assertEquals(value, instance.get());
    }
}
