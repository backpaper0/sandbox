package com.example;

import java.io.File;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ClassLoaderIT {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        File[] jars = Maven.resolver().loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(Foo.class, Bar.class)
                .addAsLibraries(jars);

        //WARファイルにservlet-api.jarが入ることを確認
        System.out.println(war.toString(true));

        return war;
    }

    @Test
    public void test(@ArquillianResource URL resource) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URL(resource, "/test/foo").toURI()).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        //Arquillian JUnit container使うからしゃーなしテストコードにしているけど目視でコンソールを確認するやつ
        System.out.println(body);
    }
}
