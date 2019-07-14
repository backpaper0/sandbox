package com.example.main;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.seasar.doma.Entity;

public class JarTraverseExample {

    public static void main(final String[] args) throws Exception {
        final URI uri = Entity.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        final Path file = Paths.get(uri);
        try (JarInputStream in = new JarInputStream(Files.newInputStream(file))) {
            JarEntry entry = null;
            while (null != (entry = in.getNextJarEntry())) {
                if (entry.isDirectory() == false) {
                    System.out.printf("%s%n", entry.getName());
                }
            }
        }
    }
}
