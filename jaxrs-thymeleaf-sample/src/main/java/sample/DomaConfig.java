package sample;

import javax.inject.Inject;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.dialect.Dialect;

public class DomaConfig extends DomaAbstractConfig {

    @Inject
    private DataSource dataSource;

    @Inject
    private Dialect dialect;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }
}
