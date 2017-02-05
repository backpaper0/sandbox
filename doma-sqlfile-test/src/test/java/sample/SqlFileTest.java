package sample;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import sample.SqlFileTest.SqlFileTestRunner;

@RunWith(SqlFileTestRunner.class)
public class SqlFileTest {

    public static class SqlFileTestRunner extends org.junit.runner.Runner {

        private final Class<?> testClass;
        private final List<DaoDirectory> daoDirectories;

        public SqlFileTestRunner(Class<?> testClass) throws IOException {
            this.testClass = testClass;
            Path root = Paths.get("src", "main", "resources", "META-INF");
            DaoDirectory.Builder builder = new DaoDirectory.Builder(root);
            Map<Path, List<Path>> dirs = Files
                    .walk(root)
                    .filter(x -> x.getFileName().toString().endsWith(".sql"))
                    .collect(Collectors.groupingBy(Path::getParent));
            this.daoDirectories = dirs.entrySet()
                    .stream()
                    .map(builder::build)
                    .collect(Collectors.toList());
        }

        @Override
        public Description getDescription() {
            Description description = Description.createSuiteDescription(testClass);
            daoDirectories.stream()
                    .map(DaoDirectory::getDescription)
                    .forEach(description::addChild);
            return description;
        }

        @Override
        public void run(RunNotifier notifier) {
            try (Connection con = getConnection();
                    Statement statement = con.createStatement()) {
                ExecutionContext context = new ExecutionContext(notifier, statement);
                Executor executor = new Executor(context);
                daoDirectories.forEach(executor::execute);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }

        protected Connection getConnection() throws SQLException {
            Properties props = new Properties();
            Path propertiesFile = Paths.get("src", "main", "resources", "application.properties");
            try (Reader in = Files.newBufferedReader(propertiesFile)) {
                props.load(in);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            String url = props.getProperty("spring.datasource.url");
            String user = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");
            return DriverManager.getConnection(url, user, password);
        }
    }

    public static class ExecutionContext {
        private final RunNotifier notifier;
        private final Statement statement;
        public ExecutionContext(RunNotifier notifier, Statement statement) {
            this.notifier = notifier;
            this.statement = statement;
        }
        public RunNotifier getNotifier() {
            return notifier;
        }
        public Statement getStatement() {
            return statement;
        }
    }

    public interface Executable {
        void execute(ExecutionContext context, Executor executor);
    }

    public static class Executor {
        private final ExecutionContext context;
        public Executor(ExecutionContext context) {
            this.context = context;
        }
        public void execute(Executable executable) {
            executable.execute(context, this);
        }
    }

    public static class DaoDirectory implements Executable {

        private final String name;
        private final List<SqlFile> sqlFiles;

        public DaoDirectory(String name, List<Path> files) {
            this.name = name;
            SqlFile.Builder builder = new SqlFile.Builder(name);
            this.sqlFiles = files.stream()
                    .map(builder::build)
                    .collect(Collectors.toList());
        }

        @Override
        public void execute(ExecutionContext context, Executor executor) {
            sqlFiles.forEach(executor::execute);
        }

        public Description getDescription() {
            Description description = Description.createSuiteDescription(name);
            sqlFiles.stream()
                    .map(SqlFile::getDescription)
                    .forEach(description::addChild);
            return description;
        }

        public static class Builder {
            private final Path root;
            public Builder(Path root) {
                this.root = root;
            }
            public DaoDirectory build(Map.Entry<Path, List<Path>> entry) {
                String name = root.resolve(entry.getKey()).toString();
                List<Path> files = entry.getValue();
                return new DaoDirectory(name, files);
            }
        }
    }

    public static class SqlFile implements Executable {

        private final String parentName;
        private final Path file;

        public SqlFile(String parentName, Path file) {
            this.parentName = parentName;
            this.file = file;
        }

        public Description getDescription() {
            return Description.createTestDescription(
                    parentName,
                    file.getFileName().toString());
        }

        @Override
        public void execute(ExecutionContext context, Executor executor) {
            Description description = getDescription();
            RunNotifier notifier = context.getNotifier();
            notifier.fireTestStarted(description);
            try {
                String sql = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                context.getStatement().execute(sql);
            } catch (Exception e) {
                Failure failure = new Failure(description, e);
                notifier.fireTestFailure(failure);
            } finally {
                notifier.fireTestFinished(description);
            }
        }

        public static class Builder {
            private String parentName;
            public Builder(String parentName) {
                this.parentName = parentName;
            }
            public SqlFile build(Path file) {
                return new SqlFile(parentName, file);
            }
        }
    }
}
