package com.example;

import java.io.IOException;
import java.util.EnumSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@WebListener
public class Initializer implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(Initializer.class.getName());

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		final boolean awsXrayEnabled = Boolean.parseBoolean(System.getenv("AWS_XRAY_ENABLED"));
		logger.info(() -> "AWS_XRAY_ENABLED = " + awsXrayEnabled);

		final ServletContext sc = sce.getServletContext();

		String version = System.getenv("APP_VERSION");
		if (version == null || version.isBlank()) {
			version = "NONE";
		}
		sc.setAttribute("app.version", version);
		sc.setAttribute("app.instance.id", UUID.randomUUID().toString());

		if (awsXrayEnabled) {
			final FilterRegistration.Dynamic reg = sc.addFilter(
					AWSXRayServletFilter.class.getSimpleName(),
					AWSXRayServletFilter.class);
			reg.setInitParameter("fixedName", "demo-caller");
			reg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		}

		CloseableHttpClient httpClient;
		if (awsXrayEnabled) {
			httpClient = com.amazonaws.xray.proxies.apache.http.HttpClientBuilder.create().build();
		} else {
			httpClient = HttpClientBuilder.create().build();
		}
		sc.setAttribute(HttpClient.class.getName(), httpClient);
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		final ServletContext sc = sce.getServletContext();
		final Object httpclient = sc.getAttribute(HttpClient.class.getName());
		if (httpclient != null && httpclient instanceof CloseableHttpClient) {
			try {
				((CloseableHttpClient) httpclient).close();
			} catch (final IOException e) {
				if (logger.isLoggable(Level.WARNING)) {
					logger.log(Level.WARNING, "", e);
				}
			}
		}
	}
}
