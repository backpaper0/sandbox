package sample;

public class HelloService {

    private String message;

    public void afterPropertiesSet() {
        message = "Hello, world!";
    }

    public String hello() {
        return message;
    }
}
