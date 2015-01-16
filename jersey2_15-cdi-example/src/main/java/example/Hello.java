package example;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Hello {

    public String say(String name) {
        return String.format("Hello, %s!", name);
    }
}
