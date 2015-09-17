package experiment;

import javax.ejb.Singleton;

@Singleton
public class EchoBean3 {

    public String echo(String s) {
        return s;
    }
}
