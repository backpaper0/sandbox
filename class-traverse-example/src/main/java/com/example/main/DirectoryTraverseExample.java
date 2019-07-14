package com.example.main;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DirectoryTraverseExample {

    public static void main(final String[] args) throws Exception {
        final URI uri = DirectoryTraverseExample.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI();
        final Path dir = Paths.get(uri);

        try (Stream<Path> files = Files.walk(dir)) {
            files.filter(Files::isRegularFile).forEach(file -> {
                System.out.printf("%s%n", file);
            });
        }
    }
}
