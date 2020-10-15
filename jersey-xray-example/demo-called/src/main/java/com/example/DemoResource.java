package com.example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/id")
public class DemoResource {

	@Context
	private HttpServletRequest request;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Object get() {
		final ServletContext sc = request.getServletContext();
		final Object version = sc.getAttribute("app.version");
		final Object instanceId = sc.getAttribute("app.instance.id");
		return String.format("%s:%s", version, instanceId);
	}
}
