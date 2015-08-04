package sample;

public class HelloImpl implements Hello {

    @Override
    public String greet(String yourName) {
        return String.format("Hello, %s!", yourName);
    }
}
