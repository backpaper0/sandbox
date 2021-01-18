package example.javacompiler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;

import example.javacompiler.processor.AnnotationProcessorDemo;

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

	@Test
	void annotationProcessor() throws Exception {
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final InMemoryClassLoader classLoader = new InMemoryClassLoader();
		final InMemoryJavaFileManager fileManager = new InMemoryJavaFileManager(
				compiler.getStandardFileManager(null, null, null), classLoader);

		final Set<InMemoryJavaFile> compilationUnits = new HashSet<>();
		final Set<Path> files = Files.walk(Path.of("src/test/java/example/javacompiler/classes"))
				.filter(Files::isRegularFile)
				.collect(Collectors.toSet());
		for (final Path file : files) {
			String className = Path.of("src/test/java").relativize(file).toString();
			className = className.substring(0, className.length() - ".java".length())
					.replace(File.separatorChar, '.');
			final InMemoryJavaFile javaFile = new InMemoryJavaFile(className,
					Files.readString(file));
			compilationUnits.add(javaFile);
		}

		final CompilationTask task = compiler.getTask(null, fileManager, null, null, null,
				compilationUnits);
		final AnnotationProcessorDemo processor = new AnnotationProcessorDemo();
		final Iterable<? extends Processor> processors = Set.of(processor);
		task.setProcessors(processors);

		assertTrue(task.call());
	}
}
