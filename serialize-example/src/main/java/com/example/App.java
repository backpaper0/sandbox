package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class App {

    public static void main(final String[] args) throws Exception {
        final ClassLoader classLoader1 = compile("Foo.1");
        final ClassLoader classLoader2 = compile("Foo.2");

        final Store store1 = Store.getInstance(classLoader1);
        final Store store2 = Store.getInstance(classLoader2);

        final Object instance1 = classLoader1
                .loadClass("com.example.Foo")
                .getDeclaredConstructor(String.class)
                .newInstance("Hello, world!");

        final Object instance2 = Store.writeAndRead(store1, store2, instance1);

        System.out.println(instance1);
        System.out.println(instance2);
    }

    static ClassLoader compile(final String... fileNames)
            throws Exception {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (final StandardJavaFileManager javaFileManager = compiler.getStandardFileManager(null,
                null,
                null)) {

            final Path classOutputDir = Files.createTempDirectory(Paths.get("target"), null);

            javaFileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                    Collections.singleton(classOutputDir.toFile()));

            final List<JavaFileObject> compilationUnits = new ArrayList<>();
            compilationUnits.add(JavaSourceFromString.get("DefaultStore"));
            for (final String fileName : fileNames) {
                compilationUnits.add(JavaSourceFromString.get(fileName));
            }
            final CompilationTask task = compiler.getTask(null, javaFileManager, null, null, null,
                    compilationUnits);
            if (task.call() == false) {
                throw new RuntimeException();
            }

            return URLClassLoader.newInstance(new URL[] {
                    classOutputDir.toUri().toURL()
            });
        }
    }

    static class JavaSourceFromString extends SimpleJavaFileObject {

        final String code;

        static JavaFileObject get(final String fileName) throws IOException {
            final Path sourceFile = Paths.get("files").resolve(fileName + Kind.SOURCE.extension);
            final byte[] data = Files.readAllBytes(sourceFile);
            final String sourceCode = new String(data);
            return new JavaSourceFromString(fileName.split("\\.")[0], sourceCode);
        }

        JavaSourceFromString(final String name, final String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
                    Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
