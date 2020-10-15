package com.example;

import java.time.Duration;

import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class App {

	public static void main(final String[] args) {
		final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(
				PrometheusConfig.DEFAULT);

		final Timer timer = Timer.builder("test/test/test")
				.publishPercentiles(0.5, 0.9, 0.95, 0.99, 0.999)
				.serviceLevelObjectives(Duration.ofMillis(100), Duration.ofMillis(400),
						Duration.ofMillis(500), Duration.ofMillis(2000))
				.register(registry);

		for (int i = 0; i < 1000; i++) {
			timer.record(Duration.ofMillis((i + 1) * 3));
		}

		System.out.println(registry.scrape());
	}
}
