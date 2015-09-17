package experiment;

import javax.ejb.Stateless;

@Stateless
public class EchoBean2 {

    public String echo(String s) {
        return s;
    }
}
