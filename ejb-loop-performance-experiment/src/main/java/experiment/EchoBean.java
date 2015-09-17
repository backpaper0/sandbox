package experiment;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class EchoBean {

    public String echo(String s) {
        return s;
    }

    @Transactional
    public String echoInTx(String s) {
        return s;
    }
}
