package com.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class App {

    public static void main(final String[] args) throws Exception {

        new App().start();

        System.out.println("Tomcat started");

        TimeUnit.DAYS.sleep(1);
    }

    public Server start() throws Exception {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        final Context context = tomcat.addWebapp("", Files
                .createDirectories(Paths.get("target/docbase")).toAbsolutePath().toString());

        final WebResourceRoot resources = new StandardRoot(context);

        final DirResourceSet webResourceSet = new DirResourceSet();
        webResourceSet.setWebAppMount("/WEB-INF/classes");
        webResourceSet.setBase(Paths.get("target/classes").toAbsolutePath().toString());

        resources.addPreResources(webResourceSet);
        context.setResources(resources);

        tomcat.start();

        return new Server(tomcat);
    }

    public static class Server {

        private final Tomcat tomcat;

        public Server(final Tomcat tomcat) {
            this.tomcat = tomcat;
        }

        public void stop() throws Exception {
            tomcat.stop();
        }
    }
}