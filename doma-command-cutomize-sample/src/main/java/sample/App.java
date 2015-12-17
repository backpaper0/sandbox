package sample;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.seasar.doma.jdbc.CommandImplementors;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.UtilLoggingJdbcLogger;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.query.SelectQuery;

public class App {

    public static void main(String[] args) {

        CommandImplementors commandImplementors = new CommandImplementors() {
            @Override
            public <RESULT> SelectCommand<RESULT> createSelectCommand(
                    Method method, SelectQuery query,
                    ResultSetHandler<RESULT> resultSetHandler) {
                return new SelectCommand<RESULT>(query, resultSetHandler) {
                    @Override
                    protected Supplier<RESULT> executeQuery(
                            PreparedStatement preparedStatement)
                                    throws SQLException {
                        final long start = System.currentTimeMillis();
                        try {
                            return super.executeQuery(preparedStatement);
                        } finally {
                            //閾値を設定すればスロークエリをログ出力する、とか出来る。
                            final long duration = System.currentTimeMillis()
                                    - start;
                            System.out.println(duration + "msec");
                        }
                    }
                };
            }
        };

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:sample;DB_CLOSE_DELAY=-1");

        Config config = new Config() {
            Dialect dialect = new H2Dialect();
            JdbcLogger jdbcLogger = new UtilLoggingJdbcLogger(Level.CONFIG);

            @Override
            public Dialect getDialect() {
                return dialect;
            }

            @Override
            public DataSource getDataSource() {
                return dataSource;
            }

            @Override
            public JdbcLogger getJdbcLogger() {
                return jdbcLogger;
            }

            @Override
            public CommandImplementors getCommandImplementors() {
                return commandImplementors;
            }
        };

        SampleDao dao = new SampleDaoImpl(config);
        dao.createTable();
        String message = IntStream.range(0, 1000).mapToObj(j -> "*")
                .collect(Collectors.joining());
        dao.insert(IntStream.range(0, 1000).mapToObj(i -> {
            Sample entity = new Sample();
            entity.message = message;
            return entity;
        }).collect(Collectors.toList()));

        System.out.println("start");
        long count = dao.select(s -> s.count());
        System.out.println("end(count = " + count + ")");
    }
}
