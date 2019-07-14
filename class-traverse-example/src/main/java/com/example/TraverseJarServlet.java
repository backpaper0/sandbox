package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.doma.Entity;

@WebServlet(urlPatterns = "/traverse")
public class TraverseJarServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(200);
        try (final PrintWriter out = resp.getWriter()) {
            traverse(out, Entity.class);
            traverse(out, getClass());
            out.flush();
        }
    }

    private static void traverse(final PrintWriter out, final Class<?> baseClass)
            throws ServletException, IOException {
        try {
            final CodeSource cs = baseClass.getProtectionDomain().getCodeSource();
            if (cs != null) {
                final Path fileOrDir = Paths.get(cs.getLocation().toURI());
                if (Files.isDirectory(fileOrDir)) {
                    int i = 0;
                    try (Stream<Path> stream = Files.walk(fileOrDir)) {
                        final List<Path> files = stream.filter(Files::isRegularFile)
                                .collect(Collectors.toList());
                        for (final Path file : files) {
                            if (Files.isRegularFile(file)) {
                                out.printf("%s[%s]%s%n", fileOrDir, ++i, file);
                            }
                        }
                    }
                } else {
                    try (JarInputStream in = new JarInputStream(Files.newInputStream(fileOrDir))) {
                        JarEntry entry = null;
                        int i = 0;
                        while (null != (entry = in.getNextJarEntry())) {
                            if (entry.isDirectory() == false) {
                                out.printf("%s[%s]%s%n", fileOrDir, ++i, entry.getName());
                            }
                        }
                    }
                }
            }
        } catch (final URISyntaxException e) {
            throw new ServletException(e);
        }
    }
}
