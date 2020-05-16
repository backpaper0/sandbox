package com.example;

import java.net.http.HttpClient;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import okhttp3.mockwebserver.MockWebServer;

public final class HttpClientTestingExtension
		implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	private MockWebServer server;

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		server = new MockWebServer();
		server.start();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		server.shutdown();
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext,
			ExtensionContext extensionContext) throws ParameterResolutionException {
		final Class<?> clazz = parameterContext.getParameter().getType();
		return clazz == HttpClient.class || clazz == MockWebServer.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext,
			ExtensionContext extensionContext) throws ParameterResolutionException {
		final Class<?> clazz = parameterContext.getParameter().getType();
		if (clazz == HttpClient.class) {
			return HttpClient.newHttpClient();
		} else if (clazz == MockWebServer.class) {
			return server;
		}
		throw new ParameterResolutionException(clazz.getName() + " is not supported");
	}
}
