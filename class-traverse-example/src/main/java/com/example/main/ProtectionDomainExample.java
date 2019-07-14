package com.example.main;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.seasar.doma.Entity;

public class ProtectionDomainExample {

    public static void main(final String[] args) throws Exception {
        example(ProtectionDomainExample.class);
        example(Entity.class);
        example(Object.class);
    }

    private static void example(final Class<?> clazz) throws URISyntaxException {
        final ProtectionDomain pd = clazz.getProtectionDomain();
        final CodeSource cs = pd.getCodeSource();
        if (cs != null) {
            final URL location = cs.getLocation();
            System.out.printf("%s ( %s )%n", clazz.getName(), location);

            final Path file = Paths.get(location.toURI());
            System.out.printf("    directory = %s%n", Files.isDirectory(file));
        } else {
            System.out.printf("%s ( - )%n", clazz.getName());
        }
    }
}
