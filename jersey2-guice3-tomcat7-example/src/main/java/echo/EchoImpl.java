package echo;

public class EchoImpl implements Echo {

    @Override
    public String apply(String in) {
        return in;
    }
}
