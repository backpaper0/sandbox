package sample;

import javax.enterprise.inject.Produces;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class DialectProducer {

    @Produces
    Dialect dialect() {
        return new StandardDialect();
    }
}
