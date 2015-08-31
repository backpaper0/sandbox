package sample;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

//テストデータを準備するためのクラス。

@WebListener
public class TestData implements ServletContextListener {

    @Resource
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection con = dataSource.getConnection();
                Statement st = con.createStatement()) {
            st.execute(
                    "CREATE TABLE message(id varchar(100) primary key, template varchar(100))");
            boolean autoCommit = con.getAutoCommit();
            try {
                con.setAutoCommit(true);
                st.executeUpdate(
                        "INSERT INTO message(id, template) VALUES ('hello', 'Hello, %s!')");
            } finally {
                con.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try (Connection con = dataSource.getConnection();
                Statement st = con.createStatement()) {
            st.execute("DROP TABLE message");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
