package app;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return String.format("Hello, %s!", name);
    }
}
