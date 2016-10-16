package sample.config;

import javax.sql.DataSource;

import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;

@SingletonConfig
public class SampleConfig implements Config {

    private static SampleConfig instance = new SampleConfig();

    private final LocalTransactionDataSource dataSource = new LocalTransactionDataSource(
            "jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
    private final Dialect dialect = new PostgresDialect();

    private SampleConfig() {
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return new LocalTransactionManager(dataSource.getLocalTransaction(getJdbcLogger()));
    }

    public static SampleConfig singleton() {
        return instance;
    }
}
