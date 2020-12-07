package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class App {
	public static void main(String[] args) throws Exception {
		String url = getValue("JDBC_URL", "jdbc:postgresql://localhost:5432/example");
		String user = getValue("JDBC_USER", "example");
		String password = getValue("JDBC_PASSWORD", "example");
		Configuration configuration = new FluentConfiguration()
				.dataSource(url, user, password);
		Flyway flyway = new Flyway(configuration);

		int size = Runtime.getRuntime().availableProcessors() - 1;
		ExecutorService executor = Executors.newFixedThreadPool(size);
		try {
			CountDownLatch ready = new CountDownLatch(size);
			CountDownLatch go = new CountDownLatch(1);
			List<Future<?>> futures = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				Future<?> future = executor.submit(() -> {
					ready.countDown();
					go.await();
					int value = flyway.migrate();
					return String.format("[%s] %d", Thread.currentThread().getName(), value);
				});
				futures.add(future);
			}

			ready.await();
			TimeUnit.SECONDS.sleep(1);
			go.countDown();
			for (Future<?> future : futures) {
				System.out.println(future.get());
			}
		} finally {
			executor.shutdown();
		}
	}

	static String getValue(String name, String defaultValue) {
		String value = System.getenv(name);
		if (value != null && value.isBlank() == false) {
			return value;
		}
		return defaultValue;
	}
}
