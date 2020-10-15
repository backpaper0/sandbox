package com.example;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

@Path("/id")
public class DemoResource {

	@Context
	private HttpServletRequest request;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Object get() throws Exception {
		final ServletContext sc = request.getServletContext();
		final Object version = sc.getAttribute("app.version");
		final Object instanceId = sc.getAttribute("app.instance.id");
		return String.format("%s:%s-%s", version, instanceId, called());
	}

	private String called() throws Exception {
		final HttpGet request = new HttpGet(calledURI() + "/api/id");
		final HttpClient httpClient = getHttpClient();
		final HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new InternalServerErrorException();
		}
		try (InputStream in = response.getEntity().getContent()) {
			return new String(in.readAllBytes());
		}
	}

	private static String calledURI() {
		final String uri = System.getenv("CALLED_URI");
		if (uri != null && uri.isBlank() == false) {
			return uri;
		}
		return "http://localhost:8081";
	}

	private HttpClient getHttpClient() {
		final Object httpClient = request.getServletContext()
				.getAttribute(HttpClient.class.getName());
		if (httpClient == null || httpClient instanceof HttpClient == false) {
			throw new InternalServerErrorException();
		}
		return (HttpClient) httpClient;
	}
}
